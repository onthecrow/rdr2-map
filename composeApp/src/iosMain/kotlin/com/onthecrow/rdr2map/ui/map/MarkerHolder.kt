package com.onthecrow.rdr2map.ui.map

import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMarker
import com.onthecrow.rdr2map.Location
import com.onthecrow.rdr2map.MarkerIconProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationCoordinate2DMake
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
class MarkerHolder(
    private val locations: List<Location>,
    private val markerIconProvider: MarkerIconProvider,
    private val markerCache: MarkerCache = MarkerCache(markerIconProvider),
) {
    private val mutex = Mutex()
    private var markers: List<GMSMarker> = emptyList()
    private var _mapView: WeakReference<GMSMapView>? = null
    var mapView: GMSMapView? = null
        set(value) {
            if (field != null || value == null || field == value) return
            _mapView = WeakReference(value)
            initMarkers()
        }

    init {
        initMarkers()
    }

    private fun initMarkers() {
        MainScope().launch(Dispatchers.IO) {
            mutex.withLock {
                val posters = locations
                markers = posters.map { poster ->
                    val image = async {
                       markerCache.getMarker("testId")
                    }.await()
                    val coordinate = CLLocationCoordinate2DMake(poster.latitudeDouble, poster.longitudeDouble)
                    withContext(Dispatchers.Main) {
                        GMSMarker.markerWithPosition(coordinate).apply {
                            icon = image
                            opacity = 0.5f
                            userData = poster.id
                            title = poster.title
                            map = _mapView?.get()
                        }
                    }
                }
            }
        }
    }
}