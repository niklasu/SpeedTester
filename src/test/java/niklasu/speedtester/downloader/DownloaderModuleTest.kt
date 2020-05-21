package niklasu.speedtester.downloader

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Provides
import niklasu.speedtester.config.ConfigProvider
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DownloaderModuleTest {
    private val mockWebServer = MockWebServer()

    private val injector = Guice.createInjector(DownloaderModule(), MockConfigProvider(mockWebServer.url("")))

    class MockConfigProvider(val url: HttpUrl) : AbstractModule() {
        @Provides
        fun getConfigProvider(): ConfigProvider {
            val configProvider = ConfigProvider()
            configProvider.interval = 1
            configProvider.size = 100
            configProvider.url = url.toString()
            return configProvider
        }
    }

    @Test
    fun testDownloads() {
        //GIVEN
        mockWebServer.enqueue(MockResponse())
        val singleDownloader = injector.getInstance(DownloadThread::class.java)

        //WHEN
        singleDownloader.run()

        //THEN
        assertEquals(1, mockWebServer.requestCount)
    }

    @Test
    fun testError() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val createInjector = injector
        val singleDownloader = createInjector.getInstance(DownloadThread::class.java)
        singleDownloader.run()

        assertEquals(1, mockWebServer.requestCount)
    }
}