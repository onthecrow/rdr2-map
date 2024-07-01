package com.onthecrow.rdr2map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NoLiveLiterals
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap

@NoLiveLiterals
@Composable
actual fun GoogleMapsComposable(
    modifier: Modifier,
    tileProvider: TileProvider,
    mapData: MutableState<MapData?>,
    infoWindowProvider: CustomInfoWindowProvider,
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
    ) {
    }
}