package com.example.firebasetest.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasetest.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GalleryFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth
        auth.currentUser?.let { setViews(it) }
        return root
    }

    private fun setViews(user: FirebaseUser) {
        val verify = user.isEmailVerified
        if (!verify) {
            binding.unverified.visibility = View.VISIBLE
            binding.userProfile.visibility = View.GONE
            binding.editUser.visibility = View.GONE
        }
        binding.btnVerifyProfile.setOnClickListener {
            requestVerification(user)
        }
        binding.btnCancel.setOnClickListener {
            binding.unverified.visibility = View.GONE
            binding.userProfile.visibility = View.VISIBLE
            binding.editUser.visibility = View.GONE
        }
        binding.btnUpdate.setOnClickListener {
            val text = binding.editTextTextPersonName.text.toString()
            updateUser(user, text)
        }
        binding.btnEditProfile.setOnClickListener {
            binding.unverified.visibility = View.GONE
            binding.userProfile.visibility = View.GONE
            binding.editUser.visibility = View.VISIBLE
        }
        binding.txtUniqueId.text = user.uid
        binding.txtEmail.text = user.email
        galleryViewModel.displayName.observe(viewLifecycleOwner, {
            binding.txtDisplayName.text = it
        })
    }

    private fun updateUser(user: FirebaseUser, text: String) {
        val observe = galleryViewModel.updateUser(user, text)
        observe.observe(viewLifecycleOwner, {
            if (it){
                binding.unverified.visibility = View.GONE
                binding.userProfile.visibility = View.VISIBLE
                binding.editUser.visibility = View.GONE
                Toast.makeText(activity?.applicationContext, "Profile Edited Successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity?.applicationContext, "Profile Fails to Edit", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun requestVerification(user: FirebaseUser) {
        val observer = galleryViewModel.requestVerification(user)
        observer.observe(viewLifecycleOwner, {
            if (it){
                Toast.makeText(activity?.applicationContext, "Verification Email Sent", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity?.applicationContext, "Failed to Send Verification Email Please Check Your Internet", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}