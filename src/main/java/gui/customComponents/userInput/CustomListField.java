package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import de.dhbwka.swe.utils.model.IDepictable;
import gui.customComponents.CustomTableComponent;
import model.Kunde;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomListField extends CustomInputField {

//    private Class aClass;
    private SimpleListComponent slc;

    public CustomListField(String title, String placeholder, IGUIEventListener observer) {
        this.title = title;
        this.placeholder = placeholder;
        this.value = "";

        //InitUI
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,50));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        slc = SimpleListComponent.builder("okayy")
                .title(title)
                .observer(observer)
                .componentSize(new Dimension(300,200))
                .build();


        this.add(slc, BorderLayout.CENTER);
    }

    public void setListElements(List<IDepictable> objectList) {
        slc.setListElements(objectList, true);
    }

    @Override
    public void setValue(String value) {

    }
}
