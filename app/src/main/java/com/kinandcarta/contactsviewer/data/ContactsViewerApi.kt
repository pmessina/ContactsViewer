package com.kinandcarta.contactsviewer.data

import retrofit2.Call
import retrofit2.http.GET

interface ContactsViewerApi {

    @GET("technical-challenge/v3/contacts.json")
    fun getContacts(): Call<ArrayList<ContactsItem>>
}