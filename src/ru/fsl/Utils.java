package ru.fsl;

import java.net.MalformedURLException;
import java.net.URL;

public class Utils {

    public static URL combineUrls(String baseUrl, String relativePart) throws MalformedURLException {
        return new URL(new URL(baseUrl), relativePart);
    }


}
