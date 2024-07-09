package uz.bahadew.bookappupdate.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.bahadew.bookappupdate.domain.AppRepository
import uz.bahadew.bookappupdate.domain.impl.AppRepositoryIml
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppRepositoryModule {

    @[Binds Singleton]
    fun bindAppRepository(impl: AppRepositoryIml): AppRepository
}