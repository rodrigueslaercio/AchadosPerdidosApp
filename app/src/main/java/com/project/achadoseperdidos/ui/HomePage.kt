package com.project.achadoseperdidos.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.project.achadoseperdidos.model.MainViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val activity = LocalContext.current as? Activity
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Red).wrapContentSize(Alignment.Center)
    ) {
        val camPosState = rememberCameraPositionState()
        val context = LocalContext.current
        val hasLocationPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            )
        }

        var markerPosition by remember { mutableStateOf(LatLng(-23.5505, -46.6333)) }

        GoogleMap (modifier = Modifier.fillMaxSize(), cameraPositionState = camPosState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            onMapClick = { latLng ->
                markerPosition = latLng
                viewModel.setSelectedMarkerPosition(latLng)
            }) {

                viewModel.items.forEach {
                    if (it.localizacao != null) {
                        Marker(state = MarkerState(position = it.localizacao), title = it.titulo)
                    }
                }

            Marker(state = MarkerState(position = markerPosition),
                title = "Local do item",
                draggable = true,
                )
        }
    }
}