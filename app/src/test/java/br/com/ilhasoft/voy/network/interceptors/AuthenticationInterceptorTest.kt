package br.com.ilhasoft.voy.network.interceptors

import br.com.ilhasoft.voy.network.BaseFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by felipe on 14/03/18.
 */
class AuthenticationInterceptorTest {

    private val mockWebServer = MockWebServer()
    private val mockedToken = UUID.randomUUID().toString()

    @Before
    fun setUp() {
        mockWebServer.start()
        BaseFactory.accessToken = mockedToken
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should add authorization token when call service`() {
        val request = createAFakeCallMockWebServer(AuthenticationInterceptor())
        assertEquals("Token $mockedToken", request.getHeader("Authorization"))
    }

    private fun createAFakeCallMockWebServer(interceptor: Interceptor): RecordedRequest {
        mockWebServer.enqueue(MockResponse())

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        return mockWebServer.takeRequest()
    }

}