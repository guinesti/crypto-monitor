package guinesti.com.github.crypto_monitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import guinesti.com.github.crypto_monitor.ui.theme.CryptomonitorTheme
import guinesti.com.github.crypto_monitor.ui.theme.screens.CryptoMonitorScreen
import guinesti.com.github.crypto_monitor.viewmodel.CryptoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CryptoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptomonitorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CryptoMonitorScreen(viewModel = viewModel)
                }
            }
        }
    }
}