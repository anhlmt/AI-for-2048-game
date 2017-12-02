package com.Game2048.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {

	public static String readFile(URL url) {
		try {
			byte[] data = Files.readAllBytes(Paths.get(url.toURI()));
			return new String(data, 0, data.length, "utf-8");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeFile(URL url, String content) {
		try {
			Files.write(Paths.get(url.toURI()), content.getBytes("utf-8"), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
