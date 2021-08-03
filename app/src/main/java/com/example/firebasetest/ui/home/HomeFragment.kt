package com.example.firebasetest.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasetest.MainActivity
import com.example.firebasetest.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        auth.currentUser?.let { setViews(it) }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val user = auth.currentUser
        val verify = user?.isEmailVerified
        if (verify == true){
            val text =
                """
                You are logged in as
                ${user.email}
                Your current email status is verified
            """.trimIndent()
            binding.emailSignedIn.text = text
            binding.btnVerifyRequest.visibility = View.GONE
        }else{
            val text =
                """
                You are logged in as
                ${user?.email}
                Your current email status is not yet verified
            """.trimIndent()
            binding.emailSignedIn.text = text
        }
    }

    private fun setViews(user: FirebaseUser) {
        val verify = user.isEmailVerified
        if (verify){
            val text =
            """
                You are logged in as
                ${user.email}
                Your current email status is verified
            """.trimIndent()
            binding.emailSignedIn.text = text
            binding.btnVerifyRequest.visibility = View.GONE
        }else{
            val text =
                """
                You are logged in as
                ${user.email}
                Your current email status is not yet verified
            """.trimIndent()
            binding.emailSignedIn.text = text
        }
        binding.btnSignOut.setOnClickListener {
            signOut()
            startActivity(Intent(activity, MainActivity::class.java))
        }
        binding.btnVerifyRequest.setOnClickListener {
            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity?.applicationContext, "Verification Email Sent", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signOut(){
        auth.signOut()
    }
}