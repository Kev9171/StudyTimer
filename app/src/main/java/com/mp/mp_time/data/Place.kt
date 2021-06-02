package com.mp.mp_time.data

import com.google.android.gms.maps.model.LatLng

data class Place(
    var placeName: String,
    var location: LatLng,
    var title: String,
    var comment: String,
    var rating: Float
)