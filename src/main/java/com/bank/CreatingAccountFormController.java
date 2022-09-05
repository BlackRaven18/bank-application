package com.bank;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.ArrayList;

public class CreatingAccountFormController {

    private static final int MIN_AGE = 18;

    @FXML private TextField name, surname, age, login, password;
    @FXML private Label creatingAccountWarning;

    public void initiateController(){
        configureTextFields();
    }

    public void configureTextFields() {
        TextFieldFormatConfigurator configurator = new TextFieldFormatConfigurator();
        configurator.configureNumericTextField(age, TextFieldFormatConfigurator.intFormat);
        configurator.configureCharacterTextField(name);
        configurator.configureCharacterTextField(surname);
    }

    @FXML
    public void goBack() throws IOException {
        Main main = new Main();
        main.changeScene("UserLogForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT );
    }

    private boolean checkIfAccountWithThisLoginExist(String login){
        for(Account account : Main.accounts.getlist()){
            if(account.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfAccountWithThisPasswordExist(String password){
        for(Account account : Main.accounts.getlist()){
            if(account.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }



    @FXML
    public void createAccount(){

        configureTextFields();

        creatingAccountWarning.setText("");
        creatingAccountWarning.setTextFill(Color.RED);

        if(name.getText().isEmpty()){
            creatingAccountWarning.setText("Podaj imię!");
            return;
        } else if(surname.getText().isEmpty()){
            creatingAccountWarning.setText("Podaj nazwisko!");
            return;
        } else if(age.getText().isEmpty()){
            creatingAccountWarning.setText("Podaj wiek!");
            return;
        } else if(Integer.parseInt(age.getText()) < MIN_AGE){
            creatingAccountWarning.setText("Wiek nie może być mniejszy niż 18 lat!");
            return;
        } else if(login.getText().isEmpty()) {
            creatingAccountWarning.setText("Podaj login!");
            return;
        } else if(checkIfAccountWithThisLoginExist(login.getText())){
            creatingAccountWarning.setText("Podany login już istnieje w naszej bazie, prosze wybrać inny!");
            return;
        } else if(password.getText().isEmpty()){
            creatingAccountWarning.setText("Podaj hasło!");
            return;
        } else if(checkIfAccountWithThisPasswordExist(password.getText())){
            creatingAccountWarning.setText("Podane hasło już istnieje w naszej bazie, proszę wybrać inne hasło!");
            return;
        }

        //Creating account
        int accountNumber = Main.accounts.getlist().size() + 1;
        Account account = new Account(String.valueOf(accountNumber), name.getText(), surname.getText(), login.getText(), password.getText(),
                100.0, 100.0, 100.0, 100.0, 100.0, null, new ArrayList<>());

        // adding new Account
        Main.accounts.addAccount(account);

        // message to user
        creatingAccountWarning.setTextFill(Color.GREEN);
        creatingAccountWarning.setText("Gratulacje! Konto zostało założone!, Teraz spróbuj się zalogować");
    }

}
