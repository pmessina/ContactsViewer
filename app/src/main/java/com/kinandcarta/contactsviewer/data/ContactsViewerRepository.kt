package com.kinandcarta.contactsviewer.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContactsViewerRepository {

    var contactsApi: ContactsViewerApi

    init {

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://s3.amazonaws.com/")
            .build()

        contactsApi = retrofit.create(ContactsViewerApi::class.java)
    }

    fun getContacts(): MutableLiveData<ArrayList<ContactsItem>> {

        val contactsData = MutableLiveData<ArrayList<ContactsItem>>()

        contactsApi.getContacts().enqueue(object : Callback<ArrayList<ContactsItem>> {
            override fun onResponse(call: Call<ArrayList<ContactsItem>>, response: Response<ArrayList<ContactsItem>>) {
                contactsData.value = response.body()
                Log.i("ContactsApi", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<ContactsItem>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return contactsData
    }
}