package com.example.firelogin.ui.login

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firelogin.data.AuthService
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService): ViewModel(){

    private val _loading:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // 13:23



    fun login(email: String, password: String, navegateToDetail: () -> Unit){
       viewModelScope.launch {
              _loading.value = true

           val result: FirebaseUser?  = withContext(Dispatchers.IO) {
               authService.login(email, password)
           }

           if (result != null) {
               _loading.value = false
               navegateToDetail()

           }else{
               _loading.value = false
           }



       }
    }


}