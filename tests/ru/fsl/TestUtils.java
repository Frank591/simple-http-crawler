package ru.fsl;

import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class TestUtils {
    public static String readTextFromResourceFile(String resourceFileName) throws URISyntaxException, IOException {
        java.net.URL url = TestUtils.class.getResource("/resources/" + resourceFileName);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
    }

    public static String getErrorMessage(String objectDescription, String expected, String actual) {
        return String.format("%s - expected: '%s', actual:'%s'.", objectDescription, expected, actual);
    }

    public static void assertEquals(String objectDescription, long expected, long actual) {
        Assert.assertTrue(TestUtils.getErrorMessage(objectDescription, Long.toString(expected), Long.toString(actual)), expected == actual);
    }

    public static void assertEquals(String objectDescription, String expected, String actual) {
        Assert.assertTrue(TestUtils.getErrorMessage(objectDescription, expected, actual), Objects.equals(expected, actual));
    }
}
