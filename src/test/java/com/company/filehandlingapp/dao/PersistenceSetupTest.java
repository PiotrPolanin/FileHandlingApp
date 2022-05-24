package com.company.filehandlingapp.dao;

import com.company.filehandlingapp.environment.PersistenceSetup;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public abstract class PersistenceSetupTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PersistenceSetupTest.class);
    protected static SessionFactory sf;

    @BeforeAll
    public static void createSessionFactory() {
        PersistenceSetup ps = new PersistenceSetup("META-INF/hibernate_test.cfg.xml");
        sf = ps.getSessionFactory();
    }

    @BeforeEach
    public void addSampleData() {
        dbExecution("scripts/sample_test_data_for_items_managments_tables.sql");
    }

    @AfterEach
    public void recreateSchema() {
        dbExecution("scripts/drop_items_management_tables.sql");
        dbExecution("scripts/create_items_management_tables.sql");
    }


    @AfterAll
    public static void closeSessionFactory() {
        if (sf != null && !sf.isClosed()) {
            sf.close();
        }
    }

    private static void dbExecution(String sqlQuery) {
        InputStream inputStream = PersistenceSetupTest.class.getClassLoader().getResourceAsStream(sqlQuery);
        String sqlStatement = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
        String url = (String) sf.getProperties().get("hibernate.connection.url");
        String user = (String) sf.getProperties().get("connection.username");
        String password = (String) sf.getProperties().get("connection.password");
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            LOGGER.info("SQL statement: " + sqlStatement);
            statement.executeUpdate(sqlStatement);
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
