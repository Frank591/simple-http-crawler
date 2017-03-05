package ru.fsl.mock;

import ru.fsl.*;
import ru.fsl.implementation.SimplePageDataExtractor;
import ru.fsl.implementation.SimpleTextAnalyzer;

public class TestComponentsFactory implements CrawlerComponentsFactory {

    public PageDownloader createPageDownloader() {
        return new TestPageDownloader();
    }

    public PageDataExtractor createPageDataExtractor() {
        return new SimplePageDataExtractor();
    }

    public TextAnalyzer createTextAnalyzer() {
        return new SimpleTextAnalyzer();
    }
}
