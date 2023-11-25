package com.example.protecthemapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.protecthemapp.Utils.PermissionUtils
import com.example.protecthemapp.R;
import android.net.Uri
import androidx.appcompat.app.AlertDialog





class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap

    private var googleApiClient: GoogleApiClient? = null
    private val REQUESTLOCATION = 199

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun enableLoc() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                    googleApiClient?.connect()
                }
            })
            .addOnConnectionFailedListener {
            }.build()
        googleApiClient?.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        this@MapsActivity,
                        REQUESTLOCATION
                    )
                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
        setUpLocationListener()
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUESTLOCATION -> when (resultCode) {
                Activity.RESULT_OK -> Log.d("abc","OK")
                Activity.RESULT_CANCELED -> Log.d("abc","CANCEL")
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        enableLoc()
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    MapsActivity.LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(20000).setFastestInterval(20000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        map.clear()
                        map.isMyLocationEnabled = true
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        placeMarkerOnMap(currentLatLng)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                        // Add other hotspots to the map
                        val hotspot1 = LatLng(-26.191245495359915, 28.069704626013458) // Example coordinates for a hotspot
                        placeMarkerOnMap(hotspot1, "Shelter", "Little Saints of Bethany")

                        val hotspot2 = LatLng(-26.148024397978073, 27.880179143292846) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot2, "Shelter", "Champions for Change")

                        val hotspot3 = LatLng(-26.16340413186307, 28.069077743292848) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot3, "Shelter", "Frida Hartley Shelter")

                        val hotspot4 = LatLng(-26.094285244390857, 28.102668864585233) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot4, "Shelter", "Agisanang Domestic Abuse Prevention & Training - A.D.A.P.T.")

                        val hotspot5 = LatLng(-26.08692775944639, 28.050356046610954) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot5, "Shelter", "WOMEN & MEN AGAINST CHILD ABUSE")

                        val hotspot6 = LatLng(-26.106660351002354, 28.050356046610954) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot6, "Shelter", "TEARS Foundation")

                        val hotspot7 = LatLng(-26.178201585345377, 28.036054514517808) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot7, "Shelter", "Forum for the Empowerment of Women")

                        val hotspot8 = LatLng(-26.156083521344534, 27.972775192243752) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot8, "Shelter", "Women of Vision")

                        val hotspot9 = LatLng(-26.100200246061867, 28.073226974422393) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot9, "Shelter", "HELP A CHILD SOUTH AFRICA")

                        val hotspot10 = LatLng(-26.059491130225183, 28.060203426724673) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot10, "Shelter", "Black Women Organisation South Africa")

                        val hotspot11 = LatLng(-26.054826284890915, 28.05732250188171) // Example coordinates for another hotspot
                        placeMarkerOnMap(hotspot11, "Shelter", "Hope SA Foundation")
                    }
                }
            },
            Looper.myLooper()
        )
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null) {
            // Show a dialog or custom view with marker information (name, description, etc.)
            val dialog = AlertDialog.Builder(this)
                .setTitle(marker.title)
                .setMessage(marker.snippet)
                .setPositiveButton("Navigate") { _, _ ->
                    // Handle navigation button click
                    navigateToMarker(marker)
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }
        return true
    }
    private fun navigateToMarker(marker: Marker) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=${marker.position.latitude},${marker.position.longitude}")
        )
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
//(Add a map to your Android app (Kotlin) (no date))



    private fun placeMarkerOnMap(location: LatLng, name: String, description: String) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(name)
            .snippet(description)

        map.addMarker(markerOptions)
    }


    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        map.addMarker(markerOptions)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MapsActivity.LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            enableLoc()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
//Add a map to your Android app (Kotlin) (no date) Google for Developers.
// Available at: https://developers.google.com/codelabs/maps-platform/maps-platform-101-android
// (Accessed: November 23, 2023).