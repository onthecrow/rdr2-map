package com.onthecrow.rdr2map.ui.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.onthecrow.rdr2map.MarkerIconProvider
import androidx.core.graphics.scale

class MarkerCache(private val markerIconProvider: MarkerIconProvider) {
    private val markers = mutableMapOf<String, BitmapDescriptor?>()

    fun getMarkerIcon(id: String, width: Int, height: Int): BitmapDescriptor? {
        return markers.getOrPut(id) {
            byteArrayToBitmapDescriptor(
                markerIconProvider.getMarkerIcon(
                    id
                ), width, height
            )
        }
    }

    /**
     * Converts a ByteArray representing a PNG image into a BitmapDescriptor.
     *
     * @param pngByteArray The ByteArray containing the PNG image data.
     * @param width The desired width (pixels) of the resulting BitmapDescriptor.
     * @param height The desired height (pixels) of the resulting BitmapDescriptor.
     * @return A BitmapDescriptor created from the PNG image, or null if conversion fails.
     */
    private fun byteArrayToBitmapDescriptor(
        pngByteArray: ByteArray?,
        width: Int,
        height: Int
    ): BitmapDescriptor? {
        if (pngByteArray == null) return null

        val originalBitmap: Bitmap? = try {
            BitmapFactory.decodeByteArray(pngByteArray, 0, pngByteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (originalBitmap == null) {
            println("Error: Could not decode ByteArray into Bitmap. Ensure it's a valid PNG.")
            return null
        }

        val scaledBitmap = originalBitmap.scale(width, height)

        // Convert the scaled bitmap to a BitmapDescriptor
        return try {
            BitmapDescriptorFactory.fromBitmap(scaledBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error: Could not create BitmapDescriptor from scaled Bitmap.")
            null
        } finally {
            // It's good practice to recycle the original bitmap if it's no longer needed
            // However, createScaledBitmap often returns a new bitmap, so this might not be strictly necessary
            // depending on how you manage your original bitmap, but it's safe to do if you're sure
            // the originalBitmap is not used elsewhere.
            // If originalBitmap is not recycled, it could lead to memory leaks for large images.
            if (!originalBitmap.isRecycled && originalBitmap != scaledBitmap) {
                originalBitmap.recycle()
            }
        }
    }
}