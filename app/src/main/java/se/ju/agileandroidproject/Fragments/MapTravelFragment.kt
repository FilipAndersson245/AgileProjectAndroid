package se.ju.agileandroidproject.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import com.google.android.gms.maps.*
import com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition
import com.google.android.gms.maps.model.*
import kotlinx.serialization.ImplicitReflectionSerializer
import se.ju.agileandroidproject.APIHandler
import se.ju.agileandroidproject.Activities.Main2Activity
import se.ju.agileandroidproject.GPSHandler
import se.ju.agileandroidproject.R


class MapTravelFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment;
    private lateinit var mMap: GoogleMap

    @SuppressLint("ResourceType")
    @ImplicitReflectionSerializer
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("EH", "DOING STUFF 2")
        mMap = googleMap

        mMap.uiSettings.setZoomGesturesEnabled(true)
        mMap.uiSettings.setScrollGesturesEnabled(false)
        mMap.uiSettings.setZoomControlsEnabled(true)

        val zoomControls = mapFragment.view!!.findViewById<View>(0x1)

        if (zoomControls != null && zoomControls.getLayoutParams() is RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            val params = zoomControls.getLayoutParams() as RelativeLayout.LayoutParams

            // Align it to - parent top|left
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            // Update margins, set to 10dp
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10F, getResources().getDisplayMetrics()).toInt()
            params.setMargins(margin, margin, margin, margin);
        }

        var location = when(GPSHandler.locationExists){
            true -> LatLng(GPSHandler.currentLocation.latitude, GPSHandler.currentLocation.longitude)
            else -> LatLng(0.0, 0.0)
        }

        var zoom = 14.0f
        var tilt = 0f

        view?.post {
            var cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).tilt(tilt).build()
            var cameraUpdate = newCameraPosition(cameraPosition)
            mMap.moveCamera(cameraUpdate)
        }

        val carBitmap = BitmapDescriptorFactory.fromResource(R.drawable.car)

        Thread {
            while(APIHandler.isTraveling) {
                view?.post {
                    if(GPSHandler.locationExists)
                    {
                        location = LatLng(GPSHandler.currentLocation.latitude, GPSHandler.currentLocation.longitude)
                    }
                    zoom = mMap.cameraPosition.zoom
                    tilt = mMap.cameraPosition.tilt
                    mMap.clear()
                    for(gantry in GPSHandler.closeGantries) {
                        mMap.addMarker(MarkerOptions()
                            .position(LatLng(gantry.latitude.toDouble(), gantry.longitude.toDouble()))
                            .title("${gantry.price} kr"))
                    }

                    mMap.addMarker(MarkerOptions()
                        .position(location)
                        .icon(carBitmap))

                    var cameraPosition = CameraPosition.Builder().target(location).zoom(zoom).tilt(tilt).build()
                    var cameraUpdate = newCameraPosition(cameraPosition)
                    mMap.moveCamera(cameraUpdate)
                }
                Thread.sleep(1000)
            }
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_map_travel, container, false)

        mapFragment =  childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


        return rootView
    }

    @ImplicitReflectionSerializer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stopTravelButton = view!!.findViewById<Button>(R.id.buttonMapStopTravel)

        stopTravelButton.setOnClickListener {
            APIHandler.isTraveling = false
            (activity as Main2Activity).stopBackgroundService()
            (parentFragment as MasterTravelFragment).switchFragment(StartTravelFragment.newInstance())
        }

        val showMapButton = view!!.findViewById<Button>(R.id.buttonMapHideMap)

        showMapButton.setOnClickListener {
            (parentFragment as MasterTravelFragment).switchFragment(DefaultTravelFragment.newInstance())
        }
    }


    companion object {
        fun newInstance(): MapTravelFragment {
            return MapTravelFragment()
        }
    }
}
