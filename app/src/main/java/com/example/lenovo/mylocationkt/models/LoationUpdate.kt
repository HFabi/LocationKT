package com.example.lenovo.mylocationkt.models

data class LocationUpdate(
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var addressLine: String = "",
    var countryName: String = "")