package ru.fsl;

import ru.fsl.exceptions.PageDownloadException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


public class HttpCrawlerTask extends RecursiveTask<Map<String, Integer>> {


    private final CrawlerComponentsFactory factory;
    private final URL url;
    private final int maxLinkDeepLevel;
    private final int currentLinkDeepLevel;
    private final Set<String> alreadyProcessedUrls;

    public HttpCrawlerTask(CrawlerComponentsFactory factory, URL url, int currentLinkDeepLevel, int maxLinkDeepLevel) {
        this.factory = factory;
        this.url = url;
        this.maxLinkDeepLevel = maxLinkDeepLevel;
        this.currentLinkDeepLevel = currentLinkDeepLevel;
        alreadyProcessedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    private HttpCrawlerTask(CrawlerComponentsFactory factory, URL url, int currentLinkDeepLevel, int maxLinkDeepLevel, Set<String> alreadyProcessedUrls) {
        this.factory = factory;
        this.url = url;
        this.maxLinkDeepLevel = maxLinkDeepLevel;
        this.currentLinkDeepLevel = currentLinkDeepLevel;
        this.alreadyProcessedUrls = alreadyProcessedUrls;
    }

    public Map<String, Integer> compute() {
        PageDownloader pageDownloader = factory.createPageDownloader();
        PageDownloadResult page = null;
        try {
            page = pageDownloader.download(url);
        } catch (PageDownloadException e) {
            throw new RuntimeException(e);
        }

        PageDataExtractor pageDataExtractor = factory.createPageDataExtractor();
        PageData pageData = pageDataExtractor.extract(page.getHtml());

        //process child page links
        List<ForkJoinTask<Map<String, Integer>>> crawlResultsByPageLinks = new ArrayList<>();
        if (currentLinkDeepLevel < maxLinkDeepLevel) {
            for (String link : pageData.getLinks()) {
                URL linkUrl = null;
                try {
                    linkUrl = Utils.combineUrls(page.getBaseUrl(), link);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                if (alreadyProcessedUrls.contains(linkUrl.toString().toLowerCase())) {
                    System.out.println(String.format("Link to page '%s' from page '%s' was skipped, because linked page was already processed.", linkUrl.toString(), url.toString()));
                    continue;
                }
                HttpCrawlerTask newCrawler = new HttpCrawlerTask(factory, linkUrl, currentLinkDeepLevel + 1, maxLinkDeepLevel, alreadyProcessedUrls);
                alreadyProcessedUrls.add(linkUrl.toString());
                crawlResultsByPageLinks.add(newCrawler.fork());
            }
        }

        //analyze page text
        TextAnalyzer textAnalyzer = factory.createTextAnalyzer();
        Map<String, Integer> analyzeResult = textAnalyzer.analyze(pageData.getText());

        //merge page text analyze result and child pages analyze results
        for (ForkJoinTask<Map<String, Integer>> childLinkCrawlTask : crawlResultsByPageLinks) {
            Map<String, Integer> childCrawlResult;
            try {
                childCrawlResult = childLinkCrawlTask.join();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            for (Map.Entry<String, Integer> childRes : childCrawlResult.entrySet()) {
                analyzeResult.merge(childRes.getKey(), 1, (currV, defaultV) -> currV + childRes.getValue());
            }
        }
        return analyzeResult;
    }
}
