/*
 * Jonathan Wyatt
 * Radio Buttons
 */
package bitbuttons;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//Base Frame class.  Determines the primary attributes of the GUI frame
class FrameBase extends JFrame {

    static final int WIDTH = 200, HEIGHT = 200;

    public FrameBase() {
        super("Frame Base");
        setFrame(WIDTH, HEIGHT);
    }

    public FrameBase(String title) {
        super(title);
        setFrame(WIDTH, HEIGHT);
    }

    public FrameBase(String title, int width, int height) {
        super(title);
        setFrame(width, height);
    }

    public void display() {
        setVisible(true);
    }

    private void setFrame(int width, int height) {
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

//builds and defines checkboxes.  each box represents a bit in an 8 bit binary number
class CheckPanel extends JPanel implements ItemListener {

    private BitButtonPanel sourcePanel;
    private JCheckBox[] checkBoxArray;
    private int bitsTotal;
    private String labels[] = {"128", "64", "32", "16", "8", "4", "2", "1"};

    public CheckPanel(BitButtonPanel sourcePanel) {
        this.sourcePanel = sourcePanel;
        checkBoxArray = new JCheckBox[8];

        for (int i = 0; i < labels.length; i++) {
            checkBoxArray[i] = new JCheckBox(labels[i]);
            checkBoxArray[i].setHorizontalAlignment(JCheckBox.CENTER);
            checkBoxArray[i].addItemListener(this);

            add(checkBoxArray[i]);

        }

    }
    //handles when a checkbox is checked or unchecked.
    public void itemStateChanged(ItemEvent e) {

        JCheckBox source = (JCheckBox) e.getSource();
        for (int i = 0; i < labels.length; i++) {
            if (source.getText().equals(labels[i]) && e.getStateChange() == ItemEvent.SELECTED) {
                sourcePanel.textPanel.setTextField(i, "1");
            } else if (source.getText().equals(labels[i])) {
                sourcePanel.textPanel.setTextField(i, "0");
            }
        }
    }
    //totals up the bits and coverts from binary to decimal
    public int getBitsTotal() {
        bitsTotal = 0;
        for (JCheckBox aCheckBoxArray : checkBoxArray) {
            if (aCheckBoxArray.isSelected()) {

                bitsTotal = bitsTotal + Integer.parseInt(aCheckBoxArray.getText());
            }
        }
        return bitsTotal;
    }

}
//defines the text boxes used to represent the binary number
class TextPanel extends JPanel {

    private BitButtonPanel sourcePanel;
    private JTextField[] textFieldArray;

    public TextPanel(BitButtonPanel sourcePanel) {

        textFieldArray = new JTextField[8];
        for (int i = 0; i < textFieldArray.length; i++) {
            textFieldArray[i] = new JTextField("0", 3);
            textFieldArray[i].setEditable(false);
            add(textFieldArray[i]);

        }

    }

    public void setTextField(int index, String text) {

        textFieldArray[index].setText(text);
    }
}

//output for the numbers converted from binary.  Converts to Decimal, Hexiaecimal
// and Octal.
class OutputPanel extends JPanel {

    private BitButtonPanel sourcePanel;
    private JLabel decimalFieldLabel = new JLabel("Decimal", JLabel.CENTER);
    private JTextField decimalFieldText = new JTextField("", JTextField.CENTER);
    private JLabel hexFieldLabel = new JLabel("Hexadecimal", JLabel.CENTER);
    private JTextField hexFieldText = new JTextField("", JTextField.CENTER);
    private JLabel octFieldLabel = new JLabel("Octal", JLabel.CENTER);
    private JTextField octFieldText = new JTextField("", JTextField.CENTER);

    public OutputPanel(BitButtonPanel sourcePanel) {

        this.sourcePanel = sourcePanel;
        decimalFieldText.setHorizontalAlignment(JTextField.CENTER);
        decimalFieldText.setEditable(false);
        hexFieldText.setHorizontalAlignment(JTextField.CENTER);
        hexFieldText.setEditable(false);
        octFieldText.setHorizontalAlignment(JTextField.CENTER);
        octFieldText.setEditable(false);
        setLayout(new GridLayout(6, 1));
        add(decimalFieldLabel);
        add(decimalFieldText);
        add(hexFieldLabel);
        add(hexFieldText);
        add(octFieldLabel);
        add(octFieldText);
    }
    public void setDecimal(int number){
        this.decimalFieldText.setText(String.valueOf(number));
        this.hexFieldText.setText("");
        this.octFieldText.setText("");
    }
    
    public void setHexadecimal(int number){
        this.hexFieldText.setText(Integer.toHexString(number));
        this.octFieldText.setText("");
        this.decimalFieldText.setText("");
    }
    
    public void setOctal(int number){
        this.octFieldText.setText(Integer.toOctalString(number));
        this.hexFieldText.setText("");
        this.decimalFieldText.setText("");
        
    }
}

//radio buttons used to choose between the three number systems.
class RadioButtonPanel extends JPanel {

    private BitButtonPanel sourcePanel;
    private JRadioButton decimalRadio = new JRadioButton("Decimal", true);
    private JRadioButton hexadecimalRadio = new JRadioButton("Hexadecimal");
    private JRadioButton octalRadio = new JRadioButton("Octal");
    private ButtonGroup buttonGroup;

    public RadioButtonPanel(BitButtonPanel sourcePanel) {
        this.sourcePanel = sourcePanel;
        add(decimalRadio);
        add(hexadecimalRadio);
        add(octalRadio);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(decimalRadio);
        buttonGroup.add(hexadecimalRadio);
        buttonGroup.add(octalRadio);
    }

    public String getRadio() {
        String returnButton = "Decimal";
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements();
                buttons.hasMoreElements();) {
            AbstractButton currentButton = buttons.nextElement();

            if (currentButton.isSelected()) {
                returnButton = currentButton.getText();
            }

        }
        return returnButton;
    }

}

//button that initiates the conversion
class SubmitButton extends JPanel {

    BitButtonPanel sourcePanel;
    private JButton submitButton = new JButton("Submit");

    public SubmitButton(BitButtonPanel sourcePanel) {
        this.sourcePanel = sourcePanel;
        submitButton.setHorizontalAlignment(JButton.CENTER);
        add(submitButton);

        submitButton.addActionListener(event -> {
            switch (SubmitButton.this.sourcePanel.radioPanel.getRadio()) {
                case("Decimal"): SubmitButton.this.sourcePanel.
                        outputPanel.setDecimal(SubmitButton.this.
                                sourcePanel.checkPanel.getBitsTotal());
                                 break;

                case("Hexadecimal"): SubmitButton.this.sourcePanel.
                        outputPanel.setHexadecimal(SubmitButton.this.
                                sourcePanel.checkPanel.getBitsTotal());
                                     break;

                case("Octal"): SubmitButton.this.sourcePanel.
                        outputPanel.setOctal(SubmitButton.this.
                                sourcePanel.checkPanel.getBitsTotal());
                               break;

                default: SubmitButton.this.sourcePanel.
                        outputPanel.setDecimal(SubmitButton.this.
                                sourcePanel.checkPanel.getBitsTotal());
                         break;

            }
        });
    }

}

//Panel used to orgainize and contain the GUI elements in a top-down format
class BitButtonPanel extends JPanel {

    public CheckPanel checkPanel = new CheckPanel(this);
    public TextPanel textPanel = new TextPanel(this);
    public OutputPanel outputPanel = new OutputPanel(this);
    public RadioButtonPanel radioPanel = new RadioButtonPanel(this);
    public SubmitButton submitPanel = new SubmitButton(this);
    //builds out the GUI in the main panel

    public BitButtonPanel() {

        try {
            //Set System L&F if possible
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException |
                ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            System.err.println("Can't use the system look and feel on this platform.");
            System.err.println("Using the default look and feel.");
        }

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(checkPanel);
        add(textPanel);
        add(radioPanel);
        add(submitPanel);
        add(outputPanel);

    }

}

public class BitButtons extends FrameBase {

    public BitButtons() {
        super("Bit Buttons", 375, 300);
        add(new BitButtonPanel());
    }

    public static void main(String[] args) {

        BitButtons mainApplication
                = new BitButtons();
        mainApplication.display();
    }

}
