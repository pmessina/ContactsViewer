package com.kinandcarta.contactsviewer

import android.app.Application
import com.kinandcarta.contactsviewer.data.ContactsViewerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ContactsViewerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ContactsViewerApplication)
            modules(appModule)
        }
    }

    private val appModule = module {
        single { ContactsViewerRepository() }
        single { ContactsViewerViewModel(get()) }
    }
}