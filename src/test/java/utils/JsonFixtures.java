package utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class JsonFixtures {
    public static String read(String filename) throws IOException {
        InputStream is = JsonFixtures.class.getClassLoader().getResourceAsStream(filename);
        return IOUtils.toString(is);
    }
}
