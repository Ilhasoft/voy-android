package br.com.ilhasoft.voy.network.interceptors

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


/**
 * Created by felipe on 14/03/18.
 */
class ContentTypeHeaderInterceptorTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should use application json for content type when use default`() {
        val request = createAFakeCallMockWebServer(ContentTypeHeaderInterceptor())
        assertEquals("application/json", request.getHeader("Content-Type"))
    }

    @Test
    fun `Should use application pdf for content type when pass application pdf on constructor`() {
        val request = createAFakeCallMockWebServer(ContentTypeHeaderInterceptor("application/pdf"))
        assertEquals("application/pdf", request.getHeader("Content-Type"))
    }

    private fun createAFakeCallMockWebServer(interceptor: Interceptor): RecordedRequest {
        mockWebServer.enqueue(MockResponse())

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(interceptor).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url("/")).build()).execute()

        return mockWebServer.takeRequest()
    }
}