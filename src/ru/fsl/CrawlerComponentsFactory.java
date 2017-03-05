package ru.fsl;

public interface CrawlerComponentsFactory {

    PageDownloader createPageDownloader();

    PageDataExtractor createPageDataExtractor();

    TextAnalyzer createTextAnalyzer();
}
