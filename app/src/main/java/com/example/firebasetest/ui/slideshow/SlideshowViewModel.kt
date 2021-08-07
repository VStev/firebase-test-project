package com.example.firebasetest.ui.slideshow

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasetest.model.Character
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File

class SlideshowViewModel : ViewModel() {

    private val fireDb = Firebase.firestore
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    fun submitData(data: Character, image: Uri): LiveData<Boolean>{
        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }
        val uploadTask = storageRef.child("images/${data.picture}").putFile(image, metadata)
        val value = MutableLiveData<Boolean>().apply{
            value = false
        }
        val character = hashMapOf(
            "charName" to data.charName,
            "charClass" to data.charClass,
            "charRace" to data.charRace,
            "charNational" to data.charNational,
            "detail" to data.detail,
            "owner" to data.owner,
            "picture" to data.picture
        )
        fireDb.collection("Characters")
            .add(character)
            .addOnSuccessListener {
                value.value = true
            }
        uploadTask
            .addOnSuccessListener{
                Log.d("ImageUpload", "success")
            }
            .addOnFailureListener{
                Log.d("ImageUpload", "fail")
            }
        return  value
    }
}