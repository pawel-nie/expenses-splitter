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
    List<Occasion> occasionsList = new ArrayList<>();
    private Occasion selectedOccasion = null;

    private PersonButton personButton;

    @Autowired
    public Home(ExpenseRepo expenseRepo, OccasionRepo occasionRepo, PersonRepo personRepo, PersonButton personButton){
        this.expenseRepo = expenseRepo;
        this.occasionRepo = occasionRepo;
        this.personButton = personButton;

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout occasionVerticalLayout = new VerticalLayout();
        HorizontalLayout occasionsOperationsHorizontalLayout =
                new HorizontalLayout();
        VerticalLayout personsAndExpensesVerticalLayout = new VerticalLayout();

        configureOccasionsGrid();
        occasionsOperationsHorizontalLayout.add(getPrepareOccasionButton());
        occasionsOperationsHorizontalLayout.add(getDeleteOccasionButton());
        occasionVerticalLayout.add(occasionsGrid);
        occasionVerticalLayout.add(occasionsOperationsHorizontalLayout);

        personButton.configPersonButton(occasionsGrid, personRepo);
        personsAndExpensesVerticalLayout.add(personButton.getPreparePersonButton());
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
    private Button getDeleteOccasionButton(){
        final Button button = new Button("Delete occasion", buttonEvent -> {

            Set<Occasion> selectedOccasionSet = occasionsGrid.getSelectedItems();
            Iterator<Occasion> iterator = selectedOccasionSet.iterator();
            Notification notification;

            if (iterator.hasNext()) {
                selectedOccasion = iterator.next();
                notification = Notification.show(
                        "Occasion "+selectedOccasion.getName()+" deleted");
            }
            else{
                notification = Notification.show(
                        "No occasion was deleted");
            }

            deleteOccasion(selectedOccasion.getId());
            occasionsGrid.getDataProvider().refreshAll();
        });
        return button;
    }
    private void populateOccasionsList(){
        occasionsList = (ArrayList<Occasion>)occasionRepo.findAll();
    }

    private void configureOccasionsGrid(){
        populateOccasionsList();

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
    public void addOccasion(String name){
        Occasion occasion = new Occasion();
        occasion.setName(name);
        occasionsList.add(occasion);
        occasionRepo.save(occasion);
    }
    public void deleteOccasion(Long id){

        Iterator<Occasion> iter = occasionsList.iterator();
        while(iter.hasNext()){
            Occasion o = iter.next();
            if (o.getId() == id){
                iter.remove();
            }
        }
        occasionRepo.deleteById(id);
    }
}
