package com.example;

import javax.swing.*;
import java.awt.*;

public class PaymentForm extends JFrame {

    private final JRadioButton rbMale = new JRadioButton("Male");
    private final JRadioButton rbFemale = new JRadioButton("Female");
    private final JRadioButton rbChild = new JRadioButton("Child (0 - 17 years)");

    private final JTextField txtAge = new JTextField(10);
    private final JTextField txtPayment = new JTextField(10);

    public PaymentForm() {
        super("Calculate the Payment for the Patient");

        txtPayment.setEditable(false);

        // group radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(rbMale);
        group.add(rbFemale);
        group.add(rbChild);

        // default selection
        rbMale.setSelected(true);

        JButton btnCalc = new JButton("Calculate");
        btnCalc.addActionListener(e -> calculate());

        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(rbMale);
        top.add(rbFemale);
        top.add(rbChild);

        JPanel mid = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mid.add(new JLabel("Age (Years)"));
        mid.add(txtAge);
        mid.add(btnCalc);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(new JLabel("Payment is"));
        bottom.add(txtPayment);
        bottom.add(new JLabel("euro €"));

        JPanel root = new JPanel();
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.add(top);
        root.add(Box.createVerticalStrut(10));
        root.add(mid);
        root.add(bottom);

        setContentPane(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);
    }

    private void calculate() {
        try {
            int age = Integer.parseInt(txtAge.getText().trim());

            PaymentCalculator.PatientType type = rbChild.isSelected() ? PaymentCalculator.PatientType.CHILD
                    : rbFemale.isSelected() ? PaymentCalculator.PatientType.FEMALE : PaymentCalculator.PatientType.MALE;

            int payment = PaymentCalculator.calculate(type, age);
            txtPayment.setText(String.valueOf(payment));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            txtPayment.setText("");
        }
    }
}
