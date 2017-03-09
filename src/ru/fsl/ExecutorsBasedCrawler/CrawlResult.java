package ru.fsl.ExecutorsBasedCrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class CrawlResult {
    private URL url;
    private List<URL> childLinks;
    private Map<String, Integer> pageAnalyzeResult;
    private int currentDeepLevel;
    private long pageCharsCount;


    public CrawlResult(@NotNull URL url, long pageCharsCount, @NotNull List<URL> childLinks, @NotNull Map<String, Integer> pageAnalyzeResult, int currentDeepLevel) {
        this.url = url;
        this.pageCharsCount = pageCharsCount;
        this.childLinks = childLinks;
        this.pageAnalyzeResult = pageAnalyzeResult;
        this.currentDeepLevel = currentDeepLevel;
    }

    @NotNull
    public Iterable<URL> getChildLinks() {
        return childLinks;
    }

    @NotNull
    public Map<String, Integer> getPageAnalyzeResult() {
        return pageAnalyzeResult;
    }

    public int getCurrentDeepLevel() {
        return currentDeepLevel;
    }

    public URL getUrl() {
        return url;
    }

    public long getPageCharsCount() {
        return pageCharsCount;
    }
}
