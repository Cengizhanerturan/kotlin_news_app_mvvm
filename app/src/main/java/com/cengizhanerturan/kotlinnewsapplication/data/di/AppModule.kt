package com.cengizhanerturan.kotlinnewsapplication.data.di

import android.content.Context
import androidx.room.Room
import com.cengizhanerturan.kotlinnewsapplication.data.repository.NewsRepositoryImpl
import com.cengizhanerturan.kotlinnewsapplication.data.repository.UserRepositoryImpl
import com.cengizhanerturan.kotlinnewsapplication.data.source.locale.NewsDao
import com.cengizhanerturan.kotlinnewsapplication.data.source.locale.NewsDatabase
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.ApiService
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.FirebaseAuthSource
import com.cengizhanerturan.kotlinnewsapplication.data.source.remote.FirestoreSource
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.UserRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.BASE_URL
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserService(): UserService = UserService()

    @Provides
    @Singleton
    fun provideFirebaseAuthSource() = FirebaseAuthSource()

    @Provides
    @Singleton
    fun provideFirestoreSource() = FirestoreSource()

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuthSource: FirebaseAuthSource,
        firestoreSource: FirestoreSource
    ): UserRepository = UserRepositoryImpl(firebaseAuthSource, firestoreSource)

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService, newsDao: NewsDao): NewsRepository =
        NewsRepositoryImpl(apiService, newsDao)

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NewsDatabase::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: NewsDatabase) = database.newsDao()
}