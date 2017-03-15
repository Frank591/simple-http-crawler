package ru.fsl.ForkJoinCrawler;

import org.jetbrains.annotations.Nullable;
import ru.fsl.CrawlManager;
import ru.fsl.CrawlerComponentsFactory;
import ru.fsl.Utils;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;


public class ForkJoinCrawlManager implements CrawlManager {

    protected ForkJoinPool pool;
    protected CrawlerComponentsFactory componentsFactory;

    public ForkJoinCrawlManager(ForkJoinPool pool, CrawlerComponentsFactory componentsFactory) {

        this.pool = pool;
        this.componentsFactory = componentsFactory;
    }

    @Override
    public Map<String, Long> crawl(URL url, int maxDeepLevel) {

        HttpCrawlerTask task = new HttpCrawlerTask(componentsFactory, url, 0, maxDeepLevel);
        pool.execute(task);
        CrawlResult crawlResult = task.join();
        Set<String> test = new HashSet<>();
        Map<String, Long> topValues = Utils.getTopValues(100, Utils.sortByValue(getTotalWordsFrequency(crawlResult, test)));
        return topValues;
    }


    @Nullable
    private static Map<String, Long> getTotalWordsFrequency(CrawlResult crawlResult, Set<String> alreadyProcessedUrls) {
        if (crawlResult.getStatus() != CrawlStatus.SUCCESS) {
            return null;
        }
        Map<String, Long> result;
        if (!alreadyProcessedUrls.contains(Utils.getUrlAsString(crawlResult.getUrl()))) {
            result = crawlResult.getPageAnalyzeResult().getWordsFrequency();
            alreadyProcessedUrls.add(Utils.getUrlAsString(crawlResult.getUrl()));
        } else {
            result = new HashMap<>();
        }
        for (CrawlResult childResult : crawlResult.getOutgoingLinksCrawlResults()) {
            Map<String, Long> childWordsFrequency = getTotalWordsFrequency(childResult, alreadyProcessedUrls);
            if (childWordsFrequency == null) {
                continue;
            }
            for (Map.Entry<String, Long> wordStat : childWordsFrequency.entrySet()) {
                result.merge(wordStat.getKey(), wordStat.getValue(), (currV, newV) -> currV + newV);
            }
        }
        return result;
    }
}
