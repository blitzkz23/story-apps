package com.naufaldystd.storyapps.ui.story.location

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationFragment : Fragment() {
	private lateinit var mMap: GoogleMap
	private val locationViewModel: LocationViewModel by viewModels()

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

	/**
	 * GoogleMap callback for fragment
	 */
	private val callback = OnMapReadyCallback { googleMap ->
		mMap = googleMap
		mMap.uiSettings.apply {
			isZoomControlsEnabled = true
			isCompassEnabled = true
			isIndoorLevelPickerEnabled = true
			isMapToolbarEnabled = true
		}

		setMapStyle()
		setMarkerStoryLocation()
	}

	/**
	 * Set map style using custom style
	 *
	 */
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

	// Used to zoom in location in the marker
	private val boundsBuilder = LatLngBounds.builder()

	/**
	 * Set marker based on the lat, lng coordinate of the stories's data
	 *
	 */
	private fun setMarkerStoryLocation() {
		lifecycleScope.launch {
			locationViewModel.getUser().collect { user ->
				locationViewModel.getStoriesWithLocation(user.token).collect { stories ->
					when (stories) {
						is Resource.Success -> {
							stories.data?.forEach { story ->
								if (story.lat != 0.0) {
									val latLng = LatLng(story.lat, story.lon)
									mMap.addMarker(
										MarkerOptions()
											.position(latLng)
											.title(story.name)
											.snippet("Lat: ${story.lat}, Lon: ${story.lon}")
									)
									boundsBuilder.include(latLng)
								}
							}

							val bounds: LatLngBounds = boundsBuilder.build()
							mMap.animateCamera(
								CameraUpdateFactory.newLatLngBounds(
									bounds, resources.displayMetrics.widthPixels,
									resources.displayMetrics.heightPixels,
									300
								)
							)
						}
						is Resource.Error -> {
							Toast.makeText(
								context,
								getString(R.string.cant_load_loc),
								Toast.LENGTH_SHORT
							).show()
						}
						else -> {}
					}
				}
			}
		}
	}
}
