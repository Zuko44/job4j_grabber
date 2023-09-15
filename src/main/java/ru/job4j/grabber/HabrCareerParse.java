package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class HabrCareerParse {
    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    private static final int PAGES = 5;

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + "?page=" + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element published = row.select(".vacancy-card__date").get(0);
                String publishedTwo = published.child(0).attr("datetime");
                HabrCareerParse parse = new HabrCareerParse();
                System.out.printf("Вакансия - %s, дата публикации - %s, %s,%nописание: %s%n%n",
                        vacancyName, publishedTwo, link, parse.retrieveDescription(link));
            });
        }
    }

    private String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements rows = document.select(".vacancy-description__text");
        StringJoiner joiner = new StringJoiner("\n");
        rows.forEach(row -> joiner.add(row.text()));
        return joiner.toString();
    }
}
