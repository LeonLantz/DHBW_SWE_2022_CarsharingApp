package gui.customComponents.userInput;

import de.dhbwka.swe.utils.gui.ObservableComponent;

public abstract class CustomInputField extends ObservableComponent implements ICustomInputField {

    public String title, placeholder, value;

    public String getTitle() {
        return title;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValue() { return value; };


}
