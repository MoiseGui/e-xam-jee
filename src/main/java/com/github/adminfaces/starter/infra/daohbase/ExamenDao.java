package com.github.adminfaces.starter.infra.daohbase;

import com.flipkart.hbaseobjectmapper.AbstractHBDAO;
import com.flipkart.hbaseobjectmapper.Records;
import com.github.adminfaces.starter.model.hbase.Examen;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamenDao extends AbstractHBDAO<String, Examen> {

    public ExamenDao(Connection connection) {
        super(connection);
    }

    public Examen save(Examen examen) throws IOException {
        String id = persist(examen);
        return get(id);
    }

    public List<Examen> findAll() throws IOException {
        List<Examen> examens = new ArrayList<>();
        Scan scan = new Scan();
        try (Records<Examen> examensRecords = records(scan)) {
            examensRecords.forEach(examens::add);
            return examens;
        }
    }


    public Examen findById(String id) throws IOException {
        return get(id);
    }

    public Examen update(Examen examen) throws IOException {
        return findById(persist(examen));
    }

    public void deleteById(String id) throws IOException {
        delete(id);
    }
}
