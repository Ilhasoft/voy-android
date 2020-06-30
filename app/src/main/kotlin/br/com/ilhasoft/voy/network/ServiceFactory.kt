package br.com.ilhasoft.voy.network

import br.com.ilhasoft.voy.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by lucasbarros on 05/01/18.
 */
abstract class ServiceFactory<out ApiType>(clazz: Class<ApiType>) :
    BaseFactory<ApiType>(clazz) {

    override fun createOkHttpClient(certificate: X509Certificate?): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
            .writeTimeout(4, TimeUnit.MINUTES)
            .readTimeout(4, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(loggingInterceptor)
        }
        return if (certificate != null) setupSSLSocket(client, certificate) else client
    }

    private fun setupSSLSocket(client: OkHttpClient.Builder, certificate: X509Certificate): OkHttpClient.Builder {

        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType).apply {
            load(null, null)
            setCertificateEntry("ca", certificate)
        }

        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm).apply {
            init(keyStore)
        }

        val sslContext: SSLContext = SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers, null)
        }
        client.sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
        return client
    }

}
