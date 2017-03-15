package ru.fsl;

import java.net.URL;
import java.util.Map;

public interface CrawlManager {
    Map<String, Long> crawl(URL url, int maxDeepLevel);
}
