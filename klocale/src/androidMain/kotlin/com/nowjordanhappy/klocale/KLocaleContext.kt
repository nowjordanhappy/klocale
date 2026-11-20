package com.nowjordanhappy.klocale

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference

/**
 * The application Context and the live activities, so `KLocale(supported)` needs nothing passed in and a pick
 * below 13 can recreate what is on screen. Filled by [KLocaleInitializer] before `Application.onCreate`.
 * Public only for the `klocale-appcompat` artifact.
 */
object KLocaleContext {
    private var context: Context? = null
    private val activities = mutableListOf<WeakReference<Activity>>()

    val app: Context
        get() = checkNotNull(context) { "KLocale isn't initialised: is KLocaleInitializer removed from the merged manifest?" }

    internal fun init(application: Application) {
        context = application
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activities.removeAll { it.get() == null }
                activities += WeakReference(activity)
            }
            override fun onActivityDestroyed(activity: Activity) {
                activities.removeAll { it.get() == null || it.get() === activity }
            }
            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        })
    }

    /** Below 13 nothing in the system rebuilds the screens after a pick: every live Activity is recreated. */
    internal fun recreateActivities() {
        Handler(Looper.getMainLooper()).post { activities.mapNotNull { it.get() }.forEach { it.recreate() } }
    }
}

/** Runs before Application.onCreate (as WorkManager and Firebase do) to hand [KLocaleContext] the app Context. */
class KLocaleInitializer : ContentProvider() {
    override fun onCreate(): Boolean {
        (context?.applicationContext as? Application)?.let(KLocaleContext::init)
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
