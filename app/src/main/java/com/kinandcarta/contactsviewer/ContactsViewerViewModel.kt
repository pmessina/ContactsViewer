package com.kinandcarta.contactsviewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kinandcarta.contactsviewer.data.ContactsItem
import com.kinandcarta.contactsviewer.data.ContactsViewerRepository

class ContactsViewerViewModel(private val contactsViewerRepository: ContactsViewerRepository) : ViewModel() {

    //Used for storing contacts and for retrieval by the view
    var contactsList = HashMap<Boolean, ArrayList<ContactsItem>>()

    //Holds selected contact for recyclerview
    var selectedContact = ContactsItem()

    //Keep state of toggle button
    var isFavorite = false

    fun getContactsRepository(): LiveData<ArrayList<ContactsItem>> {
        return contactsViewerRepository.getContacts()
    }

}

