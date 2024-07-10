package com.example.firelogin.data


import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) // @ApplicationContext es una anotación de Dagger Hilt que nos permite inyectar el contexto de la aplicación
{

    // LOGIN EN EMAIL I PASSWORD
    suspend fun login(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    suspend fun singUp(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user: FirebaseUser? = it.user
                    cancellableContinuation.resume(user)

                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }
        }

    }


    fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    private fun getCurrentUser() = firebaseAuth.currentUser


    private fun signOut() {
        firebaseAuth.signOut()
    }

    fun logout() {
        signOut()
    }


// LOGIN EN NUM TELF

    fun loginWithPhone(
        phoneNumber: String,
        activity: Activity,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {

        // per fer proves
        //  firebaseAuth.firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber("+34123456789", "123456") // Simular el código de verificación en un dispositivo real

        val opcions = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(opcions)

    }

    suspend fun verifyCode(
        verificationCode: String,
        phoneCode: String
    ): FirebaseUser? { //verificationCode: String es el código que se envía al teléfono y pinVerifed: String es el código que introduce el usuario
        val credentials = PhoneAuthProvider.getCredential(verificationCode, phoneCode)
        return completeRegistrerWithCredentials(credentials)
    }


    // suspend fun completeRegistrerWithPhone(credential: PhoneAuthCredential): FirebaseUser? {  //PhoneAuthCredential extent de AuthCredential per aixo podem utilitzar ho
    private suspend fun completeRegistrerWithCredentials(credential: AuthCredential): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }

        }
    }

    suspend fun completeRegistrerWithPhoneVerification(credential: PhoneAuthCredential) =
        completeRegistrerWithCredentials(credential)


    // LOGIN EN GOOGLE
    fun getGoogleClient(): GoogleSignInClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.example.firelogin.R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)


    }

    suspend fun loginWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        // firebaseAuth.signInWithCredential(credential)

        return completeRegistrerWithCredentials(credential)

    }
    // LOGIN EN GITHUB

    suspend fun loginWithGithub(activity: Activity) : FirebaseUser?{
        val provider = OAuthProvider.newBuilder("github.com").apply {
            scopes = listOf("user:email")  // permisos que necesitamos para acceder a la cuenta de github
        }

     return  suspendCancellableCoroutine<FirebaseUser?> { cancellableContinuation -> //suspendCancellableCoroutine es una función de extensión que nos permite crear una suspensión personalizada
            firebaseAuth.pendingAuthResult?.addOnSuccessListener {//pendingAuthResult es una propiedad de FirebaseAuth que nos permite obtener el resultado de la autenticación pendiente
                cancellableContinuation.resume(it.user)  //resume es una función de la suspensión que nos permite reanudar la ejecución de la suspensión con un valor
            }?.addOnFailureListener { // addOnFailureListener es una función de la suspensión que nos permite agregar un oyente que se ejecutará cuando la suspensión falle
                cancellableContinuation.resumeWithException(it) //resumeWithException es una función de la suspensión que nos permite reanudar la ejecución de la suspensión con una excepción
            } ?:completeRegisterWithProvider(activity, provider.build(), cancellableContinuation) // ?: es un operador de elvis que nos permite ejecutar una expresión si el valor de la izquierda es nulo

        }

    }

    private fun completeRegisterWithProvider(
        activity: Activity,
        provider: OAuthProvider,
        cancellableContinuation: CancellableContinuation<FirebaseUser?>
    ){
        firebaseAuth.startActivityForSignInWithProvider(activity, provider)  //startActivityForSignInWithProvider es una función de FirebaseAuth que nos permite iniciar la actividad de autenticación con un proveedor de OAuth
            .addOnSuccessListener {
                cancellableContinuation.resume(it.user)  //
            }
            .addOnFailureListener {
                cancellableContinuation.resumeWithException(it)//resumeWithException es una función de la suspensión que nos permite reanudar la ejecución de la suspensión con una excepción
            }
    }


    // LOGIN EN TWITTER

   private suspend fun initRegisterWithProvider(activity: Activity,provider :OAuthProvider) : FirebaseUser?{
        return  suspendCancellableCoroutine<FirebaseUser?> { cancellableContinuation -> //suspendCancellableCoroutine es una función de extensión que nos permite crear una suspensión personalizada
            firebaseAuth.pendingAuthResult?.addOnSuccessListener {//pendingAuthResult es una propiedad de FirebaseAuth que nos permite obtener el resultado de la autenticación pendiente
                cancellableContinuation.resume(it.user)  //resume es una función de la suspensión que nos permite reanudar la ejecución de la suspensión con un valor
            }?.addOnFailureListener { // addOnFailureListener es una función de la suspensión que nos permite agregar un oyente que se ejecutará cuando la suspensión falle
                cancellableContinuation.resumeWithException(it) //resumeWithException es una función de la suspensión que nos permite reanudar la ejecución de la suspensión con una excepción
            } ?:completeRegisterWithProvider(activity, provider, cancellableContinuation) // ?: es un operador de elvis que nos permite ejecutar una expresión si el valor de la izquierda es nulo

        }

    }


   suspend fun loginWithTwitter(activity: Activity): FirebaseUser? {
        val provider = OAuthProvider.newBuilder("twitter.com").build()
        return initRegisterWithProvider(activity, provider)
    }


}