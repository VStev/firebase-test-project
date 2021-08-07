package com.example.firebasetest.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.R
import com.example.firebasetest.databinding.FragmentCharacterListBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CharacterListFragment : Fragment() {

//    private lateinit var rview: RecyclerView
//    private lateinit var viewModel: CharacterViewModel
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var fireDB: FirebaseFirestore
    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        characterAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        characterAdapter.stopListening()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
//        rview = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseFirestore.setLoggingEnabled(true)
        val rv: RecyclerView = view.findViewById(R.id.recyclerView)
        fireDB = Firebase.firestore
        val q = fireDB.collection("Characters")
//            .orderBy("charName", Query.Direction.ASCENDING)
        val reference = Firebase.storage.reference
        characterAdapter = object : CharacterAdapter(q, reference){
            override fun onDataChanged() {
                super.onDataChanged()
                if (itemCount == 0){
                    binding.recyclerView.visibility = View.GONE
                    binding.imageBase.visibility = View.VISIBLE
                }else{
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.imageBase.visibility = View.GONE
                }
            }
            override fun onError(e: FirebaseFirestoreException) {
                Snackbar.make(binding.root, "Error connecting to database", Snackbar.LENGTH_LONG).show()
            }
        }
        with (rv){
            layoutManager = LinearLayoutManager(context)
            adapter = characterAdapter
        }
//        setLayout()
    }

    /*private fun setLayout(){
        *//*val data = viewModel.getData()
        Log.d("setLayout: ", "get")
        data.observe(viewLifecycleOwner, {
            Log.d("setLayout: ", "$it")
            if (it != null){
                adapter.setData(it)
                with (rview){
                    adapter = adapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
        })*//*
        fireDB = Firebase.firestore
        val q = fireDB.collection("Characters")
//            .orderBy("charName", Query.Direction.ASCENDING)
        adapter = object : CharacterAdapter(q){
            override fun onError(e: FirebaseFirestoreException) {
                Snackbar.make(binding.root, "Error connecting to database", Snackbar.LENGTH_LONG).show()
            }
        }
        with (rview){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapter
        }
    }*/

}