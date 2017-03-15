package ru.fsl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TextAnalyzer {

    @NotNull Map<String, Long> analyze(@Nullable String text);
}
