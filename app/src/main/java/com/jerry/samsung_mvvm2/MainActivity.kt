package com.jerry.samsung_mvvm2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.jerry.samsung_mvvm.ButtonStateViewModel
import com.jerry.samsung_mvvm2.databinding.ActivityMainBinding

private val Context.dataStore by preferencesDataStore(name = "check_preferences")

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ButtonStateViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(
            this,
            ButtonStateViewModel.ButtonStateViewModelFactory(
                CheckStateRepository(dataStore),
            )
        ).get(ButtonStateViewModel::class.java)

        viewModel.initialSetupEvent.observe(this) { initialSetupEvent ->
            binding.checkBox.isChecked = initialSetupEvent.widgetClicked
            handleBoxChecked(initialSetupEvent.widgetClicked)
            setupOnCheckedChangeListener()
        }

    }

    private fun setupOnCheckedChangeListener() {
        binding.checkBox.setOnCheckedChangeListener { _, checked ->
            viewModel.setClicked(checked)
            handleBoxChecked(checked)
        }
    }

    private val TAG = "MainActivity"

    fun handleBoxChecked(checked: Boolean) {
        Log.e(TAG, "box checked = $checked")
        if (checked) {
            // if checked start foreground activuty
            Intent(application, JustSomeForegroundService::class.java).also { intent ->
                startForegroundService(intent)
            }

        } else {
            // if not checked stop foreground activuty
            Intent(application, JustSomeForegroundService::class.java).also { intent ->
                stopService(intent)
            }
        }
    }


}