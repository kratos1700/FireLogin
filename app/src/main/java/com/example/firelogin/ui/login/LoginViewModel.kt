package com.example.firelogin.ui.login

import android.app.Activity
import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firelogin.data.AuthService
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _verificationCode = MutableStateFlow<String?>(null)
    val verificationCode: StateFlow<String?> = _verificationCode

    private val _codeSent = MutableStateFlow(false)
    val codeSent: StateFlow<Boolean> = _codeSent


    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            _isLoggedIn.value = authService.isUserLoggedIn()
        }
    }
    // LOGIN EN EMAIL I PASSWORD

    fun login(email: String, password: String, navigateToDetail: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true

            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                authService.login(email, password)
            }

            if (result != null) {
                _loading.value = false
                _isLoggedIn.value = true
                navigateToDetail()
            }
            _loading.value = false
        }
    }

    // LOGIN EN NUM TELF
    fun loginWithPhone(phoneNumber: String, activity: Activity) {
        _loading.value = true

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                viewModelScope.launch {
                    val user = authService.completeRegistrerWithPhoneVerification(credential)
                    if (user != null) {
                        _isLoggedIn.value = true
                    }
                    _loading.value = false
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("PhoneVerificationScreen", "onVerificationFailed", e)
                _loading.value = false
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                _verificationCode.value = verificationId
                _codeSent.value = true
                _loading.value = false
                Log.d("PhoneVerificationScreen", "ON_SEND_verificationId: $verificationId")

            }
        }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authService.loginWithPhone(phoneNumber, activity, callbacks)
            }
        }
    }

    fun verifyCode(phoneCode: String) {
        val verificationId = _verificationCode.value
        Log.d(
            "PhoneVerificationScreen",
            "Verifi_verificationCode.value: ${_verificationCode.value}"
        )

        if (verificationId != null) {
            _loading.value = true
            viewModelScope.launch {
                val user = authService.verifyCode(verificationId, phoneCode)
                if (user != null) {
                    _isLoggedIn.value = true
                } else {
                    _loading.value = false
                    Log.e("PhoneVerificationScreen", "Verification failed")
                }
            }
        } else {
            Log.e("PhoneVerificationScreen", "Verification ID is null")
        }
    }


    // LOGIN EN GOOGLE
    fun onGoogleLoginSelected(googleLauncherLogin: (GoogleSignInClient) -> Unit) {

        val gsc = authService.getGoogleClient()

        googleLauncherLogin(gsc)

    }

    fun loginWithGoogle(idToken: String, navigateToDetail: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO) {
                authService.loginWithGoogle(idToken)
            }
            if (result != null) {
                _loading.value = true
                navigateToDetail()
            }
            _loading.value = false
        }

    }

    // LOGIN EN GITHUB


    fun onGithubLoginSelected(activity: Activity, navigateToDetail: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO) {
                authService.loginWithGithub(activity)
            }
            if (result != null) {
                _loading.value = true
                navigateToDetail()
            }else{
                _loading.value = false
            }
        }

    }

    // LOGIN EN TWITTER

    fun onTwitterLoginSelected(activity: Activity, navigateToDetail: () -> Unit) {

        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO) {
                authService.loginWithTwitter(activity)
            }
            if (result != null) {
                _loading.value = true
                navigateToDetail()
            }else{
                _loading.value = false
            }
        }



    }

    // LOGIN ANONIMO
    fun onAnonymousLoginSelected(navigateToDetail: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true

            val result= withContext(Dispatchers.IO) {
                authService.loginAnonymously()
            }

            if (result != null) {

                navigateToDetail()
            } else{
                _loading.value = false
                // error
            }
            _loading.value = false
        }

    }


}



