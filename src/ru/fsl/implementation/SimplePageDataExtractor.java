package ru.fsl.implementation;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.fsl.PageData;
import ru.fsl.PageDataExtractor;


public class SimplePageDataExtractor implements PageDataExtractor {

    @NotNull
    @Override
    public PageData extract(@NotNull String rawPageHtml) {

        Document doc = Jsoup.parse(rawPageHtml);
        final StringBuilder result = new StringBuilder();
        Elements allElements = doc.getAllElements();
        //get text
        for (Element el : allElements) {
            String elOwnText = el.ownText();
            if (elOwnText != null && !elOwnText.isEmpty()) {
                result.append(elOwnText);
                result.append(" ");
            }
        }
        PageData pageData = new PageData(result.toString().trim());
        //get links
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("href").toLowerCase();
            if (href.startsWith("//")) {
                continue;
            }
            if (href.contains(".jpg")) {
                continue;
            }
            if (href.contains(".jpeg")) {
                continue;
            }
            if (href.contains(".png")) {
                continue;
            }
            if (href.contains(".svg")) {
                continue;
            }
            if (href.startsWith("/")) {
                pageData.addLink(href);
            }
        }
        return pageData;
    }
}
