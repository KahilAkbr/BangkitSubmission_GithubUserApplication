package com.example.mygithubapplication.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapplication.data.response.DetailUserResponse
import com.example.mygithubapplication.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _avatar = MutableLiveData<String>()
    val avatar: LiveData<String> = _avatar

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _follower = MutableLiveData<Int>()
    val follower: LiveData<Int> = _follower

    private val _following = MutableLiveData<Int>()
    val following: LiveData<Int> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun getDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiSevice().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _avatar.value = response.body()?.avatarUrl
                        _username.value = response.body()?.login
                        _name.value = response.body()?.name
                        _follower.value = response.body()?.followers
                        _following.value = response.body()?.following
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }
}