package com.project.achadoseperdidos.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.project.achadoseperdidos.model.MainViewModel

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val activity = LocalContext.current as? Activity
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .wrapContentSize(Alignment.Center)
    ) {
        val camPosState = rememberCameraPositionState()
        val context = LocalContext.current
        val hasLocationPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        var markerPosition by remember { mutableStateOf<LatLng?>(null) }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camPosState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            onMapClick = { latLng ->
                markerPosition = latLng
                viewModel.setSelectedMarkerPosition(latLng)
            }
        ) {
            viewModel.globalItems.filter { it.recuperado == false }.forEach {

                println("${it.titulo} - recuperado=${it.recuperado}")
                it.localizacao?.let { loc ->
                    Marker(state = MarkerState(position = loc), title = it.titulo)
                }
            }

            markerPosition?.let { pos ->
                Marker(
                    state = MarkerState(position = pos),
                    title = "Local do item",
                    draggable = true
                )
            }
        }
    }
}
