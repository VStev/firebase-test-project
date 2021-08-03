package com.example.firebasetest.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasetest.databinding.FragmentSlideshowBinding
import com.example.firebasetest.model.Character
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SlideshowFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        slideshowViewModel = ViewModelProvider(this).get(SlideshowViewModel::class.java)
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        auth.currentUser?.let { setClickListeners(it) }
        return binding.root
    }

    private fun setClickListeners(user: FirebaseUser) {
        binding.btnSubmit.setOnClickListener {
            val charName = binding.tbCharName.text.toString()
            val charClass = binding.spinnerClass.selectedItem.toString()
            val charNational = binding.spinnerNational.selectedItem.toString()
            val charRace = binding.spinnerRace.selectedItem.toString()
            val details = binding.tbDetails.text.toString()
            if (charName.isEmpty()){
                Toast.makeText(requireContext(), "Character Name cannot be empty", Toast.LENGTH_SHORT).show()
            }else{
                val data = if (details.isEmpty()){
                    Character(
                        charName,
                        charRace,
                        charClass,
                        charNational,
                        "Nondescript",
                        user.uid
                    )
                }else{
                    Character(
                        charName,
                        charRace,
                        charClass,
                        charNational,
                        details,
                        user.uid
                    )
                }
                slideshowViewModel.submitData(data).observe(viewLifecycleOwner, {
                    if(it){
                        Toast.makeText(requireContext(), "Success!", Toast.LENGTH_LONG).show()
                        binding.tbCharName.text.clear()
                        binding.tbDetails.text.clear()
                    }else{
                        Toast.makeText(requireContext(), "An Error Occured!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        binding.btnCancelAdd.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}