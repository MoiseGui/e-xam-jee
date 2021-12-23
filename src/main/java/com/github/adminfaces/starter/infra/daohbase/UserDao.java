package com.github.adminfaces.starter.infra.daohbase;

import com.flipkart.hbaseobjectmapper.AbstractHBDAO;
import com.flipkart.hbaseobjectmapper.Records;
import com.github.adminfaces.starter.model.hbase.User;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractHBDAO<String, User> {

    public UserDao(Connection connection) { super(connection);}


    public User save(User user) throws IOException {
        String id = persist(user);
        return get(id);
    }

    public List<User> findAll() throws IOException {
        List<User> users = new ArrayList<>();
        Scan scan = new Scan();
        try (Records<User> userRecords = records(scan)) {
            userRecords.forEach(users::add);
            return users;
        }
    }

    public User findById(String id) throws IOException {
        return get(id);
    }



    public User update(User user) throws IOException {
        return findById(persist(user));
    }

    public void deleteById(String id) throws IOException {
        delete(id);
    }

}
