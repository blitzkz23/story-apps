package com.naufaldystd.storyapps.ui.story.location

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MapStyleOptions
import com.naufaldystd.storyapps.R

class LocationFragment : Fragment() {
	private lateinit var mMap: GoogleMap
	private lateinit var fusedLocationClient: FusedLocationProviderClient

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_location, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val mapFragment =
			childFragmentManager.findFragmentById(R.id.view_map) as SupportMapFragment?
		mapFragment?.getMapAsync(callback)
	}

	private val callback = OnMapReadyCallback { googleMap ->
		mMap = googleMap
		mMap.uiSettings.apply {
			isZoomControlsEnabled = true
			isCompassEnabled = true
			isIndoorLevelPickerEnabled = true
			isMapToolbarEnabled = true
		}
		setMapStyle()
	}

	private fun setMapStyle() {
		try {
			val success = mMap.setMapStyle(context?.let {
				MapStyleOptions.loadRawResourceStyle(
					it,
					R.raw.map_style
				)
			})
			if (!success) {
				Log.e(TAG, getString(R.string.error_parse_style))
			}
		} catch (e: Resources.NotFoundException) {
			Log.e(TAG, getString(R.string.error_resources), e)
		}
	}

}