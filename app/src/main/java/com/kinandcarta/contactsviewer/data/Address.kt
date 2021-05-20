package com.kinandcarta.contactsviewer.data

data class Address(
    val city: String,
    val country: String,
    val state: String,
    val street: String,
    val zipCode: String
){
    override fun toString(): String {
        return "$street, $city $state, $zipCode, $country"
    }
}