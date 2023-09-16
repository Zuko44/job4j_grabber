package ru.job4j.grabber;

import java.sql.SQLException;
import java.util.List;

public interface Store extends AutoCloseable {
    void save(Post post) throws SQLException;

    List<Post> getAll();

    Post findById(int id);
}
