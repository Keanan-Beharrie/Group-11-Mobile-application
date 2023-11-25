package com.example.protecthemapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.protecthemapp.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Find the Button for navigating to the SettingsFragment
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            navigateToSignInFragment()
        }

        return view
    }

    private fun navigateToSignInFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_settingsFragment_to_signInFragment)
    }


}

