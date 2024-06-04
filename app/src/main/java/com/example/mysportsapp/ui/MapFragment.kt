package com.example.mysportsapp.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mysportsapp.R
import com.example.mysportsapp.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMapBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var zoomValue: Float = 16.5f
    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject
    private lateinit var startLocation: Point

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                moveToStartAction()
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(
                    "This feature requires access to your location." +
                            " Open settings to enable location access?"
                )
                    .setPositiveButton("Open settings") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        startActivity(intent)
                    }.setNegativeButton("Cancel", null)
                builder.show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        mapView = binding.mapview
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        moveToStartAction()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    private fun moveToStartAction() {
        if (!checkPermission()) {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                startLocation = Point(it.latitude, it.longitude)
                moveToStartLocation()
                setMarkerInStartLocation()
            }
        }
    }

    private fun moveToStartLocation() {
        binding.mapview.mapWindow.map.move(
            CameraPosition(startLocation, zoomValue, 0.0f, 0.0f)
        )
    }

    private fun setMarkerInStartLocation() {
        val marker = R.drawable.ic_pin
        mapObjectCollection = binding.mapview.mapWindow.map.mapObjects
        placemarkMapObject = mapObjectCollection.addPlacemark()
        with(placemarkMapObject) {
            geometry = startLocation
            setIcon(ImageProvider.fromResource(requireContext(), marker))
            opacity = 0.5f
        }
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}