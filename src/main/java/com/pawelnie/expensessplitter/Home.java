package com.pawelnie.expensessplitter;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class Home extends AppLayout {

    public Home(){

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(getAddOccasionButton());

        setContent(horizontalLayout);
    }

    private Button getAddOccasionButton(){
        final Button button = new Button("Add new occasion", event -> {
            Dialog dialog = new Dialog();
            dialog.add(new Label("New occasion"));

            VerticalLayout verticalLayout = new VerticalLayout();
            TextField textField = new TextField();
            textField.setPlaceholder("occasion name");
            Button acceptButton = new Button("Ok");

            verticalLayout.add(textField);
            verticalLayout.add(acceptButton);

            dialog.add(verticalLayout);
            dialog.setHeight("150px");
            dialog.setWidth("400px");
            dialog.open();
        });

        return button;
    }
}
