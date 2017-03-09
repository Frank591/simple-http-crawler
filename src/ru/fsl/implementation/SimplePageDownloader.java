package ru.fsl.implementation;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.fsl.PageDownloadResult;
import ru.fsl.PageDownloader;
import ru.fsl.exceptions.PageDownloadException;

import java.net.URL;

public class SimplePageDownloader implements PageDownloader {

    private int readTimeout = 0;

    public SimplePageDownloader(int readTimeout) {
        if (readTimeout < 0) {
            throw new IllegalArgumentException("readTimeout parameter is negative");
        }
        this.readTimeout = readTimeout;
    }

    public SimplePageDownloader() {
    }

    @NotNull
    @Override
    public PageDownloadResult download(@NotNull URL url) throws PageDownloadException {
        try {
            Connection connect = Jsoup.connect(url.toString());
            Document doc = connect.timeout(readTimeout).get();
            String rawHtml = doc.html();
            return new PageDownloadResult(rawHtml, getBaseUrl(url));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageDownloadException(e);
        }
    }


    private static String getBaseUrl(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url.getProtocol());
        stringBuilder.append("://");
        stringBuilder.append(url.getHost());
        if (url.getPort() > 0) {
            stringBuilder.append(":");
            stringBuilder.append(url.getPort());
        }
        return stringBuilder.toString();
    }
}