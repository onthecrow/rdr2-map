package com.onthecrow.rdr2map.ui.map

import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2D
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
class MapDelegate(
    private val onMarkerClick: (locationId: Int) -> Unit,
    private val onMapClick: () -> Unit,
) : NSObject(), GMSMapViewDelegateProtocol {
    override fun mapView(mapView: GMSMapView, didTapMarker: GMSMarker): Boolean {
        (didTapMarker.userData as Int?)?.run {
            onMarkerClick(this)
        }
        return true
    }
    override fun mapView(mapView: GMSMapView, didTapAtCoordinate: CValue<CLLocationCoordinate2D>) {
        didTapAtCoordinate.useContents {
            onMapClick()
        }
    }
}
