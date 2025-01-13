package com.example.login.data.local

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

class LocationHelper(context: Context) {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation(onSuccess: (Location?) -> Unit, onFailure: (Exception) -> Unit){
        try{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location->
                    onSuccess(location)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: SecurityException){
            onFailure(e)
        }
    }
}