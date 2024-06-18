package network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class NetworkResult<out T>(val status: ApiStatus, val data: T?, val message: String?) {
    data class Success<out T>(val _data: T?) :
        NetworkResult<T>(status = ApiStatus.SUCCESS, data = _data, message = null)

    data class Error<out T>(val _data: T?, val exception: String) :
        NetworkResult<T>(status = ApiStatus.ERROR, data = _data, message = exception)

    data class Loading<out T>(val isLoading: Boolean) :
        NetworkResult<T>(status = ApiStatus.LOADING, data = null, message = null)
}

fun <T> toResultFlow(call: suspend () -> NetworkResult<T?>) : Flow<NetworkResult<T>> {
    return flow {
        emit(NetworkResult.Loading(true))
        val c = call.invoke()
        c.let { response ->
            try {
                println("response${response.data}")
                emit(NetworkResult.Success(response.data))
            } catch (e: Exception) {
                //  emit(NetWorkResult.Error("error", e.toString()))
            }
        }
    }
}