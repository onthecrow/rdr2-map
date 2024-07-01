package com.onthecrow.rdr2map

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.rememberTextMeasurer
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

const val KEY = "key1"
@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val mapData: MutableState<MapData?> = mutableStateOf(null)
        LaunchedEffect(KEY) {
            mapData.value = Json { ignoreUnknownKeys = true }.decodeFromString<MapData>(Res.readBytes("files/map_data.json").decodeToString())
            println("Locations: ${mapData.value!!.locations.size}")
        }
        val textMeasurer = rememberTextMeasurer()
        GoogleMapsComposable(
            tileProvider = CustomTileProvider,
            mapData = mapData,
            infoWindowProvider = CustomInfoWindowProviderImpl(textMeasurer),
        )
    }
}