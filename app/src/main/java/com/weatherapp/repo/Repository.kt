package com.weatherapp.repo

import com.weatherapp.db.fb.FBCity
import com.weatherapp.db.fb.FBDatabase
import com.weatherapp.db.fb.FBUser
import com.weatherapp.db.fb.toFBCity
import com.weatherapp.db.local.LocalDatabase
import com.weatherapp.db.local.toCity
import com.weatherapp.db.local.toLocalCity
import com.weatherapp.model.City
import com.weatherapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(
    private val fbDB: FBDatabase,
    private val localDB: LocalDatabase
) : FBDatabase.Listener {

    interface Listener {

        fun onUserLoaded(user: User)

        fun onUserSignOut()

        fun onCityAdded(city: City)

        fun onCityUpdated(city: City)

        fun onCityRemoved(city: City)
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener? = null) {
        this.listener = listener
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var cityMap =
        emptyMap<String, City>()

    init {

        fbDB.setListener(this)

        ioScope.launch {

            localDB.getCities().collect { list ->

                val cities =
                    list.map { it.toCity() }

                val names =
                    cities.map { it.name }

                val deleted =
                    cityMap.filter {
                        it.key !in names
                    }.values

                val updated =
                    cities.filter {
                        it.name in cityMap.keys
                    }

                val added =
                    cities.filter {
                        it.name !in cityMap.keys
                    }

                mainScope.launch {

                    added.forEach {
                        listener?.onCityAdded(it)
                    }

                    updated.forEach {
                        listener?.onCityUpdated(it)
                    }

                    deleted.forEach {
                        listener?.onCityRemoved(it)
                    }

                    cityMap = cities.associateBy { it.name }
                }
            }
        }
    }

    fun add(city: City) =
        fbDB.add(city.toFBCity())

    fun remove(city: City) =
        fbDB.remove(city.toFBCity())

    fun update(city: City) =
        fbDB.update(city.toFBCity())

    override fun onUserLoaded(user: FBUser) {
        listener?.onUserLoaded(user.toUser())
    }

    override fun onUserSignOut() {
        listener?.onUserSignOut()
    }

    override fun onCityAdded(city: FBCity) {
        localDB.insert(city.toCity().toLocalCity())
    }

    override fun onCityUpdated(city: FBCity) {
        localDB.update(city.toCity().toLocalCity())
    }

    override fun onCityRemoved(city: FBCity) {
        localDB.delete(city.toCity().toLocalCity())
    }
}