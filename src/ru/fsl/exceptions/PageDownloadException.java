package ru.fsl.exceptions;

public class PageDownloadException extends Exception {

    public PageDownloadException(Exception e) {
        super(e);
    }

    public PageDownloadException(String msg) {
        super(msg);
    }

}
