package com.kinandcarta.contactsviewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kinandcarta.contactsviewer.databinding.ContactsListViewHeaderBinding
import com.kinandcarta.contactsviewer.databinding.ContactsListViewItemBinding
import com.kinandcarta.contactsviewer.data.ContactsItem

class ContactsViewerRecyclerAdapter(private val contactsViewModel: ContactsViewerViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val contactsGroups = ArrayList<ContactsGroup>()
    private val contactsList = contactsViewModel.contactsList

    init {

        val header = ContactsGroup()
        header.sectionName = "Favorite Contacts"
        header.sectionItem = null
        contactsGroups.add(header)

        for (item in contactsList[true]!!) {

            val grp = ContactsGroup()
            grp.sectionName = "Favorite Contacts"
            grp.sectionItem = item
            contactsGroups.add(grp)
        }

        val header2 = ContactsGroup()
        header2.sectionName = "Other Contacts"
        header2.sectionItem = null
        contactsGroups.add(header2)

        for (item in contactsList[false]!!) {
            val grp = ContactsGroup()
            grp.sectionName = "Other Contacts"
            grp.sectionItem = item
            contactsGroups.add(grp)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == 0 || holder.itemViewType == 2) {
            val headerViewHolder = holder as ContactsHeaderViewHolder
            headerViewHolder.binding.root.tag = contactsGroups[position]
            headerViewHolder.binding.tvContactCategory.text = contactsGroups[position].sectionName

        }

        if (holder.itemViewType == 1 || holder.itemViewType == 3) {

            val itemViewHolder = holder as ContactsListViewHolder

            itemViewHolder.binding.root.tag = contactsGroups[position]

            itemViewHolder.binding.tvName.text =
                contactsGroups[position].sectionItem?.name

            Glide
                .with(holder.itemView.context)
                .load(contactsGroups[position].sectionItem?.smallImageURL)
                .error(Glide.with(holder.itemView.context).load(R.drawable.user_icon_small_hdpi))
                .apply(RequestOptions().override(300, 300))
                .into(itemViewHolder.binding.imgSmallURL)

            if (contactsGroups[position].sectionItem?.isFavorite == true)
                itemViewHolder.binding.imageView.setImageResource(R.drawable.favorite_true_hdpi)
            else {
                itemViewHolder.binding.imageView.setImageResource(R.drawable.favorite_false_hdpi)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1 || viewType == 3) {
            val itemBinding =
                ContactsListViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

            return ContactsListViewHolder(contactsViewModel, itemBinding)
        } else {
            val headerBinding = ContactsListViewHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ContactsHeaderViewHolder(viewType, headerBinding)
        }

    }

    override fun getItemCount(): Int {
        return contactsGroups.size
    }

    override fun getItemViewType(position: Int): Int {

        if (contactsGroups[position].sectionName == "Other Contacts" && contactsGroups[position].sectionItem == null) {
            return 0
        }

        if (contactsGroups[position].sectionName == "Other Contacts" && contactsGroups[position].sectionItem != null) {
            return 1
        }

        if (contactsGroups[position].sectionName == "Favorite Contacts" && contactsGroups[position].sectionItem == null) {
            return 2
        }

        if (contactsGroups[position].sectionName == "Favorite Contacts" && contactsGroups[position].sectionItem != null) {
            return 3
        }

        return -1
    }
}

//Holder for recyclerview item
class ContactsListViewHolder(
    private val viewModel: ContactsViewerViewModel,
    val binding: ContactsListViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    var isFavorite: Boolean = false

    init {
        binding.root.setOnClickListener {

            if (binding.root.tag != null) {
                val tag = binding.root.tag as ContactsGroup
                viewModel.selectedContact = tag.sectionItem!!
                isFavorite = viewModel.selectedContact.isFavorite
                binding.root.findNavController()
                    .navigate(R.id.action_ContactsListViewFragment_to_ContactsDetailViewFragment)
            }
        }
    }

}

//Holder for recyclerview item header
class ContactsHeaderViewHolder(
    val viewType: Int = 0,
    val binding: ContactsListViewHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {

}

//Container class for categorizing Contacts by section name
class ContactsGroup {
    var sectionName: String = ""
    var sectionItem: ContactsItem? = null
}
