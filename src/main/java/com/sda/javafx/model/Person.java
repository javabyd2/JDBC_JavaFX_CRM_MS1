package com.sda.javafx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class Person {
    private IntegerProperty personId;
    private StringProperty firstname;
    private StringProperty lastname;
    private StringProperty street;
    private StringProperty city;
    private StringProperty postalcode;
    private Date birthday;

    static String[] countriesArray = {"Polska", "Niemcy", "Czechy", "Słowacja", "Austria", "Szwajcaria", "Szwecja",
            "Norwegia", "Słowenia", "Chorwacja"};
    static String[] citiesArray = {"Bydgoszcz", "Kraków", "Gerusalem", "Roma", "Siena", "Manoppello", "Vilnius", "Praha",
            "Wien", "Genève"};
    static String[] streets = {"ul. 11 Dywizjonu Artylerii Konnej", "ul. 11 Listopada", "ul. 15 Dywizji Piechoty Wielkopolskiej",
            "ul. 16 Pułku Ułanów Wielkopolskich", "ul. 19 Marca 1981 Roku", "ul. 20 Stycznia 1920 Roku",
            "ul. 2 Października", "ul. 3 Maja", "ul. 62 Pułku Piechoty Wielkopolskiej", "ul. Adama Asnyka",
            "ul. Adama Czartoryskiego", "ul. Adama Grzymały Siedleckiego", "ul. Adama Naruszewicza", "ul. Agatowa",
            "ul. Agrestowa", "ul. Akacjowa", "ul. Akademicka", "ul. Albatrosowa"};
    static String[] mNamesArray = {"Jan", "Andrzej", "Piotr", "Krzysztof", "Stanisław", "Tomasz", "Paweł", "Józef",
            "Marcin", "Marek"};
    static String[] fNamesArray = {"Anna", "Maria", "Katarzyna", "Małgorzata", "Agnieszka", "Krystyna", "Barbara",
            "Ewa", "Elżbieta", "Zofia"};
    static String[] mLastNamesArray = {"Nowak", "Kowalski", "Wiśniewski", "Dąbrowski", "Lewandowski", "Wójcik", "Kamiński",
            "Kowalczyk", "Zieliński", "Szymański", "Woźniak", "Kozłowski", "Jankowski", "Wojciechowski",
            "Kwiatkowski", "Kaczmarek", "Mazur", "Krawczyk", "Piotrowski", "Grabowski"};
    static String[] fLastNamesArray = {"Nowakowska", "Pawłowska", "Michalska", "Nowicka", "Adamczyk", "Dudek", "Zając",
            "Wieczorek", "Jabłońska", "Król", "Majewska", "Olszewska", "Jaworska", "Wróbel", "Malinowska", "Pawlak",
            "Witkowska", "Walczak", "Stępień", "Górska"};

    public Person(boolean generate) {
        if (generate) {
            Person person = generatePersonData();
            this.personId = new SimpleIntegerProperty(0);
            this.firstname = new SimpleStringProperty(person.getFirstname());
            this.lastname = new SimpleStringProperty(person.getLastname());
            this.street = new SimpleStringProperty(person.getStreet());
            this.city = new SimpleStringProperty(person.getCity());
            this.postalcode = new SimpleStringProperty(person.getPostalcode());
            this.birthday = person.getBirthday();
        }
    }

    public Person(Integer personId, String firstname, String lastname, String street, String city, String postalcode, Date birthday) {
        this.personId = new SimpleIntegerProperty(personId);
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.street = new SimpleStringProperty(street);
        this.city = new SimpleStringProperty(city);
        this.postalcode = new SimpleStringProperty(postalcode);
        this.birthday = birthday;
    }

    public int getPersonId() {
        return personId.get();
    }

    public IntegerProperty personIdProperty() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = new SimpleIntegerProperty(personId);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = new SimpleStringProperty(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = new SimpleStringProperty(lastname);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street = new SimpleStringProperty(street);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city = new SimpleStringProperty(city);
    }

    public String getPostalcode() {
        return postalcode.get();
    }

    public StringProperty postalcodeProperty() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = new SimpleStringProperty(postalcode);
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public static Person generatePersonData() {
        Person person = new Person(false);
        Random generator = new Random();
        if (generator.nextBoolean()) {
            person.firstname = new SimpleStringProperty(mNamesArray[generator.nextInt(mNamesArray.length)]);
            person.lastname = new SimpleStringProperty(mLastNamesArray[generator.nextInt(mLastNamesArray.length)]);
        } else {
            person.firstname = new SimpleStringProperty(fNamesArray[generator.nextInt(fNamesArray.length)]);
            person.lastname = new SimpleStringProperty(fLastNamesArray[generator.nextInt(fLastNamesArray.length)]);
        }
        person.street = new SimpleStringProperty(streets[generator.nextInt(streets.length)] + " " + generator.nextInt(100));
        person.city = new SimpleStringProperty(citiesArray[generator.nextInt(citiesArray.length)]);
        person.postalcode = new SimpleStringProperty(String.format("%02d-%03d", generator.nextInt(99) + 1, generator.nextInt(999) + 1));
        person.birthday = java.sql.Date.valueOf(generateDate());
        return person;
    }

    public static LocalDate generateDate() {
        LocalDate localDate = LocalDate.of(1950, 01, 01);
        Random generator = new Random();
        localDate = localDate.plusDays(generator.nextInt(18250));
        return localDate;
    }

    @Override
    public String toString() {
        return firstname.getValue() + " " + lastname.getValue();
    }
}
