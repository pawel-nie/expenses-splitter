package com.pawelnie.expensessplitter.ui;

import com.pawelnie.expensessplitter.calc.Occasion;
import com.pawelnie.expensessplitter.calc.Person;
import com.pawelnie.expensessplitter.dao.PersonRepo;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

@Component
public class PersonButton {

    private PersonRepo personRepo;
    private Grid<Occasion> occasionsGrid;

    public PersonButton(){
    }

    public PersonRepo getPersonRepo() {
        return personRepo;
    }

    public void setPersonRepo(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public Grid<Occasion> getOccasionsGrid() {
        return occasionsGrid;
    }

    public void setOccasionsGrid(Grid<Occasion> occasionsGrid) {
        this.occasionsGrid = occasionsGrid;
    }

    private void addPerson(String name){
        Person person = new Person();
        person.setName(name);

        Set<Occasion> selectedOccasionSet = occasionsGrid.getSelectedItems();
        Iterator<Occasion> iterator = selectedOccasionSet.iterator();
        Notification notification;

        Occasion selectedOccasion;
        if (iterator.hasNext()) {
            selectedOccasion = iterator.next();
            person.setOccasion(selectedOccasion);
            personRepo.save(person);
            notification = Notification.show(
                    "Person " + person.getName() + " added to " + selectedOccasion.getName());
        } else {
            notification = Notification.show(
                    "Person was not added beacause no occasion was selected");
        }
    }

    void configure(Grid<Occasion> occasionsGrid, PersonRepo personRepo){
        setOccasionsGrid(occasionsGrid);
        setPersonRepo(personRepo);
    }

    Button getPreparePersonButton(){
        final Button button = new Button("Add new person", event ->{
            Dialog dialog = new Dialog();
            dialog.add(new Label("New person"));

            VerticalLayout verticalLayout = new VerticalLayout();

            TextField textField = new TextField();
            textField.setPlaceholder("Person name");

            Button acceptButton = new Button("Ok", acceptButtonEvent ->{
                addPerson(textField.getValue());
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
}
