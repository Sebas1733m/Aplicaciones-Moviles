package com.example.myapplication33

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform