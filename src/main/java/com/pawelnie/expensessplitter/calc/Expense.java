package com.pawelnie.expensessplitter.calc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.LinkedHashMap;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double amount;
    private LinkedHashMap<Long, PartialShare> sharesList;

    public Expense() {}

    public Expense(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Expense(Long id, String name, double amount,
                   LinkedHashMap<Long, PartialShare> sharesList){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.sharesList = sharesList;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LinkedHashMap<Long, PartialShare> getSharesList() {
        return sharesList;
    }

    public void setSharesList(LinkedHashMap<Long, PartialShare> sharesList) {
        this.sharesList = sharesList;
    }

    public double calculateBalance (Long personId){
//        return sharesList.get(personId).getContribution()
//               - sharesList.get(personId).getChangeMoney()
//               - (sharesList.get(personId).getPercentUsage() / 100.0) * amount;
        return (sharesList.get(personId).getPercentUsage() / 100.0) * amount
                - sharesList.get(personId).getContribution()
                + sharesList.get(personId).getChangeMoney();
    }
}
