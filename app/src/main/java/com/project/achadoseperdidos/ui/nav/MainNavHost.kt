package com.project.achadoseperdidos.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.achadoseperdidos.ui.BuscarPage
import com.project.achadoseperdidos.ui.HomePage
import com.project.achadoseperdidos.ui.MinhasPostagensPage
import com.project.achadoseperdidos.ui.NotificacoesPage

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage() }
        composable<Route.MinhasPostagens> { MinhasPostagensPage() }
        composable<Route.Buscar> { BuscarPage() }
        composable<Route.Notificacoes> { NotificacoesPage() }
    }
}