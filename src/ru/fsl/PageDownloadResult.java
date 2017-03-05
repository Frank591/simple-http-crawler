package ru.fsl;


import org.jetbrains.annotations.NotNull;

public class PageDownloadResult {

    private String html;
    private String baseUrl;

    public PageDownloadResult(String html, String baseUrl) {

        this.html = html;
        this.baseUrl = baseUrl;
    }

    @NotNull
    public String getHtml() {
        return html;
    }

    @NotNull
    public String getBaseUrl() {
        return baseUrl;
    }

}
