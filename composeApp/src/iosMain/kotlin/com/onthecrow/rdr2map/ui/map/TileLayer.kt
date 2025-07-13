package com.onthecrow.rdr2map.ui.map

import cocoapods.GoogleMaps.GMSSyncTileLayer
import com.onthecrow.rdr2map.TileProviderInternal
import com.onthecrow.rdr2map.toUIImage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage
import platform.darwin.NSUInteger

@OptIn(ExperimentalForeignApi::class)
class TileLayer(
    private val tileProviderInternal: TileProviderInternal
) : GMSSyncTileLayer() {
    override fun tileForX(
        x: NSUInteger,
        y: NSUInteger,
        zoom: NSUInteger
    ): UIImage? {
        return tileProviderInternal.getTile(zoom.toInt(), x.toInt(), y.toInt())?.toUIImage()
    }
}
