package model;

import java.sql.Date;

public class CreditCard {
    
    //Attributes
    private int cardNum;
    private Date expirationDate;
    private int cvvNum;
    private float interestRate;

    //Constructor
    public CreditCard(int num1, Date date, int num2, float rate) {
        cardNum = num1;
        expirationDate = date;
        cvvNum = num2;
        interestRate = rate;
    }

    //Getters
    public int GetCardNum() {
        return cardNum;
    }
    public Date GetExpirationDate() {
        return expirationDate;
    }
    public int GetCvvNum() {
        return cvvNum;
    }
    public float GetInterestRate() {
        return interestRate;
    }

    //Setters
    public void SetCardNum(int num) {
        cardNum = num;
    }
    public void SetExpirationDate(Date date) {
        expirationDate = date;
    }
    public void SetCvvNum(int num) {
        cvvNum = num;
    }
    public void SetInterestRate(float rate) {
        interestRate = rate;
    }
}