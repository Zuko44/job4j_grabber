package ru.job4j.grabber;

import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException, ClassNotFoundException {
        Class.forName(cfg.getProperty("driver_class"));
        String url = cfg.getProperty("url");
        String login = cfg.getProperty("username");
        String password = cfg.getProperty("password");
        cnn = DriverManager.getConnection(url, login, password);
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            prop.load(in);
            PsqlStore store = new PsqlStore(prop);
            DateTimeParser formatter = new HabrCareerDateTimeParser();
            Post slave = new Post("slave", "fucking slave, go to work!",
                    "www.slaveMarket.ru/vacancies/java_developer", formatter.parse(LocalDateTime.now().toString()));
            Post manager = new Post("manager",
                    "O holy manager, come to us and strike us with your holy whip",
                    "www.managerMarket.ru/vacancies/holy_manager", formatter.parse(LocalDateTime.now().toString()));
            System.out.println(slave);
            store.save(slave);
            store.save(manager);
            List<Post> glist = store.getAll();
            for (Post post : glist) {
                System.out.println(post);
            }
            System.out.println(store.findById(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement prep = cnn.prepareStatement(
                "insert into post(name, description, link, created) values(?, ?, ?, ?) ON CONFLICT (link) DO NOTHING;"
        )) {
            prep.setString(1, post.getTitle());
            prep.setString(2, post.getDescription());
            prep.setString(3, post.getLink());
            prep.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            prep.execute();
            try (ResultSet generatedKeys = prep.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(generatePost(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Post generatePost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement = cnn.prepareStatement("SELECT * FROM post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    post = generatePost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
