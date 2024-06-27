package utils

import com.sngular.marvelkmp.BuildKonfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
import org.kotlincrypto.hash.md.MD5

@OptIn(ExperimentalUnsignedTypes::class)
private fun md5Digest(toByteArray: ByteArray): String {
    val md5Digest = MD5()
    return md5Digest.digest(toByteArray)
        .toUByteArray()
        .joinToString("") { it.toString(16).padStart(2, '0') }
}

fun HttpRequestBuilder.authorized() {
    val ts = Clock.System.now().epochSeconds.toString()
    val hash =
        md5Digest("$ts${BuildKonfig.MARVEL_PRIVATE_KEY}${BuildKonfig.MARVEL_PUBLIC_KEY}".toByteArray())
    parameter("ts", ts)
    parameter("hash", hash)
}
