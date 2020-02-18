package com.pawelnie.expensessplitter;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.calc.Occasion;
import com.pawelnie.expensessplitter.dao.ExpenseRepo;
import com.pawelnie.expensessplitter.dao.OccasionRepo;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;

@Route
public class Home extends AppLayout {

    private ExpenseRepo expenseRepo;
    private OccasionRepo occasionRepo;
    private TextArea textArea = new TextArea("Saved occasions");
    private Grid<Expense> expensesGrid = new Grid<>();

    private Grid<Occasion> occasionsGrid = new Grid<>();
    List<Occasion> occasionsList = new ArrayList<>();

    @Autowired
    public Home(ExpenseRepo expenseRepo, OccasionRepo occasionRepo){
        this.expenseRepo = expenseRepo;
        this.occasionRepo = occasionRepo;

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
//        verticalLayout.add(getOccasionsTextArea());

        populateOccasionsList();
        configureOccasionsGrid();
        verticalLayout.add(occasionsGrid);

        verticalLayout.add(getPrepareOccasionButton());
        horizontalLayout.add(verticalLayout);
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
                addOccasion(textField.getValue());
                occasionsGrid.getDataProvider().refreshAll();
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

    private void populateOccasionsList(){
        occasionsList = (ArrayList<Occasion>)occasionRepo.findAll();
    }

    private void configureOccasionsGrid(){
        occasionsGrid.setItems(occasionsList);
        occasionsGrid.addColumn(Occasion::getId).setHeader("Nr");
        occasionsGrid.addColumn(Occasion::getName).setHeader("name");

        occasionsGrid.asSingleSelect().addValueChangeListener(event -> {
            String message = String.format("Selection changed from %s to %s",
                    event.getOldValue(), event.getValue());
        });
    }

    public void addExpense(String name){
        Expense expense = new Expense();
        expense.setName(name);
        expenseRepo.save(expense);
    }
    public void addOccasion(String name){
        Occasion occasion = new Occasion();
        occasion.setName(name);
        occasionsList.add(occasion);
        occasionRepo.save(occasion);
    }
}
