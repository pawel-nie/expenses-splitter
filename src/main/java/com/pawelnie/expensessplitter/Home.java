package com.pawelnie.expensessplitter;

import com.pawelnie.expensessplitter.calc.Expense;
import com.pawelnie.expensessplitter.calc.Occasion;
import com.pawelnie.expensessplitter.calc.Person;
import com.pawelnie.expensessplitter.dao.ExpenseRepo;
import com.pawelnie.expensessplitter.dao.OccasionRepo;
import com.pawelnie.expensessplitter.dao.PersonRepo;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Route
public class Home extends AppLayout {

    private ExpenseRepo expenseRepo;
    private OccasionRepo occasionRepo;
    private PersonRepo personRepo;
    private TextArea textArea = new TextArea("Saved occasions");
    private Grid<Expense> expensesGrid = new Grid<>();

    private Grid<Occasion> occasionsGrid = new Grid<>();
    List<Occasion> occasionsList = new ArrayList<>();
    private Occasion chosenOccasion = null;

    @Autowired
    public Home(ExpenseRepo expenseRepo, OccasionRepo occasionRepo, PersonRepo personRepo){
        this.expenseRepo = expenseRepo;
        this.occasionRepo = occasionRepo;
        this.personRepo = personRepo;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout occasionVerticalLayout = new VerticalLayout();
        VerticalLayout personsAndExpensesVerticalLayout = new VerticalLayout();

        configureOccasionsGrid();
        occasionVerticalLayout.add(occasionsGrid);
        occasionVerticalLayout.add(getPrepareOccasionButton());

        personsAndExpensesVerticalLayout.add(getPreparePersonButton());
        personsAndExpensesVerticalLayout.add(getPrepareExpenseButton());

        horizontalLayout.add(occasionVerticalLayout);
        horizontalLayout.add(personsAndExpensesVerticalLayout);
        setContent(horizontalLayout);
    }

    private Button getPrepareOccasionButton(){
        final Button button = new Button("Add new occasion", buttonEvent -> {
            Dialog dialog = new Dialog();
            dialog.add(new Label("New occasion"));

            VerticalLayout verticalLayout = new VerticalLayout();
            TextField textField = new TextField();
            textField.setPlaceholder("Occasion name");
            Button acceptButton = new Button("Ok", acceptButtonEvent ->     {
                addOccasion(textField.getValue());
                occasionsGrid.getDataProvider().refreshAll();
                dialog.close();
            });
            acceptButton.addClickShortcut(Key.ENTER);

            verticalLayout.add(textField);
            verticalLayout.add(acceptButton);

            dialog.add(verticalLayout);
            dialog.setHeight("150px");
            dialog.setWidth("400px");
            dialog.open();

            textField.focus();
        });
        return button;
    }

    private void populateOccasionsList(){
        occasionsList = (ArrayList<Occasion>)occasionRepo.findAll();
    }

    private void configureOccasionsGrid(){
        populateOccasionsList();
//        final AtomicReference<Occasion> atomicChosenOccasion = new AtomicReference<>();
//        atomicChosenOccasion.set(null);

        occasionsGrid.asSingleSelect().addValueChangeListener(event -> {
//            String message = String.format("Selection changed from %s to %s",
//                    event.getOldValue().getName(), event.getValue().getName());
//            atomicChosenOccasion.set(event.getValue());
        });

        occasionsGrid.setItems(occasionsList);
        occasionsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        occasionsGrid.addColumn(Occasion::getId).setHeader("Nr");
        occasionsGrid.addColumn(Occasion::getName).setHeader("name");
    }

    private void selectOccasionFromGrid(HasValue.ValueChangeEvent event){
        event.getValue();

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
    public void addPerson(String name){
        Person person = new Person();
        person.setName(name);

        Set<Occasion> chosenOccasionsSet = occasionsGrid.getSelectedItems();
        Iterator<Occasion> iterator = chosenOccasionsSet.iterator();

        if (iterator.hasNext()) {
            chosenOccasion = iterator.next();
            person.setOccasion(chosenOccasion);
        }
        personRepo.save(person);
    }
    private Button getPreparePersonButton(){
        final Button button = new Button("Add new person", event ->{
            Dialog dialog = new Dialog();
            dialog.add(new Label("New person"));

            VerticalLayout verticalLayout = new VerticalLayout();

            TextField textField = new TextField();
            textField.setPlaceholder("Person name");

            Button acceptButton = new Button("Ok", acceptButtonEvent ->{
                addPerson(textField.getValue());
//                occasionsGrid.getDataProvider().refreshAll();
                dialog.close();
            });
            acceptButton.addClickShortcut(Key.ENTER);

            verticalLayout.add(textField);
            verticalLayout.add(acceptButton);

            dialog.add(verticalLayout);
            dialog.setHeight("150px");
            dialog.setWidth("400px");
            dialog.open();
        });
        return button;
    }

    private Button getPrepareExpenseButton(){
        Button button = new Button("Add new expense");
        return button;
    }
}
