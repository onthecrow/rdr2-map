package com.onthecrow.rdr2map

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rdr2_map.composeapp.generated.resources.Res

interface TileProviderInternal {
    fun getTile(z: Int, x: Int, y: Int): ByteArray?
}

@OptIn(ExperimentalResourceApi::class)
internal object TileProviderInternalImpl: TileProviderInternal {
    override fun getTile(z: Int, x: Int, y: Int): ByteArray? {
        return runBlocking {
            return@runBlocking try {
                Res.readBytes("files/$z/$x-$y.jpg")
            } catch (e: Throwable) {
                println(e)
                Res.readBytes("files/7/0-0.jpg")
            }
        }
    }
}