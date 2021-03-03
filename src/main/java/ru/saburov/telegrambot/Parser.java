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

    private DAO dao;
    private String currentUrl;

    public Parser(DAO dao) {
        this.dao = dao;
        currentUrl = dao.getCurrentUrl();

    }

    public String scanNextChapter() {
        try {
            Document page = Jsoup.connect(currentUrl).get();
            Element el = page.select("li.next > a[href]").first();
            if (el != null) {
                String nextChapterUrl = el.attr("href");
                currentUrl = nextChapterUrl;
                dao.updateCurrentUrl(nextChapterUrl);

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
}
