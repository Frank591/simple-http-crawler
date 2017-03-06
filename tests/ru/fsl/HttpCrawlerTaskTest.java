package ru.fsl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.fsl.mock.TestComponentsFactory;
import ru.fsl.mock.TestPageDownloader;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@RunWith(Parameterized.class)
public class HttpCrawlerTaskTest {

    private int parallelism = 1;

    public HttpCrawlerTaskTest(int parallelism) {
        this.parallelism = parallelism;
    }

    @Test
    public void deep0Test() throws Exception {
        Map<String, Integer> result = getCrawlResult(TestPageDownloader.PAGE1_URL, 0, parallelism);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 2, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 7, result.size());
    }

    @Test
    public void deep1Test() throws Exception {
        Map<String, Integer> result = getCrawlResult(TestPageDownloader.PAGE1_URL, 1, parallelism);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 5, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 11, result.size());
    }

    @Test
    public void deep2Test() throws Exception {
        Map<String, Integer> result = getCrawlResult(TestPageDownloader.PAGE1_URL, 2, parallelism);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("text".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'text' count", 5, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 11, result.size());
    }

    @Test
    public void deep2RecursiveTest() throws Exception {
        Map<String, Integer> result = getCrawlResult(TestPageDownloader.RECURSIVE_PAGE_URL, 2, parallelism);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            printWordCount(entry.getKey(), entry.getValue());
            if ("test".equals(entry.getKey())) {
                TestUtils.assertEquals("word 'test' count", 181440, entry.getValue());
            }
        }
        TestUtils.assertEquals("result size", 2, result.size());
    }


    @Parameterized.Parameters(name = "{index}: parallelismLevel = {0}")
    public static Collection parallelismLevels() {
        return Arrays.asList(new Object[]{1, 2, 10});
    }


    private static Map<String, Integer> getCrawlResult(String url, int maxDeepLvl, int parallelism) throws Exception {
        ForkJoinPool pool = new ForkJoinPool(parallelism);
        CrawlerComponentsFactory componentsFactory = new TestComponentsFactory();

        HttpCrawlerTask crawler = new HttpCrawlerTask(componentsFactory, new URL(url), 0, maxDeepLvl);
        pool.execute(crawler);
        return crawler.join();
    }

    private static void printWordCount(String word, int count) {
        System.out.println(String.format("Word: '%s', count: %s", word, count));
    }

}