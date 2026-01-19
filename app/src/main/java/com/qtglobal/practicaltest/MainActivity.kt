package com.qtglobal.practicaltest

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.qtglobal.practicaltest.ui.screen.EmailViewScreen
import com.qtglobal.practicaltest.ui.theme.PracticalTestTheme
import com.qtglobal.practicaltest.ui.viewmodel.EmailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: EmailViewModel by viewModels()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.loadEmail(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticalTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmailViewScreen(
                        viewModel = viewModel,
                        onPickFile = {
                            filePickerLauncher.launch("*/*")
                        }
                    )
                }
            }
        }
    }
}


//Scaffold(
//topBar = {
//    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//        ),
//        title = {
//            Text("Small Top App Bar")
//        }
//    )
//},
//) { innerPadding ->
//    ScrollContent(innerPadding)
//}