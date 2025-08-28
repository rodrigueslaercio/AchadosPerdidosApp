package com.project.achadoseperdidos.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.project.achadoseperdidos.db.fb.FBDatabase
import com.project.achadoseperdidos.model.MainViewModel
import com.project.achadoseperdidos.ui.BuscarPage
import com.project.achadoseperdidos.ui.CadastroItemPage
import com.project.achadoseperdidos.ui.HomePage
import com.project.achadoseperdidos.ui.MinhasPostagensPage
import com.project.achadoseperdidos.ui.NotificacoesPage

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel, fbDatabase: FBDatabase) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(viewModel = viewModel) }
        composable<Route.MinhasPostagens> { MinhasPostagensPage(viewModel = viewModel) }
        composable<Route.Buscar> { BuscarPage(viewModel = viewModel) }
        composable<Route.Notificacoes> { NotificacoesPage(viewModel = viewModel) }
        composable<Route.Cadastro> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.Cadastro>()
            CadastroItemPage(lat = args.lat, lng = args.lng, onBack = {
                viewModel.clearItem()
                navController.popBackStack()
            }, fbDatabase = fbDatabase, viewModel = viewModel)
        }
    }
}