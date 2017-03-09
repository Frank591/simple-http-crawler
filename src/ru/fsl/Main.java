package ru.fsl;

import ru.fsl.ExecutorsBasedCrawler.ExecutorsBasedCrawlManager;
import ru.fsl.implementation.ComponentsFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Url = ");
            String url;
            url = br.readLine();
            int maxDeepLvl;
            System.out.print("maxDeepLvl = ");
            maxDeepLvl = Integer.parseInt(br.readLine());

            System.out.println(String.format("Running crawler with parameters [Url=%s, maxDeepLvl=%s]", url, maxDeepLvl));
            CrawlManager crawlManager = new ExecutorsBasedCrawlManager(Executors.newFixedThreadPool(8), new ComponentsFactory(), true);

            long startMillis = System.currentTimeMillis();
            try {
                final Map<String, Integer> crawlResults = crawlManager.crawl(new URL(url), maxDeepLvl);
                for (Map.Entry<String, Integer> wordInfo : crawlResults.entrySet()) {
                    System.out.println(String.format("Word '%s' : %s", wordInfo.getKey(), wordInfo.getValue()));
                }
            } finally {
                long finishMillis = System.currentTimeMillis();
                System.out.println("Crawl time, sec: " + (finishMillis - startMillis) / 1000);
            }
        } finally {
            System.out.println("Press 'Enter' for exit...");
            br.readLine();
            System.exit(0);
        }
    }

}
