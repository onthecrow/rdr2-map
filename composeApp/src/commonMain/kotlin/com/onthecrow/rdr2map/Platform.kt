package com.onthecrow.rdr2map

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform