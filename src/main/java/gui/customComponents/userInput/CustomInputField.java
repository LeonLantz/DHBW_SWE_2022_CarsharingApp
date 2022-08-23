package gui.customComponents.userInput;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.gui.ObservableComponent;

public abstract class CustomInputField extends ObservableComponent implements ICustomInputField {

    public enum Commands implements EventCommand {

        ADD_BILD("GUIFahrzeugAnlegen.addBild", String[].class),
        ADD_DOKUMENT("GUIFahrzeugAnlegen.addDokument", String[].class);

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

    public String title, placeholder, value;

    public String getTitle() {
        return title;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValue() { return value; };

}
