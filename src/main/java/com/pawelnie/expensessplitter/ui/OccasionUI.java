package com.pawelnie.expensessplitter.ui;

import com.pawelnie.expensessplitter.calc.Occasion;
import com.pawelnie.expensessplitter.dao.OccasionRepo;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class OccasionUI {

    private OccasionRepo occasionRepo;
    List<Occasion> occasionsList = new ArrayList<>();
    Grid<Occasion> occasionsGrid = new Grid<>();

    public OccasionUI() {
    }

    public OccasionRepo getOccasionRepo() {
        return occasionRepo;
    }

    public void setOccasionRepo(OccasionRepo occasionRepo) {
        this.occasionRepo = occasionRepo;
    }

    public List<Occasion> getOccasionsList() {
        return occasionsList;
    }

    public void setOccasionsList(List<Occasion> occasionsList) {
        this.occasionsList = occasionsList;
    }

    public Grid<Occasion> getOccasionsGrid() {
        return occasionsGrid;
    }

    public void setOccasionsGrid(Grid<Occasion> occasionsGrid) {
        this.occasionsGrid = occasionsGrid;
    }

    void configure(Grid<Occasion> occasionsGrid, OccasionRepo occasionRepo){
        this.occasionsGrid = occasionsGrid;
        this.occasionRepo = occasionRepo;
    }
    private void selectOccasionFromGrid(HasValue.ValueChangeEvent event){
        event.getValue();
    }
    Button getPrepareOccasionButton(){
        final Button button = new Button("Add new occasion", buttonEvent -> {
            Dialog dialog = new Dialog();
            dialog.add(new Label("New occasion"));

            VerticalLayout verticalLayout = new VerticalLayout();
            TextField textField = new TextField();
            textField.setPlaceholder("Occasion name");
            Button acceptButton = new Button("Ok", acceptButtonEvent -> {
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
    Button getDeleteOccasionButton(){
        final Button button = new Button("Delete occasion", buttonEvent -> {

            Set<Occasion> selectedOccasionSet = occasionsGrid.getSelectedItems();
            Iterator<Occasion> iterator = selectedOccasionSet.iterator();
            Notification notification;

            Occasion selectedOccasion = new Occasion();
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
    void configureOccasionsGrid(){
        populateOccasionsList();

        occasionsGrid.setItems(occasionsList);
        occasionsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        occasionsGrid.addColumn(Occasion::getId).setHeader("Nr");
        occasionsGrid.addColumn(Occasion::getName).setHeader("name");
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
