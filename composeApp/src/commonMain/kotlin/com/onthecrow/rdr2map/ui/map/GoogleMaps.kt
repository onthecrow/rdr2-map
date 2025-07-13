package com.onthecrow.rdr2map.ui.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.onthecrow.rdr2map.MapData
import com.onthecrow.rdr2map.MarkerIconProvider
import com.onthecrow.rdr2map.TileProviderInternal


@Composable
expect fun GoogleMapsComposable(
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    tileProviderInternal: TileProviderInternal,
    mapData: MutableState<MapData?>,
    onMarkerClick: (locationId: Int) -> Unit,
    onMapClick: () -> Unit,
    markerIconProvider: MarkerIconProvider,
)