package ru.fsl.implementation;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.fsl.TextAnalyzer;

import java.util.HashMap;
import java.util.Map;


public class SimpleTextAnalyzer implements TextAnalyzer {

    @Override
    @NotNull
    public Map<String, Integer> analyze(@Nullable String text) {
        Map<String, Integer> result = new HashMap<>();
        if (text == null || text.trim().isEmpty()) {
            return result;
        }
        final String[] words = text.replaceAll("[\\u00A0\\.,:;\\?!'\"]", " ")
                .split(" ");
        for (String word : words) {
            String preparedWord = prepareWord(word);
            if (preparedWord.isEmpty()) {
                continue;
            }
            result.merge(preparedWord, 1, (currV, defaultV) -> currV + 1);
        }
        return result;
    }

    protected String prepareWord(String word) {
        return word.trim().toLowerCase();
    }
}
