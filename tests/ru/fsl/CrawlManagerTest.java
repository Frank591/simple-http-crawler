package ru.fsl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.fsl.mock.ExecutorsBasedCrawlManagerWrapper;
import ru.fsl.mock.ForkJoinCrawlManagerWrapper;
import ru.fsl.mock.TestComponentsFactory;
import ru.fsl.mock.TestPageDownloader;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@RunWith(Parameterized.class)
public class CrawlManagerTest {

    private CrawlManager crawlManager;

    public CrawlManagerTest(CrawlManager crawlManager) {
        this.crawlManager = crawlManager;
    }

    @Test
    public void deep0Test() throws Exception {
        Map<String, Long> result = crawlManager.crawl(new URL(TestPageDownloader.PAGE1_URL), 0);
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 2, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 7, result.size());
    }

    @Test
    public void deep1Test() throws Exception {
        Map<String, Long> result = crawlManager.crawl(new URL(TestPageDownloader.PAGE1_URL), 1);
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 5, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 11, result.size());
    }

    @Test
    public void deep2Test() throws Exception {
        Map<String, Long> result = crawlManager.crawl(new URL(TestPageDownloader.PAGE1_URL), 2);
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 5, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 11, result.size());
    }

    @Test
    public void deep0RecursiveTest() throws Exception {
        Map<String, Long> result = crawlManager.crawl(new URL(TestPageDownloader.RECURSIVE1_PAGE_URL), 0);
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("test".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'test' count", 8640, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 2, result.size());
    }


    @Test
    public void deep4RecursiveTest() throws Exception {
        Map<String, Long> result = crawlManager.crawl(new URL(TestPageDownloader.RECURSIVE1_PAGE_URL), 4);
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("test".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'test' count", 69120, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 2, result.size());
    }

    @Parameterized.Parameters(name = "{index}: crawlManager = {0}")
    public static Collection parallelismLevels() {
        return Arrays.asList(new Object[]{
                new ExecutorsBasedCrawlManagerWrapper(Executors.newSingleThreadExecutor(), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newCachedThreadPool(), new TestComponentsFactory()),
                //multiple identical tests - try to find races
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),
                new ExecutorsBasedCrawlManagerWrapper(Executors.newFixedThreadPool(10), new TestComponentsFactory()),

                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(1), new TestComponentsFactory()),
                //multiple identical tests - try to find races
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),
                new ForkJoinCrawlManagerWrapper(new ForkJoinPool(10), new TestComponentsFactory()),

        });
    }

    private static void printWordCount(String word, long count) {
        System.out.println(String.format("Word: '%s', count: %s", word, count));
    }

}