package ru.fsl.ForkJoinCrawler;

import ru.fsl.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


public class HttpCrawlerTask extends RecursiveTask<CrawlResult> {


    private final CrawlerComponentsFactory factory;
    private final URL url;
    private final int maxLinkDeepLevel;
    private final int currentLinkDeepLevel;

    public HttpCrawlerTask(CrawlerComponentsFactory factory, URL url, int currentLinkDeepLevel, int maxLinkDeepLevel) {
        this.factory = factory;
        this.url = url;
        this.maxLinkDeepLevel = maxLinkDeepLevel;
        this.currentLinkDeepLevel = currentLinkDeepLevel;
    }

    /**
     * Process url from constructor and after recursively process all found outgoing links from page
     * while not obtain deep level limit.
     *
     * @return crawl result for url from constructor
     */
    public CrawlResult compute() {
        PageDownloader pageDownloader = factory.createPageDownloader();
        PageDownloadResult page;
        try {
            if (currentLinkDeepLevel > maxLinkDeepLevel) {
                return CrawlResult.CreateSkippedResult(url, currentLinkDeepLevel, maxLinkDeepLevel);
            }
            //download and parse html
            page = pageDownloader.download(url);
            PageDataExtractor pageDataExtractor = factory.createPageDataExtractor();
            PageData pageData = pageDataExtractor.extract(page.getHtml());

            List<URL> childLinks = new ArrayList<>();
            for (String link : pageData.getLinks()) {
                childLinks.add(Utils.combineUrls(page.getBaseUrl(), link));
            }

            //process outgoing page links
            List<ForkJoinTask<CrawlResult>> crawlResultsByPageLinks = new ArrayList<>();
            for (URL link : childLinks) {
                final HttpCrawlerTask newCrawler = new HttpCrawlerTask(factory, link, currentLinkDeepLevel + 1, maxLinkDeepLevel);
                crawlResultsByPageLinks.add(newCrawler.fork());
            }

            //analyze page text
            TextAnalyzer textAnalyzer = factory.createTextAnalyzer();
            Map<String, Long> analyzeResult = textAnalyzer.analyze(pageData.getText());
            CrawlResult pageCrawlResult = new CrawlResult(CrawlStatus.SUCCESS,
                    url,
                    currentLinkDeepLevel,
                    null,
                    new PageAnalyzeResult(page.getHtml().length(), childLinks, analyzeResult));

            //merge
            for (ForkJoinTask<CrawlResult> childLinkCrawlTask : crawlResultsByPageLinks) {
                CrawlResult childCrawlResult = childLinkCrawlTask.join();
                pageCrawlResult.getOutgoingLinksCrawlResults().add(childCrawlResult);
            }
            return pageCrawlResult;
        } catch (Exception e) {
            return CrawlResult.CreateErrorResult(url, currentLinkDeepLevel, Utils.getStackTrace(e));
        }
    }


}
