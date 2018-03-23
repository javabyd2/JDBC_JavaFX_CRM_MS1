package com.sda.javafx.controller;

import com.sda.javafx.Main;
import com.sda.javafx.model.ActionState;
import com.sda.javafx.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonController {

    //referencja do klasy main
    private Main main;
    private Person person = new Person(false);
    private ActionState state = ActionState.NONE;
    private Integer itemId = -1;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setState(ActionState state) {
        this.state = state;
        if (state.equals(ActionState.ADD)) {
            titleLabel.setText("DODAWANIE NOWEJ OSOBY");
        }
        if (state.equals(ActionState.MODIFY)) {
            titleLabel.setText("MODYFIKOWANIE DANYCH OSOBY");
        }
        generateButton.setVisible(state == ActionState.ADD);
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setPersonId(Integer personId) {
        this.itemId = personId;
    }

    @FXML
    private Label titleLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postalcodeTextField;
    @FXML
    private DatePicker birthdayDatePickerField;
    @FXML
    private Button applyButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button generateButton;

    @FXML
    private void initialize() {
    }

    @FXML
    public void generateData() {
        Person person = new Person(true);
        showPerson(person);
    }

    @FXML
    public void applyButtonAction() throws Exception {
        Person personLocal = new Person(false);
        switch (state) {
            case MODIFY:
                personLocal.setPersonId(person.getPersonId());
                break;
            default:
                personLocal.setPersonId(0);
                break;
        }
        personLocal.setFirstname(nameTextField.getText());
        personLocal.setLastname(lastnameTextField.getText());
        personLocal.setStreet(streetTextField.getText());
        personLocal.setCity(cityTextField.getText());
        personLocal.setPostalcode(postalcodeTextField.getText());
        personLocal.setBirthday(java.sql.Date.valueOf(birthdayDatePickerField.getValue()));

        switch (state) {
            case NONE:
                break;
            case ADD:
                main.addPersonToList(personLocal);
                break;
            case MODIFY:
                main.modifyId = itemId;
                main.modifyPersonToList(personLocal, itemId);
                break;
        }
        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }

    public void showPerson(Person personLocal) {
        nameTextField.setText(personLocal.getFirstname());
        lastnameTextField.setText(personLocal.getLastname());
        cityTextField.setText(personLocal.getCity());
        postalcodeTextField.setText(personLocal.getPostalcode());
        streetTextField.setText(personLocal.getStreet());
        birthdayDatePickerField.setValue(personLocal.getBirthday().toLocalDate());
    }

    @FXML
    public void cancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
