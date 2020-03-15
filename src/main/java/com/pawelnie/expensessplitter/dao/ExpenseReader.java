package com.pawelnie.expensessplitter.dao;

import com.pawelnie.expensessplitter.calc.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseReader {

    private ExpenseRepo expenseRepo;

    @Autowired
    public ExpenseReader(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    public List<Expense> getAllExpenses(){
        return (List<Expense>)expenseRepo.findAll();
    }
}
