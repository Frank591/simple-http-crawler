package ru.fsl.ForkJoinCrawler;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.Map;


public class PageAnalyzeResult {

    private List<URL> outgoingLinks;
    private Map<String, Long> wordsFrequency;
    private long htmlCharsCount;


    public PageAnalyzeResult(long htmlCharsCount, @NotNull List<URL> outgoingLinks, @NotNull Map<String, Long> wordFrequency) {

        this.htmlCharsCount = htmlCharsCount;
        this.outgoingLinks = outgoingLinks;
        this.wordsFrequency = wordFrequency;
    }


    public List<URL> getOutgoingLinks() {
        return outgoingLinks;
    }

    @NotNull
    public Map<String, Long> getWordsFrequency() {
        return wordsFrequency;
    }

    public long getHtmlCharsCount() {
        return htmlCharsCount;
    }
}
