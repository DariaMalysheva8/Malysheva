package com.dariamalysheva.malysheva.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dariamalysheva.malysheva.R
import com.dariamalysheva.malysheva.presentation.popularFilms.PopularFragment
import com.dariamalysheva.malysheva.utils.constants.Constants

class MainActivity : AppCompatActivity() {

    private val preferences: SharedPreferences by lazy {
        getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            preferences.edit()
                .putBoolean(Constants.PREF_FROM_NETWORK_VALUE, true)
                .apply()
            navigateToFragment(PopularFragment())
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            commit()
        }
    }
}