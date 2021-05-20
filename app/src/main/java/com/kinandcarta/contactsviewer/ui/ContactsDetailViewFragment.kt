package com.kinandcarta.contactsviewer.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kinandcarta.contactsviewer.ContactsViewerViewModel
import com.kinandcarta.contactsviewer.R
import com.kinandcarta.contactsviewer.databinding.ContactsDetailViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contacts Details class
 */
class ContactsDetailViewFragment : Fragment() {

    private val contactsViewModel: ContactsViewerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ContactsDetailViewBinding.bind(view)

        val selectedContact = contactsViewModel.selectedContact

        binding.tvDVName.text = selectedContact.name
        binding.tvDVAddress.text = selectedContact.address.toString()
        binding.tvDVMobilePhone.text = selectedContact.phone.mobile
        binding.tvDVHomePhone.text = selectedContact.phone.home
        binding.tvDVBirthdate.text = selectedContact.birthdate
        binding.tvDVEmail.text = selectedContact.emailAddress

        Glide.with(this)
            .load(selectedContact.largeImageURL)
            .error(R.drawable.user_large_hdpi)
            .apply(RequestOptions().override(300, 300))
            .into(binding.imgLargeURL)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_main, menu)

        val selectedContact = contactsViewModel.selectedContact
        val favorite = selectedContact.isFavorite

        menu.findItem(R.id.action_settings).setIcon(R.drawable.favorite_true_hdpi)

        menu.findItem(R.id.action_settings).isChecked = favorite


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPrepareOptionsMenu(menu: Menu) {
        val selectedContact = contactsViewModel.selectedContact
        val favorite = selectedContact.isFavorite

        menu.findItem(R.id.action_settings).isChecked = favorite

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Change selected.isFavorite based on the icon that is selected
        return super.onOptionsItemSelected(item)
    }


}