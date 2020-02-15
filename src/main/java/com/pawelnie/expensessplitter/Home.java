package com.pawelnie.expensessplitter;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.dao.ExpenseRepo;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class Home extends AppLayout {

    private ExpenseRepo expenseRepo;

    @Autowired
    public Home(ExpenseRepo expenseRepo){
        this.expenseRepo = expenseRepo;

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(getPrepareOccasionButton());

        setContent(horizontalLayout);
    }

    private Button getPrepareOccasionButton(){
        final Button button = new Button("Add new occasion", buttonEvent -> {
            Dialog dialog = new Dialog();
            dialog.add(new Label("New occasion"));

            VerticalLayout verticalLayout = new VerticalLayout();
            TextField textField = new TextField();
            textField.setPlaceholder("Occasion name");
            Button acceptButton = new Button("Ok", acceptButtonEvent -> {
                addExpense(textField.getValue());
            });

            verticalLayout.add(textField);
            verticalLayout.add(acceptButton);

            dialog.add(verticalLayout);
            dialog.setHeight("150px");
            dialog.setWidth("400px");
            dialog.open();
        });

        return button;
    }

    public void addExpense(String name){
        Expense expense = new Expense();
        expense.setName(name);
        expenseRepo.save(expense);
    }
}
