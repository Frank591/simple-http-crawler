package ru.fsl;

import org.jetbrains.annotations.NotNull;

public interface PageDataExtractor {

    @NotNull
    PageData extract(@NotNull String rawPageHtml);
}
