package com.example.sophosapp


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sophos.userslogin.db_maps
import com.example.sophosapp.Maps_and_cities_Api.maps_Items
import com.example.sophosapp.databinding.FragmentGoogleMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Response


class Google_Maps : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentGoogleMapsBinding
    private lateinit var map: GoogleMap
    lateinit var locat: ArrayList<LatLng>
    lateinit var NamesMap: ArrayList<String>

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGoogleMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //--Click-Listener for Pop-up menu--//
        binding.popup.setOnClickListener { v: View ->
            showMenu(v, R.menu.menu_item)
        }

        //--Fragment transition to menu--//
        binding.backToMenu.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, menu_screen())
                commit()
            }
        }

        createFragment()
    }

    //--fragment Map--//
    private fun createFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    //--Create Map--//
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        enableLocation()
    }

    //--Function to create markers in a map--//
    private fun createMarker() {

        locat = ArrayList()
        NamesMap = ArrayList()

        db_maps.service_map.listcity()
            .enqueue(object : retrofit2.Callback<maps_Items> {
                override fun onResponse(call: Call<maps_Items>, response: Response<maps_Items>) {
                    if (response.isSuccessful) {
                        val Items = response.body()!!.Items
                        for (lat_Lon in Items) {
                            if (lat_Lon.Ciudad == "Chile") {
                                locat.add(LatLng("-${lat_Lon.Latitud}".toDouble(),lat_Lon.Longitud.toDouble()))
                            }else{
                                locat.add(
                                    LatLng(
                                        lat_Lon.Latitud.toDouble(),
                                        lat_Lon.Longitud.toDouble(),
                                    )
                                )
                            }
                            NamesMap.add(lat_Lon.Nombre)
                        }
                        for (count in locat.indices) {
                            map.addMarker(
                                MarkerOptions().position(locat[count]).title(NamesMap[count])
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<maps_Items>, t: Throwable) {
                    call.cancel()
                }

            })

    }

    //--Permissions granted to location--//
    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            layoutInflater.context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    //--Request Location Permission--//
    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                layoutInflater.context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                layoutInflater.context,
                "Go to settings and agree the permissions",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                layoutInflater.context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    //--Permissions to location--//
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    layoutInflater.context,
                    "To activate the location go to settings and accept the permissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    //--Pop-up menu options--//
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(activity, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.Send_Documents_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, Send())
                        commit()
                    }
                R.id.See_Documents_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, See_Documents())
                        commit()
                    }
                R.id.Offices_popup -> requireActivity().supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.container, Google_Maps())
                        commit()
                    }
            }
            true
        })

        popup.show()
    }


}




