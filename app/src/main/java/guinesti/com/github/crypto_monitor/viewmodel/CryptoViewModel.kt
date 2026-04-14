package guinesti.com.github.crypto_monitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guinesti.com.github.crypto_monitor.model.TickerResponse
import guinesti.com.github.crypto_monitor.service.MercadoBitcoinServiceFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CryptoUiState {

    object Loading : CryptoUiState()

    data class Success(val ticker: TickerResponse) : CryptoUiState()
    data class Error(val message: String) : CryptoUiState()

    object Initial : CryptoUiState()
}

class CryptoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CryptoUiState>(CryptoUiState.Initial)

    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    private val service = MercadoBitcoinServiceFactory().create()

    fun resetToInitial() {
        _uiState.value = CryptoUiState.Initial
    }

    fun fetchTickerData() {
        viewModelScope.launch {
            _uiState.value = CryptoUiState.Loading

            try {
                val response = service.getTicker()

                if (response.isSuccessful) {
                    response.body()?.let { tickerResponse ->
                        _uiState.value = CryptoUiState.Success(tickerResponse)
                    } ?: run {
                        _uiState.value = CryptoUiState.Error("Resposta vazia do servidor")
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Bad Request"
                        401 -> "Unauthorized"
                        403 -> "Forbidden"
                        404 -> "Not Found"
                        else -> "Erro desconhecido: ${response.code()}"
                    }
                    _uiState.value = CryptoUiState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _uiState.value = CryptoUiState.Error("Falha na chamada: ${e.message}")
            }
        }
    }
}