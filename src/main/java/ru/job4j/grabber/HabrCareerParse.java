package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
                /** edited time */
                Element published = row.select(".vacancy-card__date").get(0);
                String publishedTwo = published.child(0).attr("datetime");
                System.out.printf("Вакансия - %s, дата публикации - %s, %s%n", vacancyName, publishedTwo, link);
                /**try {
                 System.out.println(retrieveDescription(link));
                 } catch (IOException e) {
                 throw new RuntimeException(e);
                 }*/
            });
        }
    }

    private String retrieveDescription(String link) throws IOException {
        /**try {
         URL url = new URL(link);
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection connection = null;
         if(urlConnection instanceof HttpURLConnection) {
         connection = (HttpURLConnection) urlConnection;
         }else {
         //System.out.println("Пожалуйста, введите HTTP URL.");
         return "Пожалуйста, введите HTTP URL.";
         }
         BufferedReader in = new BufferedReader(
         new InputStreamReader(connection.getInputStream()));
         String urlString = "";
         String current;

         while ((current = in.readLine()) != null) {
         urlString += current;
         }
         return urlString;
         } catch (IOException e) {
         throw new RuntimeException(e);
         }*/
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-description__text");
        StringJoiner joiner = new StringJoiner("");
        rows.forEach(row -> joiner.add(row.text()));
        return joiner.toString();
    }
}
