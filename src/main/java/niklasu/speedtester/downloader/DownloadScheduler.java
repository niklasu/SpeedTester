package niklasu.speedtester.downloader;

import static java.util.concurrent.TimeUnit.MINUTES;

import niklasu.speedtester.config.ConfigProvider;

import java.util.concurrent.Executors;

public class DownloadScheduler {

    private final DownloadThread downloadThread;
    private final ConfigProvider configProvider;

    public DownloadScheduler(DownloadThread downloadThread, ConfigProvider configProvider) {

        this.downloadThread = downloadThread;
        this.configProvider = configProvider;
    }

    public void start() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                downloadThread,
                0,
                configProvider.interval,
                MINUTES);
    }
}