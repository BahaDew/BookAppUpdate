package uz.bahadew.bookappupdate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.bahadew.bookappupdate.navigation.AppNavigationDispatcher
import uz.bahadew.bookappupdate.navigation.AppNavigationHandler
import uz.bahadew.bookappupdate.navigation.AppNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @[Binds Singleton]
    fun bindAppNavigator(impl : AppNavigationDispatcher) : AppNavigator

    @[Binds Singleton]
    fun bindAppNavigationHandler(impl : AppNavigationDispatcher) : AppNavigationHandler
}