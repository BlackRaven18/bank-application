package com.bank;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class UserLogFormController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongDataLabel;


    public void userLogIn() throws IOException{
        checkLogin();
    }

    private void checkLogin() throws IOException{
        wrongDataLabel.setTextFill(Color.RED);
        wrongDataLabel.setText("");

        Main main = new Main();

        if(loginField.getText().isEmpty()){
            wrongDataLabel.setText("Podaj login!");
            return;
        } else if(passwordField.getText().isEmpty()){
            wrongDataLabel.setText("Podaj hasło!");
            return;
        }

        for(Account account : Main.accounts.list){

            if(loginField.getText().equals(account.getLogin()) && passwordField.getText().equals(account.getPassword())){
                wrongDataLabel.setTextFill(Color.GREEN);
                wrongDataLabel.setText("Sukces!");

                // lambda inject object to controller and initate it
                InjectObjectToController<UserDashboardController, Account> inject =
                        (UserDashboardController a, Account b) -> a.loadUserDashboard(b);

                main.changeSceneAndInitiateController("UserDashboard.fxml", Main.DASHBOARD_WIDTH, Main.DASHBOARD_HEIGHT,
                        inject, account);
                break;
            }
        }
        wrongDataLabel.setText("Zly login lub hasło!");

    }

    public void goToEmployeeLogForm() throws IOException{
        Main main = new Main();

        main.changeScene("EmployeeLogForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT );
    }

    @FXML
    public void goToCreateAccountForm() throws IOException{
        Main main = new Main();

        // lambda inject object to controller and initate it
        InjectObjectToController<CreatingAccountFormController, Object> inject =
                (CreatingAccountFormController a, Object b) -> a.initiateController();

        main.changeSceneAndInitiateController("CreatingAccountForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT,
                inject, null);
    }
}
