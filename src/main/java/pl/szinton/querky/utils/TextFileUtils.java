package pl.szinton.querky.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TextFileUtils {

    public static List<String> loadFileIntoList(String resourcePath) {
        Stream<String> lines = loadFileIntoLinesStream(resourcePath);
        return lines.collect(Collectors.toList());
    }

    public static Set<String> loadFileIntoSet(String resourcePath) {
        Stream<String> lines = loadFileIntoLinesStream(resourcePath);
        return lines.collect(Collectors.toSet());
    }

    protected static Stream<String> loadFileIntoLinesStream(String resourcePath) {
        try {
            InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines();
        } catch (IOException ex) {
            log.error("Couldn't open \"{}\" resource file: {}", resourcePath, ex.getMessage());
        }
        return Stream.of();
    }
}
