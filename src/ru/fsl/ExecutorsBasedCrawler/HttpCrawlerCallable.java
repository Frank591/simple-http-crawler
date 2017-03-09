package ru.fsl.ExecutorsBasedCrawler;

import ru.fsl.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class HttpCrawlerCallable implements Callable<CrawlResult> {


    private final CrawlerComponentsFactory factory;
    private final URL url;
    private final int currentLinkDeepLevel;

    public HttpCrawlerCallable(CrawlerComponentsFactory factory, URL url, int currentLinkDeepLevel) {
        this.factory = factory;
        this.url = url;
        this.currentLinkDeepLevel = currentLinkDeepLevel;
    }

    public CrawlResult call() throws Exception {
        PageDownloader pageDownloader = factory.createPageDownloader();
        PageDownloadResult page = null;
        page = pageDownloader.download(url);

        PageDataExtractor pageDataExtractor = factory.createPageDataExtractor();
        PageData pageData = pageDataExtractor.extract(page.getHtml());

        //analyze page text
        TextAnalyzer textAnalyzer = factory.createTextAnalyzer();
        Map<String, Integer> analyzeResult = textAnalyzer.analyze(pageData.getText());
        List<URL> childLinks = new ArrayList<>();
        for (String link : pageData.getLinks()) {
            childLinks.add(Utils.combineUrls(page.getBaseUrl(), link));
        }
        return new CrawlResult(url, page.getHtml().length(), childLinks, analyzeResult, currentLinkDeepLevel);
    }

}
