package com.sda.javafx.controller;

import com.sda.javafx.Main;
import com.sda.javafx.model.ActionState;
import com.sda.javafx.model.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class MainController {

    @FXML
    private TableView<Person> personTableView;
    @FXML
    private TableColumn<Person, String> firstnameColumn;
    @FXML
    private TableColumn<Person, String> lastColumn;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalcodeLabel;
    @FXML
    private Label birthdayLabel;

    @FXML
    private Button addPersonButton;
    @FXML
    private Button editPersonButton;
    @FXML
    private Button deletePersonButton;

    public MainController() {
        String s = "";
    }

    @FXML
    private void initialize() throws Exception {
        main.connection = DBConnector.getConnection("jdbc:mysql://127.0.0.1", "crm_db", "root", "JavaRootMS1", "3306", "com.mysql.cj.jdbc.Driver", "useSSL=false&serverTimezone=UTC");
        main.statement = main.connection.createStatement();

        firstnameColumn.setCellValueFactory(
                data -> data.getValue().firstnameProperty()
        );
        lastColumn.setCellValueFactory(
                data -> data.getValue().lastnameProperty()
        );

        personTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldSelectedPerson, newSelectedPerson) -> {
                    if (newSelectedPerson != null) {
                        showPerson(newSelectedPerson);
                    } else {
                        if (oldSelectedPerson != null && main.modifyId >= 0) {
                            personTableView.getFocusModel().focus(main.modifyId);
                            personTableView.getSelectionModel().select(main.modifyId);
                            main.modifyId = -1;
                        }
                    }
                });
// inna metoda:
//            .addListener((obs, oldSelection, newSelection) -> {
//                if (newSelection != null) {
//                    firstNameLabel.setText(newSelection.getFirstname());
//                    lastnameLabel.setText(newSelection.getLastname());
//                }
//            });
    }

    private void showPerson(Person person) {
        if (!person.equals(null)) {
            firstNameLabel.setText(person.getFirstname());
            lastnameLabel.setText(person.getLastname());
            cityLabel.setText(person.getCity());
            postalcodeLabel.setText(person.getPostalcode());
            streetLabel.setText(person.getStreet());
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            birthdayLabel.setText(df.format(person.getBirthday()));

            editPersonButton.setDisable(personTableView.getSelectionModel().getSelectedItems().size() != 1);
            deletePersonButton.setDisable(personTableView.getSelectionModel().getSelectedItems().size() != 1);
        }
    }

    //referencja do klasy main
    private Main main;

    public void setMain(Main main) /*throws Exception */ {
        this.main = main;
        personTableView.setItems(main.getPerson());
    }

    @FXML
    public void addPerson() throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PersonData.fxml"));
        AnchorPane addPersonLayout = loader.load();

        PersonController personController = loader.getController();
        personController.setState(ActionState.ADD);
        personController.setMain(main);

        Stage stage = new Stage();
        Scene scene = new Scene(addPersonLayout);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void editPerson() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PersonData.fxml"));
        AnchorPane addPersonLayout = loader.load();

        PersonController personController = loader.getController();
        personController.setState(ActionState.MODIFY);
        personController.setMain(main);
        personController.setPerson(personTableView.getSelectionModel().getSelectedItem());
        personController.setPersonId(personTableView.getSelectionModel().getFocusedIndex());
        personController.showPerson(personTableView.getSelectionModel().getSelectedItem());

        Stage stage = new Stage();
        Scene scene = new Scene(addPersonLayout);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void deletePerson() throws Exception {
        int index = personTableView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usunięcie osoby z listy");
            alert.setHeaderText("Usuwanie osoby");
            String personDescription = main.getPerson().get(index).toString();
            alert.setContentText("Czy na pewno chcesz usunąć osobę: " + personDescription + "?");
            Optional<ButtonType> buttons = alert.showAndWait();

            if (buttons.get() == ButtonType.OK) {
                String updateTableSQL = "delete from persons "
                        + "where personId = ?";
                PreparedStatement preparedStatement = main.connection.prepareStatement(updateTableSQL);
                preparedStatement.setInt(1, personTableView.getSelectionModel().getTableView().getItems().get(index).getPersonId());
                preparedStatement.executeUpdate();

                personTableView.getItems().remove(index);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Usunięcie osoby z listy");
                alert.setHeaderText("Usuwanie osoby");
                alert.setContentText("Usunąłeś osobę: " + personDescription);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Błąd");
            alert.setHeaderText("To jest error");
            alert.setContentText("Nie można usunąć");
            alert.showAndWait();
        }
    }

    public boolean getEditPersonButtonDisable() {
        boolean b = editPersonButton.isDisable();
        return editPersonButton.isDisable();
    }
}
