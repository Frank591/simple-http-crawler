package ru.fsl;

import org.junit.Assert;
import org.junit.Test;
import ru.fsl.implementation.SimplePageDataExtractor;

import java.util.Iterator;


public class PageDataExtractorTest {

    private static PageData extractAndCompareText(String htmlFileName, String expectedText) throws Exception {
        String html = TestUtils.readTextFromResourceFile(htmlFileName);
        PageDataExtractor pageDataExtractor = new SimplePageDataExtractor();
        PageData res = pageDataExtractor.extract(html);
        TestUtils.assertEquals(String.format("Text from page '%s'", htmlFileName), expectedText, res.getText());
        return res;
    }

    @Test
    public void extractFromHtmlTest() throws Exception {
        String expectedResText = "Title test1 test2 test3 test4 test5 test6 test7";
        extractAndCompareText("html1.html", expectedResText);
    }

    @Test
    public void extractFromTextTest() throws Exception {
        String expectedResText = "Title test1 test2 test3 test4 test5 test6 test7";
        extractAndCompareText("textOnly.html", expectedResText);
    }

    @Test
    public void extractFromNotCompletedHtmlTest() throws Exception {
        String expectedResText = "Title test1 test2 test3 test4 test5 test6 test7";
        extractAndCompareText("notCompletedHtml1.html", expectedResText);
    }

    @Test
    public void extractFromEmptyTest() throws Exception {
        String expectedResText = "";
        extractAndCompareText("empty.html", expectedResText);
    }

    @Test
    public void extractFromHtmlWithLinksTest() throws Exception {
        String expectedResText = "test1 test2 test3 test4";
        PageData pageData = extractAndCompareText("htmlWithLinks.html", expectedResText);
        Iterator<String> iterator = pageData.getLinks().iterator();
        String firstLink = iterator.next();
        Assert.assertTrue("/test".equals(firstLink));
        Assert.assertFalse(iterator.hasNext());
    }


}