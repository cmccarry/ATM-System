package model;

import java.sql.Date;

public class DebitCard {
    
    //Attributes
    private int cardNum;
    private int pinNum;
    private Date expirationDate;
    private int cvvNum;

    //Constructor
    public DebitCard(int num1, int num2, Date date, int num3) {
        cardNum = num1;
        pinNum = num2;
        expirationDate = date;
        cvvNum = num3;
    }

    //Getters
    public int GetCardNum() {
        return cardNum;
    }
    public int GetPinNum() {
        return pinNum;
    }
    public Date GetExpirationDate() {
        return expirationDate;
    }
    public int GetCvvNum() {
        return cvvNum;
    }

    //Setters
    public void SetCardNum(int num) {
        cardNum = num;
    }
    public void SetPinNum(int num) {
        pinNum = num;
    }
    public void SetExpirationDate(Date date) {
        expirationDate = date;
    }
    public void SetCvvNum(int num) {
        cvvNum = num;
    }
}