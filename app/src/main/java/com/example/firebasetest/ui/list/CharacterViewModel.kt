package com.example.firebasetest.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasetest.model.Character
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CharacterViewModel: ViewModel() {

    private val fireDB = Firebase.firestore

    private val value = MutableLiveData<List<Character>>()

    fun getData(): LiveData<List<Character>>{
        val temp = arrayListOf<Character>()
        fireDB.collection("Characters")
            .get()
            .addOnSuccessListener { doc ->
                for(documents in doc){
                    temp.add(documents.toObject())
                    Log.d("getData: ", "document $documents")
                }
                value.value = temp
            }
        return value
    }
}