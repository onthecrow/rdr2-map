package com.onthecrow.rdr2map.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition.Companion.cameraWithLatitude
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewOptions
import cocoapods.GoogleMaps.kGMSTypeNone
import com.onthecrow.rdr2map.MapData
import com.onthecrow.rdr2map.MarkerIconProvider
import com.onthecrow.rdr2map.TileProvider
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIColor


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMapsComposable(
    modifier: Modifier,
    tileProvider: TileProvider,
    mapData: MutableState<MapData?>,
    onMarkerClick: (locationId: Int) -> Unit,
    onMapClick: () -> Unit,
    markerIconProvider: MarkerIconProvider,
) {
    val mapDelegate = remember(onMarkerClick, onMapClick) {
        MapDelegate(onMarkerClick, onMapClick)
    }
    val tileLayer = remember(tileProvider) { TileLayer(tileProvider) }
    val markerHolder = remember(mapData.value?.locations, markerIconProvider) {
        MarkerHolder(mapData.value?.locations ?: emptyList(), markerIconProvider)
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val options = GMSMapViewOptions()
            // todo pass camera params inside map entity
            options.camera = cameraWithLatitude(38.737798, -9.197043, zoom = 4f)
            options.backgroundColor = UIColor(red = .8118, green = .7215, blue = .5843, alpha = 1.0)
            GMSMapView(options = options).apply {
                setMinZoom(minZoom = 2f, maxZoom = 5.7f)
                mapType = kGMSTypeNone
                myLocationEnabled = false
                settings.setMyLocationButton(false)
                settings.setScrollGestures(true)
                settings.setZoomGestures(true)
                settings.setCompassButton(false)
            }
        },
        update = { mapView ->
            markerHolder.mapView = mapView
            mapView.delegate = mapDelegate
            tileLayer.map = mapView
        },
    )
}