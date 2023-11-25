package com.example.protecthemapp.guardiandetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protecthemapp.R
import com.example.protecthemapp.databinding.FragmentGuardianInfoBinding
import com.google.android.material.snackbar.Snackbar
import android.widget.Button
import androidx.navigation.fragment.findNavController


class GuardianInfo : Fragment() {

    private lateinit var binding: FragmentGuardianInfoBinding
    private lateinit var model: GuardianInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guardian_info, container, false
        )

        // Get the view model
        model = ViewModelProviders.of(this).get(GuardianInfoViewModel::class.java)

        // Specify layout for recycler view
        val linearLayoutManager = LinearLayoutManager(
            activity!!, RecyclerView.VERTICAL, false
        )
        binding.guardianList.layoutManager = linearLayoutManager

        // Observe the model
        model.allGuardians.observe(this, Observer { guardians ->
            // Data bind the recycler view
            binding.guardianList.adapter = GuardianAdapter(guardians)
        })

        binding.addGuardian.setOnClickListener { openAddGuardian() }

        model.showSnackBarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_LONG
                ).show()
                model.doneShowingSnackbar()
            }
        })

        setHasOptionsMenu(true)

        // Inflate the layout
        val rootView = binding.root

        // Initialize and set click listener for the delete button
        val deleteButton: Button = rootView.findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            model.onClear()
        }

        return rootView
    }

    // Additional methods if needed

    private fun openAddGuardian() {
        findNavController().navigate(R.id.action_guardianInfo_to_addGuardian)
    }
}










