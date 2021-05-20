package com.kinandcarta.contactsviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kinandcarta.contactsviewer.ContactsViewerRecyclerAdapter
import com.kinandcarta.contactsviewer.ContactsViewerViewModel
import com.kinandcarta.contactsviewer.data.ContactsItem
import com.kinandcarta.contactsviewer.databinding.ContactsListViewContainerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contacts ListView Class
 */
class ContactsListViewFragment : Fragment() {

    private var _binding: ContactsListViewContainerBinding? = null

    private val binding get() = _binding!!

    private val contactsViewModel: ContactsViewerViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = ContactsListViewContainerBinding.bind(view)
        val context = binding.root.context

        val recyclerView = _binding?.contactsRecyclerView!!

        contactsViewModel.getContactsRepository().observe(this.viewLifecycleOwner, { t ->

            if (contactsViewModel.contactsList.isNotEmpty()) {
                contactsViewModel.contactsList.clear()
            }

            val contactsGroups = t.groupBy { it.isFavorite } as HashMap<Boolean, List<ContactsItem>>

            contactsViewModel.contactsList[false] = contactsGroups[false] as ArrayList<ContactsItem>
            contactsViewModel.contactsList[true] = contactsGroups[true] as ArrayList<ContactsItem>

            val contactsAdapter = ContactsViewerRecyclerAdapter(contactsViewModel)
            recyclerView.apply {
                adapter = contactsAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                setHasFixedSize(true)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}