package ru.fsl.ForkJoinCrawler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CrawlResult {

    private CrawlStatus status;
    private URL url;
    private int deepLevel;
    private String systemMessage;
    private PageAnalyzeResult pageAnalyzeResult;
    private List<CrawlResult> outgoingLinksCrawlResults;

    public CrawlResult(CrawlStatus status, @NotNull URL url, int deepLevel, @Nullable String systemMessage, @Nullable PageAnalyzeResult pageAnalyzeResult) {
        if (status == CrawlStatus.SUCCESS && pageAnalyzeResult == null) {
            throw new IllegalArgumentException("pageAnalyzeResult can't be null with success status");
        }
        this.status = status;
        this.url = url;
        this.deepLevel = deepLevel;
        this.systemMessage = systemMessage;
        this.pageAnalyzeResult = pageAnalyzeResult;
        this.outgoingLinksCrawlResults = new ArrayList<>();
    }

    public CrawlStatus getStatus() {
        return status;
    }

    @NotNull
    public URL getUrl() {
        return url;
    }

    public int getDeepLevel() {
        return deepLevel;
    }

    @Nullable
    public String getSystemMessage() {
        return systemMessage;
    }

    @Nullable
    public PageAnalyzeResult getPageAnalyzeResult() {
        return pageAnalyzeResult;
    }

    public static CrawlResult CreateErrorResult(URL url, int deepLevel, String errorMsg) {
        return new CrawlResult(CrawlStatus.ERROR, url, deepLevel, errorMsg, null);
    }

    public static CrawlResult CreateSkippedResult(URL url, int deepLevel, int maxDeepLevel) {
        return new CrawlResult(CrawlStatus.SKIP_BECAUSE_TOO_DEEP, url, deepLevel, String.format("Curr deep lvl=%s, max deep lvl=%s. Skipped.", deepLevel, maxDeepLevel), null);
    }

    @NotNull
    public List<CrawlResult> getOutgoingLinksCrawlResults() {
        return outgoingLinksCrawlResults;
    }
}


