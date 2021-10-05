package com.example.firebasetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.firebasetest.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private var retries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun updateUI(user: FirebaseUser?){
        clearTextBoxes()
        if (user != null) {
            /*val text = """
                ${user.email}
                status :
                ${user.isEmailVerified}
                display name :
                ${user.displayName}
            """.trimIndent()
            binding.signIn.visibility = View.GONE
            binding.signUp.visibility = View.GONE
            binding.signedIn.visibility = View.VISIBLE
            binding.emailSignedIn.text = text*/
//            binding.nameSignedIn.text = user.displayName
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }else{
            binding.signIn.visibility = View.GONE
            binding.signUp.visibility = View.VISIBLE
//            binding.signedIn.visibility = View.GONE
        }
    }

    private fun setListeners() {
        Log.d("TAG", "LISTENER SET BEGIN!")
        binding.btnHaveAccount.setOnClickListener {
            clearTextBoxes()
            binding.signIn.visibility = View.VISIBLE
            binding.signUp.visibility = View.GONE
            binding.forgotPassword.visibility = View.GONE
        }
        binding.btnDontHaveAccount.setOnClickListener{
            clearTextBoxes()
            binding.signIn.visibility = View.GONE
            binding.signUp.visibility = View.VISIBLE
            binding.btnForgotPassword.visibility = View.GONE
            retries = 0
            binding.forgotPassword.visibility = View.GONE
        }
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
        binding.btnSignUp.setOnClickListener {
            signUp()
        }
        binding.btnForgotPassword.setOnClickListener {
            binding.signIn.visibility = View.GONE
            binding.signUp.visibility = View.GONE
            binding.forgotPassword.visibility = View.VISIBLE
        }
        binding.btnResetPassword.setOnClickListener {
            forgotPassword()
        }
        Log.d("TAG", "LISTENER SET FINISH!")
    }

    private fun clearTextBoxes(){
        Log.d("TAG", "FIRED!")
        binding.tbEmailSignIn.text?.clear()
        binding.tbEmailSignUp.text?.clear()
        binding.tbNameSignUp.text?.clear()
        binding.tbPasswordSignIn.text?.clear()
        binding.tbPasswordSignUp.text?.clear()
        Log.d("TAG", "FINISHED!")
    }

    private fun signIn(){
        //TODO if authentication is not banned then admit else nope
        val email = binding.tbEmailSignIn.text.toString()
        val password = binding.tbPasswordSignIn.text.toString()
        Log.d("TAG", "signIn: $retries")
        if (retries > 2){
            binding.btnForgotPassword.visibility = View.VISIBLE
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signIn:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signIn:failure", it.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    clearTextBoxes()
                    retries += 1
                }
            }
    }

    private fun signUp(){
        val email = binding.tbEmailSignUp.text.toString()
        val password = binding.tbPasswordSignUp.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", it.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun forgotPassword(){
        val email = binding.tbEmailReset.text.toString()
        if (email != ""){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    Toast.makeText(baseContext, "email sent! Check your email for reset link", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(baseContext, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}