package ru.fsl.mock;

import ru.fsl.CrawlerComponentsFactory;
import ru.fsl.ExecutorsBasedCrawler.ExecutorsBasedCrawlManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorsBasedCrawlManagerWrapper extends ExecutorsBasedCrawlManager {
    public ExecutorsBasedCrawlManagerWrapper(ExecutorService executorService, CrawlerComponentsFactory factory) {
        super(executorService, factory, true);
    }

    @Override
    public String toString() {
        String poolSize = "";
        if (executorService instanceof ThreadPoolExecutor) {
            poolSize = poolSize + "_" + ((ThreadPoolExecutor) executorService).getCorePoolSize();
        }
        return "FBCManager_" + executorService.getClass().getSimpleName() + poolSize;
    }
}
