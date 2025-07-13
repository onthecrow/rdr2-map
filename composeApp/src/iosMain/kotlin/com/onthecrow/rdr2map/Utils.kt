package com.onthecrow.rdr2map

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage? {
    val width = this.width
    val height = this.height
    val buffer = IntArray(width * height)

    this.readPixels(buffer)

    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toUIImage(): UIImage {
    val nsData = this.usePinned {
        NSData.dataWithBytes(it.addressOf(0), size.toULong())
    }
    // Create UIImage from NSData
    return UIImage(data = nsData)
}

/**
 * Converts a ByteArray containing encoded image data into a UIImage,
 * resizing it to the specified width and height.
 *
 * @param width The desired width of the resulting UIImage (pt, not px).
 * @param height The desired height of the resulting UIImage (pt, not px).
 * @return A UIImage scaled to the given dimensions, or null if the ByteArray
 * could not be decoded into an image.
 */
@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toUIImage(width: Double, height: Double): UIImage? {
    val nsData = this.usePinned { pinned ->
        NSData.dataWithBytes(bytes = pinned.addressOf(index = 0), length = size.toULong())
    }

    val originalImage = UIImage(data = nsData)

    if (originalImage == null) {
        return null
    }

    val newSize = CGSizeMake(width, height)
    UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)

    originalImage.drawInRect(CGRectMake(0.0, 0.0, width, height))

    val resizedImage = UIGraphicsGetImageFromCurrentImageContext()

    UIGraphicsEndImageContext()

    return resizedImage
}

@OptIn(ExperimentalForeignApi::class)
fun IntArray.toUIImage(): UIImage? {
    val width = 256
    val height = 256


    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        data = this.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    )

    val cgImage = CGBitmapContextCreateImage(context)
    return cgImage?.let { UIImage.imageWithCGImage(it) }
}