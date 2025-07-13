package com.onthecrow.rdr2map

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rdr2_map.composeapp.generated.resources.Res

interface MarkerIconProvider {
    fun getMarkerIcon(id: String): ByteArray?
}

@OptIn(ExperimentalResourceApi::class)
internal object MarkerIconProviderImpl: MarkerIconProvider {
    override fun getMarkerIcon(id: String): ByteArray? {
        return runBlocking {
            println("Marker icon: $id")
            return@runBlocking try {
                Res.readBytes("files/32141234.png").also {
                    println("Marker icon: $id | ${it.size}")
                }
            } catch (e: Throwable) {
                println(e)
                Res.readBytes("files/7/0-0.jpg")
            }
        }
    }
}