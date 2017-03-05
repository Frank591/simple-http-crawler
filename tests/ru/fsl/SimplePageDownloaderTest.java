package ru.fsl;

import org.junit.Assert;
import org.junit.Test;
import ru.fsl.implementation.SimplePageDownloader;

import java.net.URL;

public class SimplePageDownloaderTest {
    @Test
    public void getHTMLTest() throws Exception {

        SimplePageDownloader pageDownloader = new SimplePageDownloader();
        PageDownloadResult page = pageDownloader.download(new URL("http://csb.stanford.edu/class/public/pages/sykes_webdesign/05_simple.html"));
        Assert.assertTrue("Wrong page or page download error", page.getHtml().contains("A very simple webpage"));
        Assert.assertTrue("Page body was not downloaded completely", page.getHtml().contains("</body></html>"));
    }

}