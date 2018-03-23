package com.sda.javafx;

import com.sda.javafx.controller.DBConnector;
import com.sda.javafx.controller.MainController;
import com.sda.javafx.model.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public static Connection connection = null;
    public static Statement statement = null;
    public static Integer modifyId = -1;
    public static MainController mainControllerGlobal;

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    public Main() throws Exception {
    }

    public ObservableList getPerson() {
        return personObservableList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SDA CRM 1.0");
        initRootLayout();
        showPersonLayout();
        loadPersons();
    }

    public void initRootLayout() throws IOException {
        rootLayout = FXMLLoader.load(getClass().getClassLoader().getResource("RootLayout.fxml"));
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showPersonLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PersonOverview.fxml"));
        AnchorPane person = loader.load();
        person.setMinHeight(372);
        person.setPrefHeight(372);
        rootLayout.setCenter(person);
        mainControllerGlobal = loader.getController();

        MainController controller = loader.getController();
        controller.setMain(this);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
        DBConnector.closeConnection();
    }

    public void addPersonToList(Person person) throws Exception {
        try (PreparedStatement statement = connection.prepareStatement("insert into persons "
                        + "(firstname, lastname, city, postalcode, street, birthday) "
                        + "values (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, person.getFirstname());
            statement.setString(2, person.getLastname());
            statement.setString(3, person.getCity());
            statement.setString(4, person.getPostalcode());
            statement.setString(5, person.getStreet());
            statement.setDate(6, person.getBirthday());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    person.setPersonId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
        personObservableList.add(person);
    }

    public void modifyPersonToList(Person person, Integer itemId) throws Exception {
        String updateTableSQL = "update persons "
                + "set firstname = ?, lastname = ?, city = ?, postalcode = ?, street = ?, birthday = ? "
                + "where personId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
        preparedStatement.setString(1, person.getFirstname());
        preparedStatement.setString(2, person.getLastname());
        preparedStatement.setString(3, person.getCity());
        preparedStatement.setString(4, person.getPostalcode());
        preparedStatement.setString(5, person.getStreet());
        preparedStatement.setDate(6, person.getBirthday());
        preparedStatement.setInt(7, person.getPersonId());
        preparedStatement.executeUpdate();

        personObservableList.set(itemId, person);
    }

    public void loadPersons() throws Exception {
        String sql = "select * from persons";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Person person = new Person(resultSet.getInt("personId"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("street"),
                    resultSet.getString("city"),
                    resultSet.getString("postalcode"),
                    resultSet.getDate("birthday"));
            personObservableList.add(person);
        }
    }
}
