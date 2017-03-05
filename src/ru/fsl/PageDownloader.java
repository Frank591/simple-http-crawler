package ru.fsl;

import org.jetbrains.annotations.NotNull;
import ru.fsl.exceptions.PageDownloadException;

import java.net.URL;

public interface PageDownloader {

    @NotNull
    PageDownloadResult download(@NotNull URL urlToRead) throws PageDownloadException;
}
