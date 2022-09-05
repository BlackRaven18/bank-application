package com.bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Accounts {

    List<Account> list;
    private final String accountsFile = "accounts.xml";

    public Accounts(){
        list = new ArrayList<>();
    }

    public void addAccount(Account account){
        list.add(account);
    }

    public void serializeAccounts(){
        XMLSerialization serialization = new XMLSerialization();
        try{
            serialization.serializeToXML(accountsFile, list);
        } catch(IOException e){
            System.out.println("Nieudana serializacja");
        }
    }

    public void deserializeAccounts(){
        XMLSerialization serialization = new XMLSerialization();
        try{
            list = (List<Account>)serialization.deserializeFromXML(accountsFile);
        } catch(IOException exception){
            System.out.println("Nieudana deserializacja");
        }
    }

    public Account findAccount(String accountNumber,String firstName, String lastName){
        for(Account x : list){
            if(x.getAccountNumber().equals(accountNumber)
                    && x.getName().equals(firstName)
                    && x.getSurname().equals(lastName)){
                return x;
            }
        }
        return null;
    }

    public List<Account> getlist() {
        return list;
    }

    public void setlist(List<Account> list) {
        this.list = list;
    }

}
