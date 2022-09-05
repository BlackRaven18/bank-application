package com.bank;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private String accountNumber;
    private String name;
    private String surname;
    private String login;
    private String password;
    private double balancePLN, balanceUSD, balanceEUR, balanceGBP, balanceCHF;
    private Credit credit;
    private List<Operation> operationsHistory;

    // Inicjalizuj obiekt wartościami domyślnymi
    public Account(){this("0", "", "", "", "",
            0.0, 0.0, 0.0, 0.0, 0.0, new Credit(), new ArrayList<>());}



    // Inicjalizuj obiekt przekazanymi wartościami
    public Account(String accountNumber, String name, String surname, String login, String password,
                   double balancePLN, double balanceUSD, double balanceEUR, double balanceGBP, double balanceCHF, Credit credit,
                   List<Operation>operationsHistory) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.balancePLN = balancePLN;
        this.balanceUSD = balanceUSD;
        this.balanceEUR = balanceEUR;
        this.balanceGBP = balanceGBP;
        this.balanceCHF = balanceCHF;
        this.credit = credit;
        this.operationsHistory = operationsHistory;
    }

    public void addToBalancePLN(double amount){
        this.balancePLN += amount;
    }
    public void addToBalanceUSD(double amount){
        this.balanceUSD += amount;
    }
    public void addToBalanceEUR(double amount){
        this.balanceEUR += amount;
    }
    public void addToBalanceGBP(double amount){
        this.balanceGBP += amount;
    }
    public void addToBalanceCHF(double amount){
        this.balanceCHF += amount;
    }
    public void addEntryToOperationsHistory(Operation operation){ this.operationsHistory.add(operation);}

    // Pobierz numer konta
    public String getAccountNumber() {return accountNumber;}

    // Ustaw numer konta
    public void setAccountNumber(String accountNumber)
    {this.accountNumber = accountNumber;}

    // Pobierz imię
    public String getName() {return name;}

    // Ustaw imię
    public void setName(String name)
    {this.name = name;}

    // Pobierz nazwisko
    public String getSurname() {return surname;}

    // Ustaw nazwisko
    public void setSurname(String surname) {this.surname = surname;}

    // Pobierz login
    public String getLogin() {
        return login;
    }

    // Ustaw login
    public void setLogin(String login) {
        this.login = login;
    }

    // Pobierz hasło
    public String getPassword() {
        return password;
    }

    // Ustaw hasło
    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalancePLN() {
        return balancePLN;
    }

    public void setBalancePLN(double balancePLN) {
        this.balancePLN = balancePLN;
    }

    public double getBalanceUSD() {
        return balanceUSD;
    }

    public void setBalanceUSD(double balanceUSD) {
        this.balanceUSD = balanceUSD;
    }

    public double getBalanceEUR() {
        return balanceEUR;
    }

    public void setBalanceEUR(double balanceEUR) {
        this.balanceEUR = balanceEUR;
    }

    public double getBalanceGBP() {
        return balanceGBP;
    }

    public void setBalanceGBP(double balanceGBP) {
        this.balanceGBP = balanceGBP;
    }

    public double getBalanceCHF() {
        return balanceCHF;
    }

    public void setBalanceCHF(double balanceCHF) {
        this.balanceCHF = balanceCHF;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public List<Operation> getOperationsHistory() {
        return operationsHistory;
    }

    public void setOperationsHistory(List<Operation> operationsHistory) {
        this.operationsHistory = operationsHistory;
    }
}

