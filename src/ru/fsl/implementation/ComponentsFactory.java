package ru.fsl.implementation;

import ru.fsl.*;

public class ComponentsFactory implements CrawlerComponentsFactory {

    private final int READ_TIMEOUT_MS = 3000;

    public PageDownloader createPageDownloader() {
        return new SimplePageDownloader(READ_TIMEOUT_MS);
    }

    public PageDataExtractor createPageDataExtractor() {
        return new SimplePageDataExtractor();
    }

    public TextAnalyzer createTextAnalyzer() {
        return new SimpleTextAnalyzer();
    }
}
