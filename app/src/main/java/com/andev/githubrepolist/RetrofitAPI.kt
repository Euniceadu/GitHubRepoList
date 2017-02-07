package com.andev.githubrepolist

import retrofit2.Call
import retrofit2.http.GET
import java.util.*

/**
 * Created by Eunice A. Obugyei on 02/02/2017.
 */
interface RetrofitAPI {

  @GET("/repositories")
  fun retrieveRepositories(): Call<ArrayList<Repository>>
}