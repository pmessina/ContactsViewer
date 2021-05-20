package com.kinandcarta.contactsviewer.data

data class ContactsItem(
    val address: Address = Address("", "", "", "", ""),
    val birthdate: String = "",
    val companyName: String = "",
    val emailAddress: String = "",
    val id: String = "",
    val isFavorite: Boolean = false,
    val largeImageURL: String = "",
    val name: String = "",
    val phone: Phone = Phone("", "", ""),
    val smallImageURL: String = ""
){

    override fun toString(): String {
        return "$address $birthdate $companyName $emailAddress $id $isFavorite $largeImageURL $name $phone $smallImageURL"
    }

}