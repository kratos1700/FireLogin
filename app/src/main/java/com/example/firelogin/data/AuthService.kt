package com.example.firelogin.data


import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthService  @Inject constructor( private val firebaseAuth: FirebaseAuth){

    suspend fun login(email: String, password: String): FirebaseUser? {
      return  firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

   suspend fun singUp(email: String, password: String): FirebaseUser? {
      return suspendCancellableCoroutine {cancellableContinuation ->
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

    fun logout(){
        signOut()
    }

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


}