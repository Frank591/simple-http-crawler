package ru.fsl;

import java.util.LinkedList;
import java.util.List;

public class PageData {

    private String pageText;
    private List<String> links = new LinkedList<>();

    public PageData(String pageText) {

        this.pageText = pageText;
    }

    /**
     * @return Text from page
     */
    public String getText() {
        return pageText;
    }

    public void addLink(String linkUrl) {
        links.add(linkUrl);
    }

    public Iterable<String> getLinks() {
        return links;
    }
}
