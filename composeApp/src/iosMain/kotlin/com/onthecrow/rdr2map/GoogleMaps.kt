package com.onthecrow.rdr2map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition.Companion.cameraWithLatitude
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMapViewOptions
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSSyncTileLayer
import cocoapods.GoogleMaps.kGMSTypeNone
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIView
import platform.darwin.NSObject
import platform.darwin.NSUInteger


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMapsComposable(
    modifier: Modifier,
    tileProvider: TileProvider,
    mapData: MutableState<MapData?>,
    infoWindowProvider: CustomInfoWindowProvider,
) {
    val delegateDelegate = remember {
        object : NSObject(), GMSMapViewDelegateProtocol {
            // Note: this shows an error, but it compiles and runs fine(!)
            override fun mapView(mapView: GMSMapView, markerInfoWindow: GMSMarker): UIView? {
                val infoWindow = infoWindowProvider.get(markerInfoWindow.title ?: "HUY", "")
                val size = infoWindow.second
                return UIImageView(CGRectMake(0.0, 0.0, size.toDouble(), size.toDouble())).apply {
                    this.image = infoWindow.first.toUIImage()
                }
            }
        }
    }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val options = GMSMapViewOptions()
            options.camera = cameraWithLatitude(38.737798, -9.197043, zoom = 4f)
            options.backgroundColor = UIColor(red = .8118, green = .7215, blue = .5843, alpha = 1.0)
            GMSMapView(options = options).apply {
                val mapView = this@apply
                delegate = delegateDelegate
                setMinZoom(minZoom = 2f, maxZoom = 5.7f)
                settings.myLocationButton = true
                mapType = kGMSTypeNone
                myLocationEnabled = false

                settings.setMyLocationButton(false)
                settings.setScrollGestures(true)
                settings.setZoomGestures(true)
                settings.setCompassButton(false)

                object : GMSSyncTileLayer() {
                    override fun tileForX(
                        x: NSUInteger,
                        y: NSUInteger,
                        zoom: NSUInteger
                    ): UIImage? {
                        return tileProvider.getTile(zoom.toInt(), x.toInt(), y.toInt())?.toUIImage()
                    }
                }.apply { map = mapView }
            }
        },
        update = { mapView ->
            mapData.value?.let {
                val posters = it.locations.filter { location -> location.categoryId == 33 }
                posters.map { poster ->
                    GMSMarker.markerWithPosition(
                        CLLocationCoordinate2DMake(poster.latitudeDouble, poster.longitudeDouble)
                    ).apply {

                        title = poster.title
                        map = mapView
                    }
                }
            }
        },
    )
}