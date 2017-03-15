package ru.fsl.ExecutorsBasedCrawler;

import ru.fsl.CrawlManager;
import ru.fsl.CrawlerComponentsFactory;
import ru.fsl.Utils;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


@Deprecated
//Problems with lost page analyze results available, if different link levels contains same link
//TODO fix
public class ExecutorsBasedCrawlManager implements CrawlManager {

    private static final int SLEEP_DURATION_BETWEEN_ITERATIONS_MS = 500;

    protected ExecutorService executorService;
    protected CrawlerComponentsFactory factory;
    private final Set<String> alreadyProcessedUrls;
    private LinkedHashMap<String, Long> pageTotalCharsCount = new LinkedHashMap<>();
    private boolean isDebugEnabled;


    public ExecutorsBasedCrawlManager(ExecutorService executorService, CrawlerComponentsFactory factory, boolean isDebugEnabled) {
        alreadyProcessedUrls = new HashSet<>();
        this.executorService = executorService;
        this.factory = factory;
        this.isDebugEnabled = isDebugEnabled;
    }

    @Override
    public Map<String, Long> crawl(URL url, int maxDeepLevel) {
        if (maxDeepLevel < 0) {
            throw new IllegalArgumentException("maxDeepLevel can't be negative");
        }
        alreadyProcessedUrls.clear();
        Map<String, Long> result = new HashMap<>();
        List<Future<CrawlResult>> crawlTasks = new LinkedList<>();

        HttpCrawlerCallable crawler = new HttpCrawlerCallable(factory, url, 0);
        alreadyProcessedUrls.add(Utils.getUrlAsString(url));
        Future<CrawlResult> crawlResult = executorService.submit(crawler);
        crawlTasks.add(crawlResult);

        while (!crawlTasks.isEmpty()) {
            Iterator<Future<CrawlResult>> tasksIterator = crawlTasks.iterator();
            List<Future<CrawlResult>> childTasks = new ArrayList<>();
            while (tasksIterator.hasNext()) {
                Future<CrawlResult> task = tasksIterator.next();
                if (!task.isDone()) {
                    continue;
                }
                try {
                    CrawlResult taskResult = null;
                    try {
                        taskResult = task.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    //merge results
                    for (Map.Entry<String, Long> wordStat : taskResult.getPageAnalyzeResult().entrySet()) {
                        result.merge(wordStat.getKey(), wordStat.getValue(), (currV, newV) -> currV + newV);
                    }
                    if (isDebugEnabled) {
                        pageTotalCharsCount.merge(Utils.getUrlAsString(taskResult.getUrl()), taskResult.getPageCharsCount(), (currV, newV) -> currV + newV);
                    }

                    //stop process links if maxdeeplvl was reached
                    int nextDeepLvl = taskResult.getCurrentDeepLevel() + 1;
                    if (nextDeepLvl > maxDeepLevel) {
                        continue;
                    }

                    for (URL link : taskResult.getChildLinks()) {
                        //skip already processed urls
                        if (alreadyProcessedUrls.contains(Utils.getUrlAsString(link))) {
                            continue;
                        }
                        HttpCrawlerCallable childCrawler = new HttpCrawlerCallable(factory, link, nextDeepLvl);
                        alreadyProcessedUrls.add(Utils.getUrlAsString(link));
                        Future<CrawlResult> childCrawlResult = executorService.submit(childCrawler);
                        childTasks.add(childCrawlResult);
                    }
                } finally {
                    tasksIterator.remove();
                }
            }
            crawlTasks.addAll(childTasks);
            try {
                Thread.sleep(SLEEP_DURATION_BETWEEN_ITERATIONS_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isDebugEnabled) {
            long totalCharsCount = 0;
            for (Map.Entry<String, Long> pageStats : pageTotalCharsCount.entrySet()) {
                System.out.println(String.format("page  '%s' - %s chars", pageStats.getKey(), pageStats.getValue()));
                totalCharsCount = totalCharsCount + pageStats.getValue();
            }
            System.out.println("Total chars count - " + totalCharsCount);
        }

        return Utils.getTopValues(100, Utils.sortByValue(result));
    }


}
