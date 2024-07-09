package uz.bahadew.bookappupdate.navigation

import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {
    val buffer : Flow<AppNavigation>
}