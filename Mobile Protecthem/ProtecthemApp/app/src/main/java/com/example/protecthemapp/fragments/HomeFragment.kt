package com.example.protecthemapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.protecthemapp.R
import com.example.protecthemapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnJournal = view.findViewById<ImageView>(R.id.btnJournal)
        val btnServices = view.findViewById<ImageView>(R.id.btnServices)
        val btnSettings = view.findViewById<ImageView>(R.id.btnSettings)
        val btnEvidence = view.findViewById<ImageView>(R.id.btnEvidence)
        val btnContacts = view.findViewById<ImageView>(R.id.btnContacts)
        val btnLocation = view.findViewById<ImageView>(R.id.btnLocation)
        val btnHelp = view.findViewById<ImageView>(R.id.btnHelp)
        val btnSupport = view.findViewById<ImageView>(R.id.btnSupport)


        btnJournal.setOnClickListener {
            navigateToJournalFragment()
        }
        btnServices.setOnClickListener {
            navigateToServicesFragment()
        }
        btnSettings.setOnClickListener {
            navigateToSettingsFragment()
        }
        btnEvidence.setOnClickListener {
            navigateToImageFragment()
        }
        btnContacts.setOnClickListener {
            navigateToAddGuardian()
        }
        btnLocation.setOnClickListener {
            navigateToMapsActivity()
        }
        btnHelp.setOnClickListener {
            navigateToDashBoardFragment()
        }
        btnSupport.setOnClickListener {
            navigateToSupportActivity()
        }

    }

    private fun navigateToJournalFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_journalActivity)
    }
    private fun navigateToServicesFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_servicesFragment)
    }
    private fun navigateToSettingsFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_settingsFragment)
    }
    private fun navigateToImageFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_ImageActivity)
    }
    private fun navigateToAddGuardian() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_guardianInfoFragment)
    }
    private fun navigateToMapsActivity() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_mapsActivity)
    }
    private fun navigateToDashBoardFragment() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_dashBoardFragment)
    }
    private fun navigateToSupportActivity() {
        val navController = Navigation.findNavController(requireView())
        navController.navigate(R.id.action_homeFragment_to_supportActivity)
    }

}