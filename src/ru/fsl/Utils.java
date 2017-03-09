package ru.fsl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static URL combineUrls(String baseUrl, String relativePart) throws MalformedURLException {
        return new URL(new URL(baseUrl), relativePart);
    }

    public static String getUrlAsString(URL url) {
        return url.toString().toLowerCase();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public static <K, V> Map<K, V> getTopValues(int topValuesNum, Map<K, V> source) {
        int i = 0;
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : source.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
            i++;
            if (i == topValuesNum) {
                return result;
            }
        }
        return result;
    }

}
