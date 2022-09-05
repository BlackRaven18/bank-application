package com.bank;

public class Credit {

    public static final double INTEREST_RATE = 0.02;
    private double monthlyIncome, monthlyCosts, propertyValue, amountOfCredit, periodOfCredit;
    private boolean isApproved;

    // calkowita kwota kredytu, kwota odsetek, rata kredytu, środki na spłatę
    private double finalCreditCost, amountOfInterest, creditRate, fundsToPayOff;

    public Credit(){this(0.0, 0.0, 0.0, 0.0, 0.0, false);}

    public Credit(double monthlyIncome, double monthlyCosts, double propertyValue, double amountOfCredit, double periodOfCredit, boolean isApproved) {
        this.monthlyIncome = monthlyIncome;
        this.monthlyCosts = monthlyCosts;
        this.propertyValue= propertyValue;
        this.amountOfCredit = amountOfCredit;
        this.periodOfCredit = periodOfCredit;
        this.isApproved = isApproved;

        calculateFinalCreditCost();
        calculateAmountOfInterest();
        calculateCreditRate();
        calculateFundsToPayOff();
    }

    private void calculateFinalCreditCost(){
        finalCreditCost = amountOfCredit * Math.pow(1.0 + INTEREST_RATE, (int)periodOfCredit);
    }

    private void calculateAmountOfInterest(){
        amountOfInterest = finalCreditCost - amountOfInterest;
    }

    private void calculateCreditRate(){
        creditRate = finalCreditCost / (int)periodOfCredit / 12;
    }

    private void calculateFundsToPayOff(){
        fundsToPayOff = monthlyIncome - monthlyCosts;
    }



    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getMonthlyCosts() {
        return monthlyCosts;
    }

    public void setMonthlyCosts(double monthlyCosts) {
        this.monthlyCosts = monthlyCosts;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public double getAmountOfCredit() {
        return amountOfCredit;
    }

    public void setAmountOfCredit(double amountOfCredit) {
        this.amountOfCredit = amountOfCredit;
    }

    public double getPeriodOfCredit() {
        return periodOfCredit;
    }

    public void setPeriodOfCredit(double periodOfCredit) {
        this.periodOfCredit = periodOfCredit;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public double getFinalCreditCost() {
        return finalCreditCost;
    }

    public void setFinalCreditCost(double finalCreditCost) {
        this.finalCreditCost = finalCreditCost;
    }

    public double getAmountOfInterest() {
        return amountOfInterest;
    }

    public void setAmountOfInterest(double amountOfInterest) {
        this.amountOfInterest = amountOfInterest;
    }

    public double getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(double creditRate) {
        this.creditRate = creditRate;
    }

    public double getFundsToPayOff() {
        return fundsToPayOff;
    }

    public void setFundsToPayOff(double fundsToPayOff) {
        this.fundsToPayOff = fundsToPayOff;
    }


}
