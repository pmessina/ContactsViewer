package com.kinandcarta.contactsviewer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.kinandcarta.contactsviewer.ContactsViewerViewModel
import com.kinandcarta.contactsviewer.R
import com.kinandcarta.contactsviewer.data.ContactsItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val contactsViewModel: ContactsViewerViewModel by viewModel()

    var contact = ContactsItem()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            Scaffold(
                modifier = Modifier.fillMaxWidth(),
                topBar = {
                    TopAppBar(
                        title = { Text("Contacts View") },
                        navigationIcon = {
                            IconButton(onClick = { }) { // or your navigation logic
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            )
            {
                ContactsNavHost(contactsViewModel)
                //runOnUiThread {
//                    setContent {
//                        //Navigate to details view
//

//                    }
                //}

            }


        }

    }
}

@Composable
fun ContactsListView(
    modifier: Modifier = Modifier,
    contactsViewModel: ContactsViewerViewModel,
    contact: ContactsItem,
    navController: NavController = rememberNavController()
) {
    contactsViewModel.loadContacts()

    val contactsState = contactsViewModel.getContactsRepository().observeAsState(emptyList())

    val contactsGroup = arrayListOf<ContactsGroup>()
    for (item in contactsState.value) {
        contactsGroup.add(
            ContactsGroup(
                sectionName = if (item.isFavorite) "Favorites" else "Contacts",
                sectionItem = item
            )
        )
    }

    if (contactsState.value.isNotEmpty()) {
        ContactsViewerLazyList(contactsGroup) {
            onContactClick ->
            contactsGroup.filter{ it.sectionItem == onContactClick }
                .map { it.sectionItem?.id }
                .firstOrNull()?.let { contactId ->
                    navController.navigate("contactDetail/$contactId")
                    //contactsViewModel.selectedContact = it
                }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun ContactsDetailView(modifier: Modifier = Modifier, viewModel: ContactsViewerViewModel, contactId: String) {

    viewModel.loadContacts()

    val contactsRepository = viewModel.getContactsRepository().observeAsState(emptyList());

    val selectedContact = contactsRepository.value?.find { it.id == contactId }!!

    Column(
        modifier = modifier,
        Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.user_large_hdpi),
            contentDescription = "Contact Image"
        )
        Text("Home Phone", modifier = Modifier)
        //Text placeholder for phone number
        Text("${selectedContact.phone.home}", modifier = Modifier)
        Text("Mobile Phone", modifier = Modifier)
        Text("${selectedContact.phone.mobile}", modifier = Modifier)
        Text("Address", modifier = Modifier)
        //Text placeholder for address
        Text(text = "${selectedContact.address}", modifier = Modifier)
        Text("Birth date", modifier = Modifier)
        //Text placeholder for birth date
        Text("${selectedContact.birthdate}", modifier = Modifier)
        Text("Email", modifier = Modifier)
        //Text placeholder for email
        Text("${selectedContact.emailAddress}", modifier = Modifier)

    }
}

@Composable
fun ContactListItem(
    contact: ContactsItem,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(contact.smallImageURL),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(contact.name)
            Icon(
                painter = painterResource(
                    if (isFavorite) R.drawable.favorite_true_hdpi else R.drawable.favorite_false_hdpi
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ContactListHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h1,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)
    )
}

@Composable
fun ContactsViewerLazyList(
    contactsGroups: List<ContactsGroup>,
    onContactClick: (ContactsItem) -> Unit
) {
    LazyColumn {

        items(count=contactsGroups.size) { index ->
            val group = contactsGroups[index]
            if (group.sectionItem == null) {
                ContactListHeader(title = group.sectionName)
            } else {
                ContactListItem(
                    contact = group.sectionItem!!,
                    isFavorite = group.sectionItem!!.isFavorite,
                    onClick = { onContactClick(group.sectionItem!!) }
                )
            }
        }
    }
}

@Composable
fun ContactsNavHost(viewModel: ContactsViewerViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "contactsList"
    ) {
        composable("contactsList") {
            ContactsListView(
                contactsViewModel = viewModel,
                contact = viewModel.selectedContact,
                navController = navController
            )


        }
        composable("contactDetail/{contactId}") { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")

            ContactsDetailView(contactId = contactId!!, viewModel = viewModel)
        }
    }
}

data class ContactsGroup (
    var sectionName: String = "",
    var sectionItem: ContactsItem? = null
)



