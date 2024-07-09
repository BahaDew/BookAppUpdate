package uz.bahadew.bookappupdate.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.bahadew.bookappupdate.data.MyShared
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MySharedModule {

    @[Provides Singleton]
    fun provideMyShared(@ApplicationContext context: Context): MyShared {
        return MyShared(context.getSharedPreferences("bahadew_book_app", Context.MODE_PRIVATE))
    }
}