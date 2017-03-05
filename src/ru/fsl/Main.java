package ru.fsl;

import ru.fsl.implementation.ComponentsFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

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
            ForkJoinPool pool = new ForkJoinPool();
            HttpCrawlerTask task = new HttpCrawlerTask(new ComponentsFactory(), new URL(url), 0, maxDeepLvl);
            long startMillis = System.currentTimeMillis();
            try {
                pool.execute(task);
                Map<String, Integer> crawlResult = task.join();
                if (crawlResult.size() == 0) {
                    System.out.println(String.format("Page '%s' is empty", url));
                    return;
                }
                int i = 0;
                for (Map.Entry<String, Integer> wordInfo : sortByValue(crawlResult).entrySet()) {
                    if (i >= 100) {
                        return;
                    }
                    System.out.println(String.format("Word '%s' : %s", wordInfo.getKey(), wordInfo.getValue()));
                    i++;
                }
            } finally {
                long finishMillis = System.currentTimeMillis();
                System.out.println("Crawl time, sec: " + (finishMillis - startMillis) / 1000);
            }
        } finally {
            System.out.println("Press 'Enter' for exit...");
            br.readLine();
        }
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
