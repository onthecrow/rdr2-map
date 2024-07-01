package com.onthecrow.rdr2map

import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

interface CustomInfoWindowProvider {
    fun get(title: String, subtitle: String): Pair<ImageBitmap, Float>
}
internal class CustomInfoWindowProviderImpl(
    val textMeasurer: TextMeasurer
) : CustomInfoWindowProvider {
    override fun get(title: String, subtitle: String): Pair<ImageBitmap, Float> {
        return drawToBitmap(title) to 100f
    }

    private fun drawToBitmap(title: String): ImageBitmap {
        val drawScope = CanvasDrawScope()
        val size = Size(100f, 100f) // simple example of 400px by 400px image
        val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
        val canvas = Canvas(bitmap)

        drawScope.draw(
            density = Density(1f),
            layoutDirection = LayoutDirection.Ltr,
            canvas = canvas,
            size = size,
        ) {
            // Draw whatever you want here; for instance, a white background and a red line.
            drawRect(color = Color.White, topLeft = Offset.Zero, size = size)
            drawLine(
                color = Color.Red,
                start = Offset.Zero,
                end = Offset(size.width, size.height),
                strokeWidth = 5f
            )
            drawText(
                textMeasurer = textMeasurer,
                text = title,/*"q w e r t y u i o p a s d f g h j k l z x c v b n m q w e r t y u i o p a s d f g h j k l z x c v b n m"*/
                topLeft = Offset(10f, 10f),
                style = TextStyle(),
                overflow = TextOverflow.Ellipsis,
                size = Size(80f, 80f),
                maxLines = 4
            )
        }
        return bitmap
    }
}