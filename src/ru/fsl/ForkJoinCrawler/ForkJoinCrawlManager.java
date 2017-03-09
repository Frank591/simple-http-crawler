package ru.fsl.ForkJoinCrawler;

import ru.fsl.CrawlManager;
import ru.fsl.CrawlerComponentsFactory;
import ru.fsl.Utils;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;


public class ForkJoinCrawlManager implements CrawlManager {

    protected ForkJoinPool pool;
    protected CrawlerComponentsFactory componentsFactory;

    public ForkJoinCrawlManager(ForkJoinPool pool, CrawlerComponentsFactory componentsFactory) {

        this.pool = pool;
        this.componentsFactory = componentsFactory;
    }

    @Override
    public Map<String, Integer> crawl(URL url, int maxDeepLevel) {
        HttpCrawlerTask task = new HttpCrawlerTask(componentsFactory, url, 0, maxDeepLevel);
        pool.execute(task);
        Map<String, Integer> crawlResult = task.join();
        return Utils.getTopValues(100, Utils.sortByValue(crawlResult));
    }
}
