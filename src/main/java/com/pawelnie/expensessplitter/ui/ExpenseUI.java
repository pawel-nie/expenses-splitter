package com.pawelnie.expensessplitter.ui;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.dao.ExpenseRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Component;

@Component
public class ExpenseUI {

    private ExpenseRepo expenseRepo;

    public ExpenseUI() {
    }

    public ExpenseRepo getExpenseRepo() {
        return expenseRepo;
    }

    public void setExpenseRepo(ExpenseRepo expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    void configure(ExpenseRepo expenseRepo){
        this.expenseRepo = expenseRepo;
    }

    public void addExpense(String name){
        Expense expense = new Expense();
        expense.setName(name);
        expenseRepo.save(expense);
    }
    public Button getPrepareExpenseButton(){
        final Button button = new Button("Add new expense", event ->{
            Dialog dialog = new Dialog();
            dialog.add(new Label("New expense"));

            VerticalLayout verticalLayout = new VerticalLayout();
            HorizontalLayout headerHorizontalLayout = new HorizontalLayout();
            HorizontalLayout participantHorizontalLayout = new HorizontalLayout();

            TextField nameTextField = new TextField();
            nameTextField.setPlaceholder("Expense name");

            TextField amountTextField = new TextField();
            amountTextField.setPlaceholder("Expense amount");

            Button acceptButton = new Button("Ok", acceptButtonEvent -> {
                dialog.close();
            });

            headerHorizontalLayout.add(nameTextField);
            headerHorizontalLayout.add(amountTextField);
            headerHorizontalLayout.add(acceptButton);

            verticalLayout.add(headerHorizontalLayout);
            verticalLayout.add(participantHorizontalLayout);

            dialog.add(verticalLayout);
            dialog.setHeight("150px");
            dialog.setWidth("600px");
            dialog.open();
        });
        return button;
    }
}
