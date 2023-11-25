package com.example.protecthemapp.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.protecthemapp.R
import com.example.protecthemapp.database.Guardian
import com.example.protecthemapp.database.GuardianDatabase
import com.example.protecthemapp.databinding.FragmentDashBoardBinding
import kotlinx.coroutines.*

class DashBoardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var Latitude: String = ""
    private var Longitude: String = ""

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    companion object {
        private const val PERMISSION_SEND_SMS = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnContacts2.setOnClickListener {
            findNavController().navigate(R.id.action_dashBoardFragment_to_guardianInfo)
        }

        binding.btnLocation2.setOnClickListener {
            findNavController().navigate(R.id.action_dashBoardFragment_to_mapsActivity)
        }
        binding.btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_dashBoardFragment_to_homeFragment)
        }

        binding.emerButton.setOnClickListener {
            getLocation()
            if (Longitude.isNullOrBlank() || Longitude.isNullOrEmpty()) {
                Toast.makeText(
                    requireActivity(),
                    "Click on the Location button to get your current location",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.SEND_SMS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.SEND_SMS),
                        PERMISSION_SEND_SMS
                    )
                } else {
                    uiScope.launch {
                        withContext(Dispatchers.IO) {
                            emergencyFun()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_SEND_SMS) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    emergencyFun()
                }
            }
        }
    }

    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                Latitude = location.latitude.toString()
                Longitude = location.longitude.toString()
            }
        }
    }

    private fun emergencyFun() {
        val db = Room.databaseBuilder(
            requireActivity(),
            GuardianDatabase::class.java,
            "GuardianDB"
        ).build()
        val emailList: List<Guardian> = db.guardianDatabaseDao().getEmail()

        var maillist = ""
        val subject = "From Protecthem App"
        val text = resources.getString(R.string.problem)
        val text1 = text.plus("https://www.google.com/maps/search/?api=1&query=$Latitude,$Longitude")

        emailList.forEach { guardian ->
            maillist = emailList.joinToString(separator = ",") { it.guardianEmail }
        }

        emailList.forEach { guardian ->
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(guardian.guardianPhoneNo, null, text1, null, null)
        }

        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.type = "message/rfc822"
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(maillist))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, text1)
        startActivity(Intent.createChooser(shareIntent, "Send mail using.."))
    }
}




