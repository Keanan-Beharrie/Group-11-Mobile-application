package com.example.protecthemapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.protecthemapp.databinding.ActivityImageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun registerClickEvents() {
        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }

        binding.showAllBtn.setOnClickListener {
            startActivity(Intent(this, ImagesActivity::class.java))
        }

        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageView.setImageURI(it)
    }


    private fun initVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun uploadImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful){
                                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }

                            // Optionally, reset the image view to a default image
                            binding.imageView.setImageResource(R.drawable.imageupload)
                        }
                    }//(Get started with cloud firestore (no date))
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    // Optionally, reset the image view to a default image
                    binding.imageView.setImageResource(R.drawable.imageupload)
                }
            }
        }
    }

}
//Get started with cloud firestore (no date) Firebase.
// Available at: https://firebase.google.com/docs/firestore/quickstart
// (Accessed: November 23, 2023).