package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.SimpleListComponent;
import gui.customComponents.CustomTableComponent;
import model.Kunde;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomListField extends CustomInputField {

//    private Class aClass;

    public CustomListField(String title, String placeholder, IGUIEventListener observer, List<Kunde> lstKunde) {
        this.title = title;
        this.placeholder = placeholder;
        this.value = "";

        //InitUI
        this.setLayout(new BorderLayout(0,0));
        this.setPreferredSize(new Dimension(200,50));
        this.setBorder(new EmptyBorder(5,10,5,10));
        this.setBackground(Color.WHITE);

        SimpleListComponent slc = SimpleListComponent.builder("okayy")
                .title(title)
                .componentSize(new Dimension(300,200))
                .build();

        slc.setListElements(lstKunde, true);




//        CustomTableComponent ctc = CustomTableComponent.builder(title+"-Table")
//                .observer(observer)
//                .columnWidths(new int[]{50, 100})
//                .modelClass(Kunde.class)
//                .build();

        this.add(slc, BorderLayout.CENTER);
    }

    @Override
    public void setValue(String value) {

    }
}
