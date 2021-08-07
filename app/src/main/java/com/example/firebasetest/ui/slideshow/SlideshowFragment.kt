package com.example.firebasetest.ui.slideshow

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var imageUri: Uri

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
            val image = binding.txtImagePath.text.toString()
            if (charName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Character Name cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (image == "No Image Selected"){
            Toast.makeText(
                requireContext(),
                "Please add an image",
                Toast.LENGTH_SHORT
            ).show()
            }else{
                val picturePath = user.uid.substring(3, 6) + charName.substring(0, 3) + image
                val data = if (details.isEmpty()){
                    Character(
                        charName,
                        charRace,
                        charClass,
                        charNational,
                        "Nondescript",
                        user.uid,
                        picturePath
                    )
                }else{
                    Character(
                        charName,
                        charRace,
                        charClass,
                        charNational,
                        details,
                        user.uid,
                        picturePath
                    )
                }
                slideshowViewModel.submitData(data, imageUri).observe(viewLifecycleOwner, {
                    if(it){
                        Toast.makeText(requireContext(), "Success!", Toast.LENGTH_LONG).show()
                        binding.tbCharName.text.clear()
                        binding.tbDetails.text.clear()
                    }else{
                        Toast.makeText(requireContext(), "An Error Occurred!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        binding.btnCancelAdd.setOnClickListener {

        }
        binding.imageButton.setOnClickListener{
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your profile picture")

            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item] == "Take Photo") {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                } else if (options[item] == "Choose from Gallery") {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    if (selectedImage != null) {
                        imageUri = selectedImage
                    }
                    if (selectedImage != null) {
                        binding.txtImagePath.text = selectedImage.lastPathSegment
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}