package com.example.firebasetest.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.adapters.FirestoreAdapter
import com.example.firebasetest.databinding.ItemsBinding
import com.example.firebasetest.model.Character
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

open class CharacterAdapter(query: Query) : FirestoreAdapter<CharacterAdapter.CardViewHolder>(query) {

    inner class CardViewHolder(private val items: ItemsBinding) : RecyclerView.ViewHolder(items.root) {
        fun bind(data: DocumentSnapshot) {
            val char = data.toObject<Character>()
            Log.d("bind:", "$char")
            items.txtName.text = char?.charName
            items.txtClass.text = char?.charClass
            items.txtRace.text = char?.charRace
            items.txtOwner.text = char?.owner
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(ItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }
}