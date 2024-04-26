package com.example.mygithubapplication.data.retrofit

import com.example.mygithubapplication.BuildConfig
import com.example.mygithubapplication.data.response.DetailUserResponse
import com.example.mygithubapplication.data.response.GithubResponse
import com.example.mygithubapplication.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @GET("search/users")
    // @Headers("Authorization: Bearer " + BuildConfig.KEY)
    fun getUser(
        @Query("q") query: String?,
    ): Call<GithubResponse>

    @GET("users/{login}")
    // @Headers("Authorization: Bearer " + BuildConfig.KEY)
    fun getDetailUser(
        @Path("login") login: String
    ): Call<DetailUserResponse>

    @GET("users/{login}/followers")
    // @Headers("Authorization: Bearer " + BuildConfig.KEY)
    fun getFollowers(
        @Path("login") login: String?,
    ): Call<List<ItemsItem>>

    @GET("users/{login}/following")
    // @Headers("Authorization: Bearer " + BuildConfig.KEY)
    fun getFollowings(
        @Path("login") login: String?,
    ): Call<List<ItemsItem>>

}
