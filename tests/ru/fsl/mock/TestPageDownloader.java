package ru.fsl.mock;

import org.jetbrains.annotations.NotNull;
import ru.fsl.PageDownloadResult;
import ru.fsl.TestUtils;
import ru.fsl.exceptions.PageDownloadException;
import ru.fsl.implementation.SimplePageDownloader;

import java.net.URL;
import java.util.Random;

public class TestPageDownloader extends SimplePageDownloader {

    public static final String UNKNOWN_URL_PREFIX = "Unknown url: ";
    public static final String HOST = "http://test.io";
    public static final String PAGE1_URL = HOST + "/test1";
    public static final String PAGE2_URL = HOST + "/test2";
    public static final String PAGE3_URL = HOST + "/test3";
    public static final String RECURSIVE_PAGE_URL = HOST + "/recursive";
    private static Random random = new Random();

    @NotNull
    @Override
    public PageDownloadResult download(@NotNull URL urlToRead) throws PageDownloadException {
        //uncomment for network delay simulation
        //try {
        //     int sleep = random.nextInt(1400) + 500;
        //      Thread.sleep(sleep);
        //   } catch (InterruptedException e) {
        //      e.printStackTrace();
        //  }
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
        } else if (url.startsWith(RECURSIVE_PAGE_URL)) {
            try {
                return new PageDownloadResult(TestUtils.readTextFromResourceFile("recursive.html"), HOST);
            } catch (Exception e) {
                throw new PageDownloadException(e);
            }
        } else {
            throw new PageDownloadException(UNKNOWN_URL_PREFIX + url);
        }
    }
}
