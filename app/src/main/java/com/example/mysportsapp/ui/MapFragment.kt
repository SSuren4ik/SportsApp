package com.example.mysportsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysportsapp.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d("MAP", "onCreateView1")
        MapKitFactory.setApiKey("fc94633a-5a13-48c5-b5e7-abb0c5a76ee3")
        Log.d("MAP", "onCreateView2")
        MapKitFactory.initialize(requireContext())
        Log.d("MAP", "onCreateView3")
        binding = FragmentMapBinding.inflate(inflater)
        mapView = binding.mapview
        Log.d("MAP", "onCreateView4")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
        Log.d("MAP", "onStart")
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        Log.d("MAP", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MAP", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MAP", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MAP", "onDetach")
    }

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}