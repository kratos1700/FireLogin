package com.example.firelogin.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firelogin.data.AuthService
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

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            _isLoggedIn.value = authService.isUserLoggedIn()
        }
    }

    fun login(email: String, password: String, navegateToDetail: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true

            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                authService.login(email, password)
            }

            if (result != null) {
                _loading.value = false
                _isLoggedIn.value = true
                navegateToDetail()

            }
            _loading.value = false


        }
    }

    fun loginWithPhone(
        phoneNumber: String,
        activity: Activity,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSend: () -> Unit,

    ) {

        viewModelScope.launch {
            _loading.value = true

            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                    Log.d("TAG", "onVerificationCompleted:$credential")

                    //signInWithPhoneAuthCredential(credential)
                    onVerificationCompleted()


                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w("TAG", "onVerificationFailed", e)

                    _loading.value = false
                    onVerificationFailed(e.message.orEmpty())

                    // Show a message and update the UI
                    // ...
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    _loading.value = false
                    onCodeSend()
                }
            }

            val result = withContext(Dispatchers.IO) {
                authService.loginWithPhone(phoneNumber, activity, callback)
            }


            _loading.value = false
        }


    }


}