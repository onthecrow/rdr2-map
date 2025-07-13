package com.onthecrow.rdr2map.ui.map

import com.onthecrow.rdr2map.MarkerIconProvider
import com.onthecrow.rdr2map.toUIImage
import platform.UIKit.UIImage

class MarkerCache(private val markerIconProvider: MarkerIconProvider) {
    private val markers = mutableMapOf<String, UIImage?>()

    fun getMarker(markerId: String): UIImage? {
        return markers.getOrPut(markerId) { createMarker(markerId) }
    }

    private fun createMarker(markerId: String): UIImage? {
        return markerIconProvider.getMarkerIcon(markerId)?.toUIImage(38.0, 51.0)
    }
}