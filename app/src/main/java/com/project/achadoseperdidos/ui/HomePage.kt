package com.project.achadoseperdidos.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
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
        GoogleMap (modifier = Modifier.fillMaxSize(), cameraPositionState = camPosState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)) {}
    }
}