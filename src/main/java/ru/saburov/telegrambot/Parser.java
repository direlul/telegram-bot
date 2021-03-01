package ru.saburov.telegrambot;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Data
public class Parser {
    private final String PATH_FILE = "src/main/resources/current_url";

    private String currentUrl;
    private File file;

    public Parser() {
        file = new File(PATH_FILE);
        currentUrl = readUrlFromFile();
    }

    public String scanNextChapter() {
        try {
            Document page = Jsoup.connect(currentUrl).get();
            Element el = page.select("li.next > a[href]").first();
            if (el != null) {
                String nextChapterUrl = el.attr("href");
                currentUrl = nextChapterUrl;
                writeNewUrl(nextChapterUrl);

                return nextChapterUrl;
            }
        } catch (IOException e) {
            System.out.println("Сайт не доступен");
        }

        return null;
    }

    public List<String> checkNewChapters() {
        List<String> newChapters = new ArrayList<>();
        String url;

        while ((url = scanNextChapter()) != null) {
            newChapters.add(url.replaceAll("[^0-9]", ""));
        }

        return newChapters;
    }

    public String readUrlFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeNewUrl(String newUrl) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(newUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
