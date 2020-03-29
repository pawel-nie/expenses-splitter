package com.pawelnie.expensessplitter.ui;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.calc.Occasion;
import com.pawelnie.expensessplitter.calc.Person;
import com.pawelnie.expensessplitter.dao.ExpenseRepo;
import com.pawelnie.expensessplitter.dao.OccasionRepo;
import com.pawelnie.expensessplitter.dao.PersonRepo;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Route
public class Home extends AppLayout {

    private ExpenseRepo expenseRepo;
    private OccasionRepo occasionRepo;
    private PersonRepo personRepo;
    private TextArea textArea = new TextArea("Saved occasions");
    private Grid<Expense> expensesGrid = new Grid<>();

    Grid<Occasion> occasionsGrid = new Grid<>();

    private PersonButton personButton;
    private OccasionUI occasionUI;

    @Autowired
    public Home(ExpenseRepo expenseRepo, OccasionRepo occasionRepo, PersonRepo personRepo,
                PersonButton personButton, OccasionUI occasionUI){
        this.expenseRepo = expenseRepo;
        this.occasionRepo = occasionRepo;
        this.personButton = personButton;
        this.occasionUI = occasionUI;

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout occasionVerticalLayout = new VerticalLayout();
        HorizontalLayout occasionsOperationsHorizontalLayout =
                new HorizontalLayout();
        VerticalLayout personsAndExpensesVerticalLayout = new VerticalLayout();

        occasionUI.configure(occasionsGrid, occasionRepo);
        occasionUI.configureOccasionsGrid();
        occasionsOperationsHorizontalLayout.add(occasionUI.getPrepareOccasionButton());
        occasionsOperationsHorizontalLayout.add(occasionUI.getDeleteOccasionButton());
        occasionVerticalLayout.add(occasionsGrid);
        occasionVerticalLayout.add(occasionsOperationsHorizontalLayout);

        personButton.configure(occasionsGrid, personRepo);
        personsAndExpensesVerticalLayout.add(personButton.getPreparePersonButton());
        personsAndExpensesVerticalLayout.add(getPrepareExpenseButton());

        horizontalLayout.add(occasionVerticalLayout);
        horizontalLayout.add(personsAndExpensesVerticalLayout);
        setContent(horizontalLayout);
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
//                addOccasion(textField.getValue());
//                occasionsGrid.getDataProvider().refreshAll();
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
