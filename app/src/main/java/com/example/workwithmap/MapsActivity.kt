package com.example.workwithmap

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.workwithmap.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (permissionPass()) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , 1000 , 3f , this)
        }
    }
    override fun onLocationChanged(location: Location) {
        val driverLocation = LatLng(location.latitude, location.longitude)
        mMap
            .addMarker(
                MarkerOptions()
                    .position(driverLocation)
                    .title("Driver location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.driver))
            )

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(driverLocation, 25f), 2000, null
        )
    }

    private fun permissionPass(): Boolean {
        val permissionsWeNeed = mutableListOf<String>()

        val locationAccessPermission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (locationAccessPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsWeNeed.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsWeNeed.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }


        if (permissionsWeNeed.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, permissionsWeNeed.toTypedArray(), 5
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 5)
            Toast.makeText(this, "یه اتفاقی رخ داد", Toast.LENGTH_SHORT).show()
    }

}