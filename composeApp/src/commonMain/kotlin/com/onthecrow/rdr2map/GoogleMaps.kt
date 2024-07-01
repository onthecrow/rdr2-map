package com.onthecrow.rdr2map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier


@Composable
expect fun GoogleMapsComposable(
    modifier: Modifier = Modifier,
    tileProvider: TileProvider,
    mapData: MutableState<MapData?>,
    infoWindowProvider: CustomInfoWindowProvider,
)