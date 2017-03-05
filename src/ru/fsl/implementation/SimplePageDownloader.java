package ru.fsl.implementation;

import org.jetbrains.annotations.NotNull;
import ru.fsl.PageDownloadResult;
import ru.fsl.PageDownloader;
import ru.fsl.exceptions.PageDownloadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
            StringBuilder result = new StringBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeout);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return new PageDownloadResult(result.toString(), getBaseUrl(url));
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