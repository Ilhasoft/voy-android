package br.com.ilhasoft.voy.shared.managers

import android.content.Context
import android.content.res.Resources.NotFoundException
import java.io.BufferedInputStream
import java.io.InputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

object CertificateManager {

    fun getX509Certificate(context: Context, rawResourceId: Int): X509Certificate? {
        val factory: CertificateFactory = CertificateFactory.getInstance("X.509")
        var certificate: X509Certificate? = null
        try {
            val certificateStream: InputStream = BufferedInputStream(context.resources.openRawResource(rawResourceId))
            certificate = certificateStream.let { factory.generateCertificate(it) as X509Certificate }
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }
        return certificate
    }

    /*
    * Note:
    * Add other functions to get other certificate types as needed
    * */

}