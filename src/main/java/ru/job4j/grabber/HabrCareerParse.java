package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    private static final int PAGES = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    /**
     * public static void main(String[] args) throws IOException {
     * HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
     * List<Post> list = parse.list(SOURCE_LINK + PAGE_LINK);
     * for (Post post : list) {
     * System.out.println(post);
     * }
     * }
     */

    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= PAGES; i++) {
            Connection connection = Jsoup.connect(link + "?page=" + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> list.add(createPost(row)));
        }
        return list;
    }

    private Post createPost(Element element) {
        Post post = new Post();
        Element titleElement = element.select(".vacancy-card__title").first();
        post.setTitle(titleElement.text());
        Element linkElement = titleElement.child(0);
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        post.setLink(link);
        post.setDescription(retrieveDescription(link));
        Element published = element.select(".vacancy-card__date").get(0);
        String publishedTwo = published.child(0).attr("datetime");
        post.setCreated(dateTimeParser.parse(publishedTwo));
        return post;
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
        StringBuilder build = new StringBuilder();
        rows.forEach(row -> {
            build.append(row.text());
            build.append(System.lineSeparator());
        });
        return build.toString();
    }
}
