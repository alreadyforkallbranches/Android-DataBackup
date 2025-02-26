package com.xayah.databackup.ui.activity.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xayah.databackup.ui.activity.settings.components.SettingsScaffold
import com.xayah.databackup.ui.activity.settings.components.content.onAppInitialize
import com.xayah.databackup.ui.activity.settings.components.content.onBackupInitialize
import com.xayah.databackup.ui.activity.settings.components.content.onRestoreInitialize
import com.xayah.databackup.ui.activity.settings.components.content.onUserInitialize
import com.xayah.databackup.ui.theme.DataBackupTheme
import com.xayah.databackup.util.GlobalObject
import com.xayah.materialyoufileexplorer.MaterialYouFileExplorer
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class SettingsActivity : ComponentActivity() {
    private lateinit var explorer: MaterialYouFileExplorer

    private fun onInitialize(viewModel: SettingsViewModel) {
        viewModel.viewModelScope.launch {
            onAppInitialize(viewModel, this@SettingsActivity)
            onUserInitialize(viewModel, this@SettingsActivity)
            onBackupInitialize(viewModel, this@SettingsActivity)
            onRestoreInitialize(viewModel, this@SettingsActivity)
            viewModel.isInitialized.targetState = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        GlobalObject.initializeRootService {
            onInitialize(viewModel)
        }

        explorer = MaterialYouFileExplorer().apply {
            initialize(this@SettingsActivity)
        }
        setContent {
            DataBackupTheme {
                val isInitialized = viewModel.isInitialized
                SettingsScaffold(
                    isInitialized = isInitialized,
                    viewModel = viewModel,
                    explorer = explorer
                ) { finish() }
            }
        }
    }
}

