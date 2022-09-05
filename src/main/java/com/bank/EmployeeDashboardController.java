package com.bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EmployeeDashboardController {


    //******************************************************************************
    // general variables
    //******************************************************************************
    Account currentAccount;

    //******************************************************************************
    // general methods
    //******************************************************************************

    public void initiateController(){
        TextFieldFormatConfigurator configurator = new TextFieldFormatConfigurator();

        configurator.configureCharacterTextField(nameField);
        configurator.configureCharacterTextField(surnameField);
        configurator.configureCharacterTextField(searchedName);
        configurator.configureCharacterTextField(searchedSurname);

    }

    public ObservableList<Account>  getAccounts(){
        ObservableList<Account> observableAccounts = FXCollections.observableArrayList();
        observableAccounts.addAll(Main.accounts.getlist());

        return observableAccounts;
    }

    public ObservableList<Operation> getOperations(Account account){
        ObservableList<Operation> observableOperations = FXCollections.observableArrayList();

        observableOperations.addAll(account.getOperationsHistory());
        return observableOperations;
    }

    public ObservableList<Account> getClientsRequestingCredit(){
        ObservableList<Account> observableClients = FXCollections.observableArrayList();

        for(Account account : Main.accounts.getlist()){
            if(account.getCredit() != null && !account.getCredit().isApproved()){
                observableClients.add(account);
            }
        }
        return observableClients;
    }

    public void employeeLogOut() throws IOException {
        Main main = new Main();
        main.changeScene("EmployeeLogForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT);
    }

    //******************************************************************************
    // Clients data tab variables
    //******************************************************************************

    @FXML private TableView<Account> clientsTable;
    @FXML private TableColumn<Account, String> nameColumn;
    @FXML private TableColumn<Account, String> surnameColumn;
    @FXML private TableColumn<Account, String> accountNumberColumn;
    @FXML private TableColumn<Account, String> loginColumn;
    @FXML private TableColumn<Account, String> passwordColumn;

    @FXML private TextField loginField, passwordField, nameField, surnameField, accountNumberField;
    @FXML private Label clientsDataWarning;

    //******************************************************************************
    // Operations history tab variables
    //******************************************************************************

    @FXML private TableView<Operation> operationHistoryTable;
    @FXML private TableColumn<Operation, String> operationNameColumn;
    @FXML private TableColumn<Operation, LocalDateTime> operationDataColumn;

    @FXML private TextField searchedName, searchedSurname, searchedAccountNumber;
    @FXML private Label operationsHistoryWarning;

    //******************************************************************************
    // Credit approve tab variables
    //******************************************************************************

    @FXML private TableView<Account> clientsRequestingCreditTable;
    @FXML private TableColumn<Account, String> clientName;
    @FXML private TableColumn<Account, String> clientSurname;
    @FXML private TableColumn<Account, String> clientAccountNumber;

    @FXML private Label creditAmount, creditPeriod, monthlyIncome, monthlyCosts, propertyValue, creditRate, fundsToPayOff, creditApproveWarning;

    Account creditRequestingAccount;

    //******************************************************************************
    // Credit approve tab methods
    //******************************************************************************

    @FXML
    public void showClientsRequestingCredit(){

        clientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        clientAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        clientsRequestingCreditTable.setItems(getClientsRequestingCredit());
    }

    @FXML public void showClientCreditData() {
        int index = clientsRequestingCreditTable.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            return;
        }

        String tmpAccountNumber = clientAccountNumber.getCellData(index);
        String tmpName = clientName.getCellData(index);
        String tmpSurname = clientSurname.getCellData(index);
        Account tmpAccount = Main.accounts.findAccount(tmpAccountNumber, tmpName, tmpSurname);
        creditRequestingAccount = tmpAccount;

        if (tmpAccount == null) {
            //creditApproveWarning.setText("Blaad");
            return;
        }

        //creditPeriod, monthlyIncome, monthlyCosts, propertyValue, fundsToPayOff, creditApproveWarning;
        creditAmount.setText(tmpAccount.getCredit().getAmountOfCredit() + " PLN");
        creditPeriod.setText(tmpAccount.getCredit().getPeriodOfCredit() + " (lata)");
        monthlyIncome.setText(tmpAccount.getCredit().getMonthlyIncome() + " PLN");
        monthlyCosts.setText(tmpAccount.getCredit().getMonthlyCosts() + " PLN");
        propertyValue.setText(tmpAccount.getCredit().getPropertyValue() + " PLN");
        creditRate.setText(String.format("%.2f", tmpAccount.getCredit().getCreditRate()) + " PLN");
        fundsToPayOff.setText(String.format("%.2f", tmpAccount.getCredit().getFundsToPayOff()) + " PLN");

        if (tmpAccount.getCredit().getFundsToPayOff() < tmpAccount.getCredit().getCreditRate()) {
            creditApproveWarning.setTextFill(Color.RED);
            creditApproveWarning.setText("Srodki na spłatę są mniejsze niż rata kredytu, zaleca się ODMOWĘ udzielenia kredytu!");
        } else {
            creditApproveWarning.setTextFill(Color.GREEN);
            creditApproveWarning.setText("Srodki na spłatę są większe niż rata kredytu, zaleca się ZAAKCEPTOWANIE udzielenia kredytu!");
        }
    }

    @FXML
    public void acceptCreditApplication(){
        if(creditRequestingAccount == null){
            return;
        }
        // accepting credit
        creditApproveWarning.setTextFill(Color.GREEN);
        creditApproveWarning.setText("Podanie o kredyt zostało zakceptowane");

        // dodanie kwoty kredytu do salda
        creditRequestingAccount.getOperationsHistory().add(new Operation(OperationType.CREDIT_GRANTED));
        creditRequestingAccount.getCredit().setApproved(true);
        creditRequestingAccount.addToBalancePLN(creditRequestingAccount.getCredit().getAmountOfCredit());

        writeCreditRatesToFile();

        showClientsRequestingCredit();
        operationHistoryTable.refresh();



    }

    private void writeCreditRatesToFile(){
        try{
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDateFormatted;

            String separator = System.getProperty("file.separator");
            String fileName = "UserCreditRates" + separator +
                    creditRequestingAccount.getName() + " " + creditRequestingAccount.getSurname() + " Credit Rates.txt";
            PrintWriter writer = new PrintWriter(fileName);

            writer.println("Raty kredytu");
            writer.println("");

            int numberOfRates = (int)(creditRequestingAccount.getCredit().getPeriodOfCredit() * 12);
            for(int i = 1; i <= numberOfRates; i++){
                currentDateFormatted = localDateTime.format(formatter);
                writer.println(i + ". " + currentDateFormatted + " kwota: " + String.format("%.2f", creditRequestingAccount.getCredit().getCreditRate()));
                localDateTime = localDateTime.plusMonths(1);
            }
            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void rejectCreditApplication(){

        if(creditRequestingAccount == null){
            return;
        }

        // rejecting credit
        creditApproveWarning.setTextFill(Color.RED);
        creditApproveWarning.setText("Podanie o kredyt zostało odrzcone");

        writeRejectedCreditDecisionToFile();
        creditRequestingAccount.setCredit(null);
        showClientsRequestingCredit();
        operationHistoryTable.refresh();
    }

    private void writeRejectedCreditDecisionToFile(){
        try{
            String separator = System.getProperty("file.separator");
            String fileName = "UserCreditRates" + separator +
                    creditRequestingAccount.getName() + " " + creditRequestingAccount.getSurname() + " Credit Rejected Decision.txt";
            PrintWriter writer = new PrintWriter(fileName);
            writer.println("Data wydania decyzji:" + new Date());
            writer.println("""
                    
                    Jesteśmy zmuszeni poinformować, że wniosek o przyznanie kredytu został
                    odrzucony.
                    
                    Pozdrawiamy.
                    
                    """);
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }



    //******************************************************************************
    // Clients data tab methods
    //******************************************************************************

    @FXML
    public void getSelected(){
        clientsDataWarning.setText("");
        int index = clientsTable.getSelectionModel().getSelectedIndex();

        if(index < 0){
            return;
        }

        nameField.setText(nameColumn.getCellData(index));
        surnameField.setText(surnameColumn.getCellData(index));
        accountNumberField.setText(accountNumberColumn.getCellData(index));
        loginField.setText(loginColumn.getCellData(index));
        passwordField.setText(passwordColumn.getCellData(index));

        currentAccount = Main.accounts.findAccount(accountNumberField.getText(), nameField.getText(), surnameField.getText());
    }

    @FXML
    public void changeAccountData(){
        if(currentAccount == null){
            return;
        }
        currentAccount.setName(nameField.getText());
        currentAccount.setSurname(surnameField.getText());
        currentAccount.setAccountNumber(accountNumberField.getText());
        currentAccount.setLogin(loginField.getText());
        currentAccount.setPassword(passwordField.getText());

        clientsDataWarning.setText("Dane zostały zmienione");
        clientsTable.refresh();
    }

    @FXML
    public void deleteAccount(){

        //deleting account
        Main.accounts.getlist().remove(currentAccount);
        currentAccount = null;

        clientsDataWarning.setTextFill(Color.ORANGE);
        clientsDataWarning.setText("Konto zostalo usunięte!");
        showClients();
    }

    @FXML
    public void showClients(){
        //set up the column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        clientsTable.setItems(getAccounts());

    }

    //******************************************************************************
    // Clients data tab methods
    //******************************************************************************

    @FXML
    public void showOperationsHistory(){
        operationsHistoryWarning.setText("");
        operationsHistoryWarning.setTextFill(Color.RED);

        if(searchedName.getText().isEmpty()){
            operationsHistoryWarning.setText("Podaj imie!");
            return;
        } else if(searchedSurname.getText().isEmpty()){
            operationsHistoryWarning.setText("Podaj nazwisko!");
            return;
        } else if(searchedAccountNumber.getText().isEmpty()){
            operationsHistoryWarning.setText("Podaj numer konta");
            return;
        }

        Account tmpAccount = currentAccount = Main.accounts.findAccount(searchedAccountNumber.getText(), searchedName.getText(), searchedSurname.getText());

        if(tmpAccount == null){
            operationsHistoryWarning.setText("Nie znaleziono klienta o podanych danych!");
            return;
        }

        operationNameColumn.setCellValueFactory(new PropertyValueFactory<>("operationName"));
        operationDataColumn.setCellValueFactory(new PropertyValueFactory<>("operationLocalDate"));

        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
        operationDataColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // Formating data
                    setText(myDateFormatter.format(item));
                }
            }
        });

        operationHistoryTable.setItems(getOperations(tmpAccount));

        operationsHistoryWarning.setText("Znaleziono historię operacji klienta");
        operationsHistoryWarning.setTextFill(Color.GREEN);
    }

}
