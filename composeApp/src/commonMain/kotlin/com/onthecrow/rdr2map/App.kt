package com.onthecrow.rdr2map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.onthecrow.rdr2map.ui.map.GoogleMapsComposable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import rdr2_map.composeapp.generated.resources.Res

@Serializable
class MapData(@SerialName("locations") val locations: List<Location>)

@Serializable
class Location(
    @SerialName("id") val id: Int?,
    @SerialName("map_id") val mapId: Int?,
    @SerialName("region_id") val regionId: Int?,
    @SerialName("category_id") val categoryId: Int?,
    @SerialName("title") val title: String?,
    @SerialName("description") val description: String?,
    @SerialName("latitude") val latitude: String?,
    @SerialName("longitude") val longitude: String?,
) {
    val latitudeDouble by lazy(LazyThreadSafetyMode.NONE) { latitude?.toDoubleOrNull() ?: 0.0 }
    val longitudeDouble by lazy(LazyThreadSafetyMode.NONE) { longitude?.toDoubleOrNull() ?: 0.0 }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val mapData: MutableState<MapData?> = mutableStateOf(null)
        LaunchedEffect(Unit) {
            mapData.value = Json { ignoreUnknownKeys = true }.decodeFromString<MapData>(Res.readBytes("files/map_data.json").decodeToString())
            println("Locations: ${mapData.value!!.locations.size}")
        }
        Scaffold(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val isSelected = remember { mutableStateOf(false) }
                GoogleMapsComposable(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
                    tileProviderInternal = TileProviderInternalImpl,
                    mapData = mapData,
                    onMarkerClick = {
                        println("Marker clicked: $it")
                        isSelected.value = true
                                    },
                    onMapClick = {
                        println("Map clicked")
                        isSelected.value = false
                                 },
                    markerIconProvider = MarkerIconProviderImpl,
                )
                if (isSelected.value) {
                    Box(
                        modifier = Modifier.padding(bottom = 50.dp).width(200.dp).height(50.dp)
                            .align(Alignment.BottomCenter).clip(
                            RoundedCornerShape(8.dp)
                        ).background(
                            Color.White
                        )
                    )
                }
            }
        }
    }
}