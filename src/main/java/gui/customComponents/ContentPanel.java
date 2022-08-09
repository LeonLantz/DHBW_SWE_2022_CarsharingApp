package gui.customComponents;

import de.dhbwka.swe.utils.event.EventCommand;
import de.dhbwka.swe.utils.event.IGUIEventListener;
import de.dhbwka.swe.utils.gui.ObservableComponent;
import de.dhbwka.swe.utils.util.IPropertyManager;
import gui.MainComponentMitNavBar;
import util.CSHelp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class ContentPanel extends ObservableComponent {

    public enum Commands implements EventCommand {

        ADD_ELEMENT( "contentPanel.add_element", String.class );

        public final Class<?> payloadType;
        public final String cmdText;

        Commands(String cmdText, Class<?> payloadType) {
            this.cmdText = cmdText;
            this.payloadType = payloadType;
        }

        @Override
        public String getCmdText() {
            return this.cmdText;
        }

        @Override
        public Class<?> getPayloadType() {
            return this.payloadType;
        }
    }

    private JPanel header, content, footer;
    private JPanel header_west, header_west_searchBar, header_east, footer_east;
    private JLabel header_west_label;
    private JTextField header_west_searchBar_textfield;
    private CustomTableComponent ctc = null;

    //Config
    private String title;
    private MainComponentMitNavBar.NewObjectButton button;

    public ContentPanel() {
    }

    public ContentPanel(String id) {
        super(id);
    }

    private void initUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.setPreferredSize(new Dimension(900, 720));
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createContent(), BorderLayout.CENTER);
        this.add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        //Kopfzeile
        header = new JPanel(new BorderLayout(0,0));
        header.setPreferredSize(new Dimension(900, 130));
        header.setBackground(Color.red);

        // Kopfzeile, Komponente links
        header_west = new JPanel(new BorderLayout(0,0));
        header_west.setBackground(CSHelp.main);
        header_west.setBorder(BorderFactory.createMatteBorder(0,0,1,0, CSHelp.tableDividerColor));
        header.add(header_west, BorderLayout.CENTER);

        header_west_label = new JLabel(this.title);
        header_west_label.setFont(CSHelp.lato_bold.deriveFont(40f));
        header_west_label.setBorder(new EmptyBorder(20, 20, 10, 20));
        header_west.add(header_west_label, BorderLayout.SOUTH);

        header_west_searchBar = new JPanel(new BorderLayout(0,0));
        header_west_searchBar.setBorder(new EmptyBorder(20, 20, 20, 20));
        header_west_searchBar.setBackground(CSHelp.main);

        header_west_searchBar_textfield = new JTextField();
        header_west_searchBar_textfield.setFont(CSHelp.lato.deriveFont(12f));
        header_west_searchBar_textfield.setBounds(10,10,10,10);
        header_west_searchBar_textfield.setPreferredSize(new Dimension(200, 30));
        header_west_searchBar.add(header_west_searchBar_textfield, BorderLayout.WEST);
        header_west.add(header_west_searchBar, BorderLayout.NORTH);

        // Kopfzeile, Komponente rechts
        header_east = new JPanel(new BorderLayout(0,0));
        header_east.setBackground(CSHelp.main);
        header_east.setPreferredSize(new Dimension(130, 130));
        header_east.add(new JLabel(CSHelp.imageList.get("logo.png")));
        header_east.setBorder(BorderFactory.createMatteBorder(0,1,1,0, CSHelp.tableDividerColor));
        header.add(header_east, BorderLayout.EAST);

        return this.header;
    }

    private JPanel createContent() {
        content = new JPanel(new BorderLayout(0,0));
        content.setPreferredSize(new Dimension(900, 508));
        content.setBackground(CSHelp.main);
        if (this.ctc != null) content.add(ctc, BorderLayout.CENTER);
        return this.content;
    }

    private JPanel createFooter() {
        //Fußzeile
        footer = new JPanel(new BorderLayout(20,20));
        footer.setPreferredSize(new Dimension(900, 72));
        footer.setBackground(CSHelp.main);

        //Fußzeile, Komponente rechts
        footer_east = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        footer_east.setPreferredSize(new Dimension(338,50));
        footer_east.add(button);
        footer.add(footer_east, BorderLayout.EAST);

        return this.footer;
    }


    public JPanel getContent() {
        return content;
    }

    public JPanel getFooter() {
        return footer;
    }

    public JPanel getHeader() {
        return header;
    }


    /**
     * *************************************************************************************************
     * The local builder class
     * *************************************************************************************************
     */

    public static SLCBuilder builder(String id ) {
        if( id == null || id.isEmpty() ) throw new IllegalArgumentException( "ID must be given!" );
        SLCBuilder slcBuilder = new SLCBuilder();
        slcBuilder.id = id;
        return slcBuilder;
    }

    public static final class SLCBuilder {

        private String title;
        private CustomTableComponent ctc;
        private MainComponentMitNavBar.NewObjectButton button;

        private IPropertyManager propManager;
        private String id;

        private IGUIEventListener listener;

        private SLCBuilder() {
        }

        public SLCBuilder propManager(IPropertyManager propMgr) {
            this.propManager = propMgr;
            return this;
        }

        public SLCBuilder title(String title) {
            this.title = title;
            return this;
        }

        public SLCBuilder table(CustomTableComponent ctc) {
            this.ctc = ctc;
            return this;
        }

        public SLCBuilder addButton(MainComponentMitNavBar.NewObjectButton button) {
            this.button = button;
            return this;
        }

        /**
         * Assign an initial {@link IGUIEventListener} as observer
         *
         * @param observer the observer
         *
         * @return the current instance for chaining
         */
        public SLCBuilder observer(IGUIEventListener observer ) {
            Objects.requireNonNull( observer, "observer must not be null!" );
            this.listener = observer;
            return this;
        }

        /**
         * Build a ContentPanel instance
         *
         * @return the built ContentPanel instance
         */
        public ContentPanel build() {
            ContentPanel cp = new ContentPanel( this.id );
            cp.setPropertyManager( this.propManager );
            cp.title = this.title;
            cp.button = this.button;
            if( this.ctc != null ) cp.ctc = this.ctc;
            cp.initUI();
            if( this.listener != null ) cp.addObserver(listener);
            return cp;
        }
    }
}
