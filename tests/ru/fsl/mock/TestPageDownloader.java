package ru.fsl.mock;

import org.jetbrains.annotations.NotNull;
import ru.fsl.PageDownloadResult;
import ru.fsl.TestUtils;
import ru.fsl.exceptions.PageDownloadException;
import ru.fsl.implementation.SimplePageDownloader;

import java.net.URL;

public class TestPageDownloader extends SimplePageDownloader {

    public static final String UNKNOWN_URL_PREFIX = "Unknown url: ";
    public static final String HOST = "http://test.io";
    public static final String PAGE1_URL = HOST + "/test1";
    public static final String PAGE2_URL = HOST + "/test2";
    public static final String PAGE3_URL = HOST + "/test3";

    @NotNull
    @Override
    public PageDownloadResult download(@NotNull URL urlToRead) throws PageDownloadException {
        String url = urlToRead.toString();
        if (PAGE1_URL.equalsIgnoreCase(url)) {
            try {
                return new PageDownloadResult(TestUtils.readTextFromResourceFile("test1.html"), HOST);
            } catch (Exception e) {
                throw new PageDownloadException(e);
            }
        } else if (PAGE2_URL.equalsIgnoreCase(url)) {
            try {
                return new PageDownloadResult(TestUtils.readTextFromResourceFile("test2.html"), HOST);
            } catch (Exception e) {
                throw new PageDownloadException(e);
            }
        } else if (PAGE3_URL.equalsIgnoreCase(url)) {
            try {
                return new PageDownloadResult(TestUtils.readTextFromResourceFile("test3.html"), HOST);
            } catch (Exception e) {
                throw new PageDownloadException(e);
            }
        } else {
            throw new PageDownloadException(UNKNOWN_URL_PREFIX + url);
        }
    }
}
