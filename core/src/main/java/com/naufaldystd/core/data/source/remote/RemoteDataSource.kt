package com.naufaldystd.core.data.source.remote

import com.naufaldystd.core.data.source.remote.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
}