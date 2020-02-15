package com.pawelnie.expensessplitter.calc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Occasion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private ArrayList<Expense> expensesList;
    private ArrayList<Person> personsList;

    public Occasion(){};

    public Occasion(Long id, String name){
        this.id = id;
        this.name = name;
        this.expensesList = new ArrayList<>();
        this.personsList = new ArrayList<>();
    }

    public Occasion(Long id, String name, ArrayList<Person> personsList){
        this.id = id;
        this.name = name;
        this.personsList = personsList;
        this.expensesList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Expense> getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(ArrayList<Expense> expensesList) {
        this.expensesList = expensesList;
    }

    public ArrayList<Person> getPersonsList() {
        return personsList;
    }

    public void setPersonsList(ArrayList<Person> personsList) {
        this.personsList = personsList;
    }

    public void addPerson(Person person){
        personsList.add(person);
    }

    public void removePerson(Person person){
        personsList.remove(person);
    }

    public void addExpense(Expense expense){
        expensesList.add(expense);
    }

    public void removeExpense(Expense expense){
        expensesList.remove(expense);
    }

    /* Negative balance value means that user needs to get money.
    *  Positive balance value means that user needs to give money.
    */
    public double calculateTotalBalance(Long personId){
        double totalBalance = 0;
        for (Expense expense : expensesList){
            totalBalance += expense.calculateBalance(personId);
        }
        return totalBalance;
    }
}

