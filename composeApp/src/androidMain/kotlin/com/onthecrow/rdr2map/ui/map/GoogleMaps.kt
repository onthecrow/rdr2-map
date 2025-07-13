package com.onthecrow.rdr2map.ui.map

import android.graphics.Color
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Tile
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.onthecrow.rdr2map.MapData
import com.onthecrow.rdr2map.MarkerIconProvider
import com.onthecrow.rdr2map.TileProviderInternal

@NoLiveLiterals
@Composable
actual fun GoogleMapsComposable(
    modifier: Modifier,
    contentPadding: PaddingValues,
    tileProviderInternal: TileProviderInternal,
    mapData: MutableState<MapData?>,
    onMarkerClick: (locationId: Int) -> Unit,
    onMapClick: () -> Unit,
    markerIconProvider: MarkerIconProvider,
) {
    val markerCache = remember { MarkerCache(markerIconProvider) }

    // todo move camera min/max/position and other defaults outside (in common model)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(38.737798, -9.197043), 4f
        )
    }
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        properties = MapProperties(mapType = MapType.NONE, maxZoomPreference = 7f, minZoomPreference = 2f),
        cameraPositionState = cameraPositionState,
        googleMapOptionsFactory = {
            GoogleMapOptions().run {
                backgroundColor(Color.argb(255, 207, 184, 149))
                this
            }
        },
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,       // Disables the zoom in/out buttons
            compassEnabled = false,          // Disables the compass icon
            myLocationButtonEnabled = false, // Disables the "my location" button
            mapToolbarEnabled = false,       // Disables the map toolbar (directions, open in maps)
            rotationGesturesEnabled = false,  // You might want to keep gestures, set to false to disable
            scrollGesturesEnabled = true,    // You might want to keep gestures, set to false to disable
            tiltGesturesEnabled = false,      // You might want to keep gestures, set to false to disable
            zoomGesturesEnabled = true,       // You might want to keep gestures, set to false to disable
        ),
        contentPadding = contentPadding,
        onMapClick = { onMapClick() },
    ) {
        TileOverlay({ p0, p1, p2 ->
            Tile(
                256,
                256,
                tileProviderInternal.getTile(p2, p0, p1)
            )
        })
        // todo for test purposes only, remove later
        val (width, height) = LocalDensity.current.run {
            38.dp.toPx().toInt() to 51.dp.toPx().toInt()
        }
        mapData.value?.locations?.forEach { location ->
            val markerState =
                rememberSaveable(key = location.id.toString(), saver = MarkerState.Saver) {
                    MarkerState(
                        position = LatLng(
                            location.latitudeDouble,
                            location.longitudeDouble
                        )
                    )
                }
            // todo replace testId for actual marker id
            val markerIcon = remember { markerCache.getMarkerIcon("testId", width, height)!! }
            Marker(
                state = markerState,
                onClick = {
                    onMarkerClick(location.id ?: -1)
                    false
                },
                icon = markerIcon
            )
        }
    }
}