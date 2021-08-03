package com.example.firebasetest.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

class GalleryViewModel : ViewModel() {

    private val _displayName = MutableLiveData<String>().apply {
        value = "Undefined"
    }
    val displayName: LiveData<String> = _displayName

    fun updateUser(user: FirebaseUser, content: String): LiveData<Boolean>{
        val value = MutableLiveData<Boolean>().apply{
            value = false
        }
        val profileUpdates = userProfileChangeRequest {
            displayName = content
        }
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                value.value = task.isSuccessful
                _displayName.value = content
            }
        return value
    }

    fun requestVerification(user: FirebaseUser): LiveData<Boolean>{
        val value = MutableLiveData<Boolean>().apply{
            value = false
        }
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    value.value = true
                }
            }
        return value
    }
}