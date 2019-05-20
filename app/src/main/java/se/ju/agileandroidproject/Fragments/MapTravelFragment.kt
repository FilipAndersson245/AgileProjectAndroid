package se.ju.agileandroidproject.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import se.ju.agileandroidproject.R


class MapTravelFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("EH", "DOING STUFF 2")
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)

        var cameraPosition = CameraPosition.Builder().target(sydney).zoom(14.0f).build()
        var cameraUpdate = newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);

//        mMap.setMyLocationEnabled(true);

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map_travel, container, false)

        val mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
//        val a = childFragmentManager.findFragmentById(R.id.aa)
        mapFragment.getMapAsync(this)
//
//        Log.d("EH", a.toString())

        return rootView
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapFragment = childFragmentManager.findFragmentById(se.ju.agileandroidproject.R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync {
//            Log.d("EH", "DOING STUFF")
//            it.addMarker(
//                MarkerOptions()
//                    .position(LatLng(1.0,2.0)).title("TutorialsPoint"))
//        }
//
//    }

    companion object {
        fun newInstance(): MapTravelFragment {
            return MapTravelFragment()
        }
    }
}
