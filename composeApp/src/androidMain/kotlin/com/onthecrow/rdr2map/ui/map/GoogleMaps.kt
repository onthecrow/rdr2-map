package com.onthecrow.rdr2map.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap
import com.onthecrow.rdr2map.MapData
import com.onthecrow.rdr2map.MarkerIconProvider
import com.onthecrow.rdr2map.TileProvider

@NoLiveLiterals
@Composable
actual fun GoogleMapsComposable(
    modifier: Modifier,
    tileProvider: TileProvider,
    mapData: MutableState<MapData?>,
    onMarkerClick: (locationId: Int) -> Unit,
    onMapClick: () -> Unit,
    markerIconProvider: MarkerIconProvider,
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
    ) {
    }
}