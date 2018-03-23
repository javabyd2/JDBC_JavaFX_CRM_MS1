package com.sda.javafx.controller;

import com.sda.javafx.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

public class RootController {

    public static Main main;

    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem addContactMenuItem;
    @FXML
    private MenuItem editContactMenuItem;
    @FXML
    private MenuItem removeContactMenuItem;
    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private void initialize() {
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
    }

    @FXML
    public void onShowingMenu() throws Exception {
        boolean disable = main.mainControllerGlobal.getEditPersonButtonDisable();
        editContactMenuItem.setDisable(disable);
        removeContactMenuItem.setDisable(disable);
    }

    @FXML
    public void addContact() throws Exception {
        main.mainControllerGlobal.addPerson();
    }

    @FXML
    public void editContact() throws Exception {
        main.mainControllerGlobal.editPerson();
    }

    @FXML
    public void removeContact() throws Exception {
        main.mainControllerGlobal.deletePerson();
    }

    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("O programie...");
        alert.setHeaderText("Autor: Marcin Słomiński\n" +
                "Program powstał w ramach ćwiczeń na zajęciach: \"Programowanie II\" oraz \"JDBC/Hibernate\"\n" +
                "w Software Development Academy w Bydgoszczy");
        alert.setContentText("Pozdrowienia dla Łukasza Ogana oraz developerów grupy JAVABYD2 :)");
        alert.showAndWait();
    }
}
