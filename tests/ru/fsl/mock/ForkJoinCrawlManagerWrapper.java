package ru.fsl.mock;

import ru.fsl.CrawlerComponentsFactory;
import ru.fsl.ForkJoinCrawler.ForkJoinCrawlManager;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinCrawlManagerWrapper extends ForkJoinCrawlManager {
    public ForkJoinCrawlManagerWrapper(ForkJoinPool pool, CrawlerComponentsFactory componentsFactory) {
        super(pool, componentsFactory);
    }

    @Override
    public String toString() {
        return "ForkJoinCrawlManager_" + pool.getParallelism();
    }
}
