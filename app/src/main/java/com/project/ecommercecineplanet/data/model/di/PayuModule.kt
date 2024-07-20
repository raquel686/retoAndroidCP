package com.project.ecommercecineplanet.data.model.di

import com.project.ecommercecineplanet.data.network.ApiClient
import com.project.ecommercecineplanet.data.network.PayuClient
import com.project.ecommercecineplanet.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PayuModule {
    @Singleton
    @Provides
    @Named("PayuRetrofit")
    fun provideRetrofitPayu(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.PAYU_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(@Named("PayuRetrofit") retrofit: Retrofit): PayuClient {
        return retrofit.create(PayuClient::class.java)
    }
}