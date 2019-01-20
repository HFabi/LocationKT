package com.example.lenovo.mylocationkt.models

/**
 * Location Update
 *
 * @constructor
 * @property longitude longitude
 * @property latitude latitude
 * @property addressLine addressLine
 * @property countryName country name
 */
data class LocationUpdate(
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var addressLine: String = "",
    var countryName: String = "")