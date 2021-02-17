package com.barto.simplecrud

import android.Manifest

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.barto.simplecrud.databinding.ActivityMapsBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //firebase
    val user = FirebaseAuth.getInstance().currentUser
    val database = FirebaseDatabase.getInstance()
    val refUser = database.getReference("public/")
    val productRef = refUser.child("shops")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapsBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.rvMaps.layoutManager = LinearLayoutManager(this)
        binding.rvMaps.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //firebase
        val list = arrayListOf<Shop>()
        val adapter = ShopAdapter(this, list, productRef)
        binding.rvMaps.adapter = adapter

        //test
        val sydney = LatLng(-34.0, 151.0)
        val marker = MarkerOptions()
                .position(sydney)
                .title("test")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var mChildEventListener: ChildEventListener
        val mUsers = database.getReference("public/").child("shops")
        val geoClient = LocationServices.getGeofencingClient(this)
        var id = 0

        bt_place.setOnClickListener{
            //uses permissions declared in onMapReady()
            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener {
                    Log.i("location", "Location: ${it.latitude}, ${it.longitude}")
                    val latlng = LatLng(it.latitude, it.longitude)
                    val lat = it.latitude
                    val long = it.longitude
                    val marker = MarkerOptions()
                        .position(latlng)
                        .title(et_shopName.text.toString())
                            .snippet(et_shopDesc.toString())

                    val geofenceRadius = et_geoRadius.text.toString().toFloat()
                    val shopName = et_shopName.text.toString()
                    mMap.addMarker(marker)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))

                    val geo = Geofence.Builder()
                        .setRequestId("Geo${id++}, Shop ${shopName}")
                        .setCircularRegion(it.latitude, it.longitude, geofenceRadius)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or
                                            Geofence.GEOFENCE_TRANSITION_EXIT)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .build()

                    val geoRequest = GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER or
                                            Geofence.GEOFENCE_TRANSITION_EXIT)
                        .addGeofence(geo)
                        .build()

                    val geoPendingIntent = PendingIntent.getBroadcast(
                            this,
                            id,
                            Intent(this, GeoReceiver::class.java),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    //uses permissions declared in onMapReady()
                    geoClient.addGeofences(geoRequest, geoPendingIntent)

                    CoroutineScope(Dispatchers.IO).launch {
                        if(user != null){
                            val name = binding.etShopName.text.toString().trim()
                            val desc = binding.etShopDesc.text.toString().trim()
                            val latitude = lat
                            val longitude = long
                            val radius = binding.etGeoRadius.text.toString().trim().toFloat()
                            val shop = Shop(name, desc, latitude, longitude, radius)
                            if (name != null) {
                                productRef.child(name).setValue(shop)
                            }
                        }
                    }
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
        mMap = googleMap
        val perms = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
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
            requestPermissions(perms, 0)
        }
        mMap.isMyLocationEnabled = true

        val mUsers = database.getReference("public/").child("shops")
        mUsers.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot ) {
                for (s in dataSnapshot.getChildren()){
                    val shop = s.getValue(Shop::class.java)
                    val pos = LatLng(shop!!.latitude, shop!!.longitude)
                    mMap.addMarker(MarkerOptions().position(pos).title(shop.name).snippet(shop.desc))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
            }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("read-error", error.details)
            }
        })
    }
}