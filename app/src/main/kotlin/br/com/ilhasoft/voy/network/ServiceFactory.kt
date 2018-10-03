package br.com.ilhasoft.voy.network

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import br.com.ilhasoft.voy.BuildConfig
import br.com.ilhasoft.voy.shared.network.Tls12SocketFactory
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by lucasbarros on 05/01/18.
 */
abstract class ServiceFactory<out ApiType>(clazz: Class<ApiType>) :
    BaseFactory<ApiType>(clazz) {

    override fun createOkHttpClient(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
            .writeTimeout(4, TimeUnit.MINUTES)
            .readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)
        }
        enableTls12OnPreLollipop(client)
        return client
    }

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (SDK_INT in 16..21) {
            try {
                val trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                }
                val trustManager = trustManagers[0] as X509TrustManager

                val sc = SSLContext.getInstance("TLS")
                sc.init(null, null, null)

                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory), trustManager)

                val cs = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(
                        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
                    )
                    .build()

                val specs = mutableListOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT, cs)

                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }

        }
        return client
    }
}

