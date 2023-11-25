package com.example.protecthemapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.protecthemapp.R
import com.example.protecthemapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        binding.textViewSignIn.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val verifyPass = binding.verifyPassEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()) {
                if (pass == verifyPass) {
                    if (isEmailValid(email)) {
                        if (isPasswordValid(pass)) {
                            registerUser(email, pass)
                        } else {
                            Toast.makeText(
                                context,
                                "Password must be at least 6 characters long",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        // You can implement a more sophisticated email validation if needed
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    private fun registerUser(email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navController.navigate(R.id.action_signUpFragment_to_DashBoardFragment)
            } else {
                Toast.makeText(
                    context,
                    "Registration failed: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}
