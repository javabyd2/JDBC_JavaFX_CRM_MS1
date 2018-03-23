package com.sda.javafx.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.*;
import java.util.Optional;

public class DBConnector {

    private static Connection connection = null;
    private static String ADDRESS = "";
    private static String DATEBASE = "";
    private static String USER = "";
    private static String PW = "";
    private static String PORT = "";
    private static String DRIVER = "";
    private static String PARAMS = "";

    private static void loadDriver() {
        try {
            Class.forName(DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean loadConnection() throws Exception {
        try {
            connection = DriverManager.getConnection(getFormatedURL(), USER, PW);
            return false;
        } catch (SQLException e) {
            String DB_URL = ADDRESS + "?" + PARAMS;
            Connection connectionCreateDatabase = null;
            Statement stmt = null;
            Class.forName(DRIVER);
            connectionCreateDatabase = DriverManager.getConnection(DB_URL, USER, PW);
            stmt = connectionCreateDatabase.createStatement();
            String sql = "create database crm_db";
            stmt.executeUpdate(sql);
            connection = DriverManager.getConnection(getFormatedURL(), USER, PW);
            stmt = connection.createStatement();
            sql = "create table persons (\n" +
                    "personId smallint(50) unsigned not null auto_increment, \n" +
                    "firstname varchar(50),\n" +
                    "lastname varchar(50),\n" +
                    "city varchar(50),\n" +
                    "postalcode varchar(6),\n" +
                    "street varchar(100), \n" +
                    "birthday date,\n" +
                    "primary key (personId)\n" +
                    ");";
            stmt.executeUpdate(sql);

            return true;
        }
    }

    private static String getFormatedURL() {
        return ADDRESS + ":" + PORT + "/" + DATEBASE + "?" + PARAMS;
    }

    public static Connection getConnection(String address, String database, String user, String pw, String port, String driver, String params) throws Exception {
        String mysqlFile = "mysql.json";
        String inputUsername = "";
        String inputPw = "";
        File file = new File(mysqlFile);
        if (!file.exists()) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Brak danych dostepowych do bazy danych");
            dialog.setHeaderText("Proszę podać dane użytkownika bazy MySQL.\n" +
                    "Zostanie utworzona w lokalizacji localhost baza danych MySQL o nazwie \"crm_db\"\n" +
                    "oraz tabela o nazwie \"person\".\n" +
                    "Uwaga! Dane dostępowe zostaną zapisane w pliku mysql.json bez szyfrowania danych!\n" +
                    "Po zatwierdzeniu proszę uruchomić ponownie aplikację :)");

            // Set the icon (must be included in the project).
            dialog.setGraphic(new ImageView("login.png"));

            // Set the button types.
            ButtonType loginButtonType = new ButtonType("Zatwierdź", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField username = new TextField();
            username.setPromptText("root");
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Hasło");

            grid.add(new Label("Nazwa użytkownika:"), 0, 0);
            grid.add(username, 1, 0);
            grid.add(new Label("Hasło:"), 0, 1);
            grid.add(passwordField, 1, 1);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            username.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> username.requestFocus());

            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType) {
                    return new Pair<>(username.getText(), passwordField.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            if (result.isPresent()) {
                inputUsername = username.getText();
                inputPw = passwordField.getText();
            }

            if (!inputUsername.equals("")) {
                JSONObject obj = new JSONObject();
                obj.put("username", inputUsername);
                obj.put("pw", inputPw);

                try (FileWriter fileWriter = new FileWriter(mysqlFile)) {

                    fileWriter.write(obj.toJSONString());
                    fileWriter.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            System.exit(0);
        } else {
            try {
                JSONParser parser = new JSONParser();

                Object obj = parser.parse(new FileReader(mysqlFile));

                JSONObject jsonObject = (JSONObject) obj;

                user = (String) jsonObject.get("username");
                pw = (String) jsonObject.get("pw");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (address != "")
            ADDRESS = address;
        if (database != "")
            DATEBASE = database;
        if (user != "")
            USER = user;
        if (pw != "")
            PW = pw;
        if (port != "")
            PORT = port;
        if (driver != "")
            DRIVER = driver;
        if (params != "")
            PARAMS = params;

        if (connection == null) {
            loadDriver();
            loadConnection();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection == null) {
            System.out.println("nie ma co zamykać");
        } else {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
