package com.malikstudios.backtune.models

data class AmbientSound(
    val id: String,
    val name: String,
    val resourceName: String // Remove default value to ensure proper resource names
) 