package com.bank;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EmployeeLogFormController {

    private static final String EMPLOYEE_LOGIN = "1";
    private static final String EMPLOYEE_PASSWORD = "1";

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongDataLabel;
    @FXML
    private Button logInButton;
    @FXML
    private Button changeLogInFormButton;

    public void employeeLogIn() throws IOException{
        checkLogin();
    }

    private void checkLogin() throws IOException{

        Main main = new Main();

        if(loginField.getText().equals(EMPLOYEE_LOGIN) && passwordField.getText().equals(EMPLOYEE_PASSWORD)){
            wrongDataLabel.setText("Sukces!");

            // lambda inject initiate method
            InjectObjectToController<EmployeeDashboardController, Object> inject =
                    (EmployeeDashboardController a, Object b) -> a.initiateController();

            main.changeSceneAndInitiateController("EmployeeDashboard.fxml", Main.EMPLOYEE_DASHBOARD_WIDTH, Main.EMPLOYEE_DASHBOARD_HEIGHT,
                    inject, null);

        } else if(loginField.getText().isEmpty()){
            wrongDataLabel.setText("Podaj identyfikator");
        }else if(passwordField.getText().isEmpty()){
            wrongDataLabel.setText("Podaj hasło");
        } else {
            wrongDataLabel.setText("Zly identyfikator lub hasło!");
        }
    }

    public void goToCustomerLogForm() throws IOException {
        Main main = new Main();

        main.changeScene("UserLogForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT);
    }

}
