package com.example.firebasetest.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasetest.model.Character
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SlideshowViewModel : ViewModel() {

    private val fireDb = Firebase.firestore

    fun submitData(data: Character): LiveData<Boolean>{
        val value = MutableLiveData<Boolean>().apply{
            value = false
        }
        val character = hashMapOf(
            "charName" to data.charName,
            "charClass" to data.charClass,
            "charRace" to data.charRace,
            "charNational" to data.charNational,
            "detail" to data.detail,
            "owner" to data.owner
        )
        fireDb.collection("Characters")
            .add(character)
            .addOnSuccessListener {
                value.value = true
            }
        return  value
    }
}