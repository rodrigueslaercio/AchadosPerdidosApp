package com.project.achadoseperdidos.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


sealed interface Route {
    @Serializable
    data object Home : Route
    @Serializable
    data object MinhasPostagens : Route
    @Serializable
    data object Notificacoes : Route
    @Serializable
    data object Buscar : Route
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: Route) {
    data object HomeButton : BottomNavItem("Início", Icons.Default.Home, Route.Home)
    data object MinhasPostagensButton : BottomNavItem("Minhas postagens", Icons.Default.Favorite, Route.MinhasPostagens)
    data object NotificacoesButton : BottomNavItem("Notificações", Icons.Default.Notifications, Route.Notificacoes)
    data object BuscarButton : BottomNavItem("Buscar", Icons.Default.Search, Route.Buscar)
}