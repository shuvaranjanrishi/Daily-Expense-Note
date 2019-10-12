package com.example.asus.dailyexpensenote.model_class;

public class Expense {

    private int id;
    private String expenseType;
    private String expenseAmount;
    private String expenseDate;
    private String expenseTime;

    public Expense(String expenseType, String expenseAmount, String expenseDate, String expenseTime) {
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseTime = expenseTime;
    }

    public Expense(int id, String expenseType, String expenseAmount, String expenseDate, String expenseTime) {
        this.id = id;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseTime = expenseTime;
    }

    public int getId() {
        return id;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseTime() {
        return expenseTime;
    }
}
