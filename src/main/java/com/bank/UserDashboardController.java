package com.bank;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Date;

enum BalanceCurrency{
    PLN, USD, EUR, GBP, CHF
}

public class UserDashboardController {

    //******************************************************************************
    // Header variables
    //******************************************************************************

    @FXML private Label accountBalance;

    //******************************************************************************
    // Personal informations tab variables
    //******************************************************************************

    @FXML private  Label userName, userSurname, userAccountNumber;

    //******************************************************************************
    // Currency exchange tab variables
    //******************************************************************************

    @FXML private TextField  exchangeCurrencyAmount;
    @FXML private MenuButton exchangeCurrencyMenuFirst, exchangeCurrencyMenuSecond;
    @FXML private Label exchangeCurrencyRate, exchangeCurrencyWarning;

    //******************************************************************************
    // Transfer tab variables
    //******************************************************************************

    @FXML private TextField name, surname, number, amount, title;
    @FXML private Label transferWarning;
    @FXML private MenuButton balanceMenu;
    @FXML private CheckBox paymentConfirmation;

    String currentTransferCurrency;

    //******************************************************************************
    // Credit tab variables
    //******************************************************************************

    @FXML
    private TextField monthlyIncome, monthlyCosts, propertyValue, amountOfCredit, periodOfCredit;
    @FXML
    private Label creditWarnings;

    //******************************************************************************
    // general variables
    //******************************************************************************

    Account currentAccount;
    String firstCurrencyToExchange, secondCurrencyToExchange;
    String exchangeRate;
    BalanceCurrency currentBalanceCurrency, transferCurrency;

    //******************************************************************************
    // general methods
    //******************************************************************************

    public void userLogOut() throws IOException {
        Main main = new Main();
        main.changeScene("UserLogForm.fxml", Main.LOGIN_FORM_WIDTH, Main.LOGIN_FORM_HEIGHT);
    }

    public void loadUserDashboard(Account account){
        currentAccount = account;
        this.userName.setText(account.getName());
        this.userSurname.setText(account.getSurname());
        this.userAccountNumber.setText(account.getAccountNumber());
        this.currentBalanceCurrency = BalanceCurrency.PLN;
        this.transferCurrency = BalanceCurrency.PLN;
        this.accountBalance.setText(Double.toString(account.getBalancePLN()));
        this.userAccountNumber.setText(account.getAccountNumber());
        this.firstCurrencyToExchange = this.secondCurrencyToExchange = "pln";
        this.currentTransferCurrency = "PLN";

        configureTextFields();
    }

    public void configureTextFields(){
        TextFieldFormatConfigurator configurator = new TextFieldFormatConfigurator();

        // Exchange currency TextFields
        configurator.configureNumericTextField(exchangeCurrencyAmount, TextFieldFormatConfigurator.doubleFormat);

        // Transfer tab TextFields
        configurator.configureCharacterTextField(name);
        configurator.configureCharacterTextField(surname);
        configurator.configureNumericTextField(amount, TextFieldFormatConfigurator.doubleFormat);

        // Credit tab TextFields
        configurator.configureNumericTextField(monthlyIncome, TextFieldFormatConfigurator.doubleFormat);
        configurator.configureNumericTextField(monthlyCosts, TextFieldFormatConfigurator.doubleFormat);
        configurator.configureNumericTextField(propertyValue, TextFieldFormatConfigurator.doubleFormat);
        configurator.configureNumericTextField(amountOfCredit, TextFieldFormatConfigurator.doubleFormat);
        configurator.configureNumericTextField(periodOfCredit, TextFieldFormatConfigurator.doubleFormat);

    }

    public void updateBalance(){
        switch(currentBalanceCurrency){
            case PLN -> accountBalance.setText(String.format("%.2f", currentAccount.getBalancePLN()));
            case USD -> accountBalance.setText(String.format("%.2f", currentAccount.getBalanceUSD()));
            case EUR -> accountBalance.setText(String.format("%.2f", currentAccount.getBalanceEUR()));
            case GBP -> accountBalance.setText(String.format("%.2f", currentAccount.getBalanceGBP()));
            case CHF -> accountBalance.setText(String.format("%.2f", currentAccount.getBalanceCHF()));
        }
    }

    @FXML
    public void showPLNBalance(){
        balanceMenu.setText("PLN");
        currentBalanceCurrency = BalanceCurrency.PLN;
        accountBalance.setText(String.format("%.2f",currentAccount.getBalancePLN()));
    }

    @FXML
    public void showUSDBalance(){
        balanceMenu.setText("USD");
        currentBalanceCurrency = BalanceCurrency.USD;
        accountBalance.setText(String.format("%.2f", currentAccount.getBalanceUSD()));
    }

    @FXML
    public void showEURBalance(){
        balanceMenu.setText("EUR");
        currentBalanceCurrency = BalanceCurrency.EUR;
        accountBalance.setText(String.format("%.2f", currentAccount.getBalanceEUR()));
    }

    @FXML
    public void showGBPBalance(){
        balanceMenu.setText("GBP");
        currentBalanceCurrency = BalanceCurrency.GBP;
        accountBalance.setText(String.format("%.2f", currentAccount.getBalanceGBP()));
    }

    @FXML
    public void showCHFBalance(){
        balanceMenu.setText("CHF");
        currentBalanceCurrency = BalanceCurrency.CHF;
        accountBalance.setText(String.format("%.2f", currentAccount.getBalanceCHF()));
    }

    //******************************************************************************
    // Currency exchange tab methods
    //******************************************************************************

    @FXML
    public void exchangeCurrency() throws MalformedURLException {
        exchangeCurrencyWarning.setText("");
        exchangeCurrencyWarning.setTextFill(Color.RED);

        if(exchangeCurrencyAmount.getText().isEmpty()){
            exchangeCurrencyWarning.setText("Musisz podać kwotę!");
            return;
        }

        if(!showExchangeRate()){
            return;
        }

        double amountOfMoney = Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')) * Double.parseDouble(exchangeRate.replace(',', '.'));

        boolean isMoneyAvailable = true;
        switch(firstCurrencyToExchange){
            case "pln":
                if(currentAccount.getBalancePLN() - amountOfMoney < 0) {isMoneyAvailable = false;}
                break;
            case "usd":
                if(currentAccount.getBalanceUSD() - amountOfMoney < 0) isMoneyAvailable = false;
                break;
            case "eur":
                if(currentAccount.getBalanceEUR() - amountOfMoney < 0) isMoneyAvailable = false;
                break;
            case "gbp":
                if(currentAccount.getBalanceGBP() - amountOfMoney < 0) isMoneyAvailable = false;
                break;
            case "chf":
                if(currentAccount.getBalanceCHF() - amountOfMoney < 0) isMoneyAvailable = false;
                break;
        }

        if(!isMoneyAvailable){
            exchangeCurrencyWarning.setText("Zbyt mało środków, aby przeprowadzić operację");
            return;
        }

        switch(firstCurrencyToExchange){
            case "pln" -> currentAccount.addToBalancePLN(-amountOfMoney);
            case "usd" -> currentAccount.addToBalanceUSD(-amountOfMoney);
            case "eur" -> currentAccount.addToBalanceEUR(-amountOfMoney);
            case "gbp" -> currentAccount.addToBalanceGBP(-amountOfMoney);
            case "chf" -> currentAccount.addToBalanceCHF(-amountOfMoney);
        }

        switch(secondCurrencyToExchange){
            case "pln" -> currentAccount.addToBalancePLN(Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')));
            case "usd" -> currentAccount.addToBalanceUSD(Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')));
            case "eur" -> currentAccount.addToBalanceEUR(Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')));
            case "gbp" -> currentAccount.addToBalanceGBP(Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')));
            case "chf" -> currentAccount.addToBalanceCHF(Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.')));
        }

        exchangeCurrencyWarning.setText("Dokonano wymiany walut");
        exchangeCurrencyWarning.setTextFill(Color.GREEN);

        //updating balance
        updateBalance();

        // adding entry to operation history
        currentAccount.addEntryToOperationsHistory(new Operation(OperationType.CURRENCY_EXCHANGE));
    }

    public boolean showExchangeRate() throws MalformedURLException {

        double exchangeValue;

        if(firstCurrencyToExchange.equals(secondCurrencyToExchange)){
            exchangeCurrencyWarning.setText("Waluty muszą się różnić!");
            return false;
        }

        APINBPManager apiManager = new APINBPManager();
        //System.out.println(apiManager.getTodaysCurrencyInformation("eur"));

        if(firstCurrencyToExchange.equals("pln")){
            exchangeRate = apiManager.getTodaysCurrencyInformation(secondCurrencyToExchange);

        } else if(secondCurrencyToExchange.equals("pln")){
            Double tmpRate = 1 / Double.parseDouble(apiManager.getTodaysCurrencyInformation(firstCurrencyToExchange));
            exchangeRate = String.format("%.4f", tmpRate);
        }else{
            Double tmpRate = Double.parseDouble(apiManager.getTodaysCurrencyInformation(firstCurrencyToExchange))
                    / Double.parseDouble(apiManager.getTodaysCurrencyInformation(secondCurrencyToExchange));
            exchangeRate = String.format("%.4f", tmpRate);
        }

        exchangeValue = Double.parseDouble(exchangeRate.replace(',', '.')) * Double.parseDouble(exchangeCurrencyAmount.getText().replace(',', '.'));

        exchangeCurrencyRate.setText("aktualny kurs wymiany " + firstCurrencyToExchange.toUpperCase() + "/" + secondCurrencyToExchange.toUpperCase() + " wynosi: " +
                exchangeRate + ", kosz wymiany: " + String.format("%.2f ", exchangeValue) + secondCurrencyToExchange.toUpperCase());
        return true;
    }

    public void selectFirstPLNCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuFirst.setText("PLN");
        firstCurrencyToExchange = "pln";
        showExchangeRate();
    }

    public void selectFirstUSDCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuFirst.setText("USD");
        firstCurrencyToExchange = "usd";
        showExchangeRate();
    }

    public void selectFirstEURCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuFirst.setText("EUR");
        firstCurrencyToExchange = "eur";
        showExchangeRate();
    }

    public void selectFirstGBPCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuFirst.setText("GBP");
        firstCurrencyToExchange = "gbp";
        showExchangeRate();
    }

    public void selectFirstCHFCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuFirst.setText("CHF");
        firstCurrencyToExchange = "chf";
        showExchangeRate();
    }

    // ********************************************************

    public void selectSecondPLNCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuSecond.setText("PLN");
        secondCurrencyToExchange= "pln";
        showExchangeRate();
    }

    public void selectSecondUSDCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuSecond.setText("USD");
        secondCurrencyToExchange= "usd";
        showExchangeRate();
    }

    public void selectSecondEURCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuSecond.setText("EUR");
        secondCurrencyToExchange= "eur";
        showExchangeRate();
    }

    public void selectSecondGBPCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuSecond.setText("GBP");
        secondCurrencyToExchange= "gbp";
        showExchangeRate();
    }

    public void selectSecondCHFCurrencyExchange() throws MalformedURLException {
        exchangeCurrencyMenuSecond.setText("CHF");
        secondCurrencyToExchange= "chf";
        showExchangeRate();
    }

    //******************************************************************************
    // Transfer tab methods
    //******************************************************************************

    @FXML
    public void transferMoney(){
        transferWarning.setText("");
        transferWarning.setTextFill(Color.RED);

        Account destinationAccount;

        // checking requirements
        if(name.getText().isEmpty()){
            transferWarning.setText("Imię odbiorcy nie może być puste!");
        } else if(surname.getText().isEmpty()){
            transferWarning.setText("Nazwisko odbiorcy nie może być pusta!");
        } else if(number.getText().isEmpty()){
            transferWarning.setText("Numer rachunku odbiorcy nie może być pusty!!!");
        } else if(amount.getText().isEmpty()){
            transferWarning.setText("Wpisz kwotę przelewu!");
        } else if(title.getText().isEmpty()){
            transferWarning.setText("Podaj tytuł przelewu!");
        } else{
            transferWarning.setText("");

            double tmpAccountBalance;
            switch(transferCurrency){
                case PLN -> tmpAccountBalance = currentAccount.getBalancePLN();
                case USD -> tmpAccountBalance = currentAccount.getBalanceUSD();
                case EUR -> tmpAccountBalance = currentAccount.getBalanceEUR();
                case GBP -> tmpAccountBalance = currentAccount.getBalanceGBP();
                case CHF -> tmpAccountBalance = currentAccount.getBalanceCHF();
                default -> tmpAccountBalance = 0.0;
            }
            //double tmpAccountBalance = Double.parseDouble(accountBalance.getText().replace(',', '.'));
            double tmpAmount = Double.parseDouble(amount.getText().replace(',', '.'));
            double accountBalanceAfterTransfer = tmpAccountBalance - tmpAmount;

            if(accountBalanceAfterTransfer < 0){
                transferWarning.setText("Nie masz tylu środków, aby wykonać przelew!");
                return;
            }

            destinationAccount = Main.accounts.findAccount(number.getText(), name.getText(), surname.getText());
            if(destinationAccount == null){
                transferWarning.setText("Brak klienta o takich danych!!");
                return;
            }


            //sending money
            switch (transferCurrency) {
                case PLN -> {
                    currentAccount.setBalancePLN(accountBalanceAfterTransfer);
                    destinationAccount.addToBalancePLN(tmpAmount);
                }
                case USD -> {
                    currentAccount.setBalanceUSD(accountBalanceAfterTransfer);
                    destinationAccount.addToBalanceUSD(tmpAmount);
                }
                case EUR -> {
                    currentAccount.setBalanceEUR(accountBalanceAfterTransfer);
                    destinationAccount.addToBalanceEUR(tmpAmount);
                }
                case GBP -> {
                    currentAccount.setBalanceGBP(accountBalanceAfterTransfer);
                    destinationAccount.addToBalanceGBP(tmpAmount);
                }
                case CHF -> {
                    currentAccount.setBalanceCHF(accountBalanceAfterTransfer);
                    destinationAccount.addToBalanceCHF(tmpAmount);
                }
            }

            updateBalance();
            transferWarning.setText("Przelew wysłany!!!!");
            transferWarning.setTextFill(Color.GREEN);

            // generating payment confirmation
            generatePaymentConfirmation(destinationAccount);

            // adding entry to operation history
            currentAccount.addEntryToOperationsHistory(new Operation(OperationType.SENDING_MONEY_TRANSFER));
            destinationAccount.addEntryToOperationsHistory(new Operation(OperationType.RECEIVING_MONEY_TRANSFER));
        }

    }

    public void generatePaymentConfirmation(Account destinationAccount) {
        if (paymentConfirmation.isSelected()) {
            try {
                Date nowDate = new Date();
                String separator = System.getProperty("file.separator");
                String fileName = "UserPaymentConfirmations" + separator +
                        currentAccount.getName() + " " + currentAccount.getSurname() + " " + nowDate.toString().replace(":", " ")+ ".txt";
                PrintWriter writer = new PrintWriter(fileName);

                writer.println("Date: "+ nowDate);
                writer.println("Tytul przelewu: " + title.getText());
                writer.println("Imie nadawcy: "+ name.getText());
                writer.println("Nazwisko nadawcy: "+ surname.getText());
                writer.println("Numer konta nadawcy: "+ userAccountNumber.getText());
                writer.println("Numer konta odbiorcy "+ destinationAccount.getAccountNumber());

                writer.println("Kwota: "+ amount.getText() + " " + currentTransferCurrency);

                String accountBalance = "PLN";
                switch(transferCurrency){
                    case PLN -> accountBalance = String.format("%.4f", currentAccount.getBalancePLN());
                    case USD -> accountBalance = String.format("%.4f", currentAccount.getBalanceUSD());
                    case EUR -> accountBalance = String.format("%.4f", currentAccount.getBalanceEUR());
                    case GBP -> accountBalance = String.format("%.4f", currentAccount.getBalanceGBP());
                    case CHF -> accountBalance = String.format("%.4f", currentAccount.getBalanceCHF());
                }
                writer.println("Stan konta po przelewie: "+ accountBalance + " " + currentTransferCurrency);

                writer.close();
            } catch(FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @FXML public void selectTransferPLNCurrency(){
        transferCurrency = BalanceCurrency.PLN;
        currentTransferCurrency = "PLN";
    }
    @FXML public void selectTransferUSDCurrency(){
        transferCurrency = BalanceCurrency.USD;
        currentTransferCurrency = "USD";
    }
    @FXML public void selectTransferEURCurrency(){
        transferCurrency = BalanceCurrency.EUR;
        currentTransferCurrency = "EUR";
    }
    @FXML public void selectTransferGBPCurrency(){
        transferCurrency = BalanceCurrency.GBP;
        currentTransferCurrency = "GBP";
    }
    @FXML public void selectTransferCHFCurrency(){
        transferCurrency = BalanceCurrency.CHF;
        currentTransferCurrency = "CHF";
    }

    //******************************************************************************
    // Credit tab methods
    //******************************************************************************

    @FXML
    public void takeCredit(){
        //inicjacja tekstu ostrzegawczego
        creditWarnings.setText("");
        creditWarnings.setTextFill(Color.RED);

        // ostrzeganie o pustych polach
        if(monthlyIncome.getText().isEmpty()){
            creditWarnings.setText("Podaj kwotę miesięcznego przychodu!");
        } else if(monthlyCosts.getText().isEmpty()){
            creditWarnings.setText("Podaj kwotę miesięcznych wydatków!");
        } else if(propertyValue.getText().isEmpty()){
            creditWarnings.setText("Podaj wartość majątku!");
        } else if(amountOfCredit.getText().isEmpty()){
            creditWarnings.setText("Podaj kwotę kredytu, o jaką się ubiegasz!");
        } else if(periodOfCredit.getText().isEmpty()){
            creditWarnings.setText("Podaj, przez jaki okres czasu chciałbyś spłacać kredyt!");
        }else{

            // wiadomosc o poprawnosci formularza
            creditWarnings.setText("Wniosek został wysłany, i zostanie rozpatrzony przez naszego pracownika");
            creditWarnings.setTextFill(Color.GREEN);

            //TODO operacja kredytowa;

            double monthlyIncomeD = Double.parseDouble(monthlyIncome.getText().replace(',', '.'));
            double monthlyCostsD = Double.parseDouble(monthlyCosts.getText().replace(',', '.'));
            double propertyValueD = Double.parseDouble(propertyValue.getText().replace(',', '.'));
            double amountOfCreditD = Double.parseDouble(amountOfCredit.getText().replace(',', '.'));
            double periodOfCreditD = Double.parseDouble(periodOfCredit.getText().replace(',', '.'));

            Credit newCredit = new Credit(monthlyIncomeD, monthlyCostsD, propertyValueD, amountOfCreditD, periodOfCreditD, false);
            currentAccount.setCredit(newCredit);
        }

        // adding entry to operation history
        currentAccount.addEntryToOperationsHistory(new Operation(OperationType.CREDIT_APPLICATION));
    }
    
    
}
