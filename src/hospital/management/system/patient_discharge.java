package hospital.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class patient_discharge extends JFrame {

    static final Color BRAND_TEAL  = new Color(0, 109, 119);
    static final Color BG_PAGE     = new Color(240, 244, 248);
    static final Color TEXT_DARK   = new Color(26, 31, 54);
    static final Color TEXT_MUTED  = new Color(132, 146, 166);
    static final Color DANGER      = new Color(224, 92, 92);
    static final Color BORDER_CLR  = new Color(226, 232, 240);

    patient_discharge() {
        setUndecorated(true);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_PAGE);
        root.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        add(root);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));
        header.setBorder(new EmptyBorder(0, 30, 0, 30));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        JLabel mainTitle = new JLabel("Patient Discharge Portal");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainTitle.setForeground(TEXT_DARK);
        JLabel subTitle = new JLabel("Verify records and finalize patient exit");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subTitle.setForeground(TEXT_MUTED);
        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);
        header.add(titlePanel, BorderLayout.WEST);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        closeBtn.setForeground(TEXT_MUTED);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        header.add(closeBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 30, 0));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblName    = new JLabel("—");
        JLabel lblRoom    = new JLabel("—");
        JLabel lblDisease = new JLabel("—");
        JLabel lblInTime  = new JLabel("—");
        styleValueLabel(lblName);
        styleValueLabel(lblRoom);
        styleValueLabel(lblDisease);
        styleValueLabel(lblInTime);

        JPanel leftPanel = createCard();
        leftPanel.add(createSectionHeader("PATIENT IDENTIFICATION"));

        Choice choice = new Choice();
        choice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadPatientIDs(choice);
        leftPanel.add(createFieldRow("Select Patient ID (Number):", choice));

        leftPanel.add(Box.createVerticalStrut(25));
        leftPanel.add(createSectionHeader("ADMISSION RECORD"));
        leftPanel.add(createDisplayRow("Patient Name:", lblName));
        leftPanel.add(createDisplayRow("Diagnosis/Disease:", lblDisease));
        leftPanel.add(createDisplayRow("Allocated Room:", lblRoom));
        leftPanel.add(createDisplayRow("Check-In Time:", lblInTime));

        JPanel rightPanel = createCard();
        rightPanel.add(createSectionHeader("DISCHARGE TIMING"));

        JLabel lblOutTime = new JLabel(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new Date()));
        styleValueLabel(lblOutTime);
        lblOutTime.setForeground(BRAND_TEAL);
        rightPanel.add(createDisplayRow("Check-Out Time:", lblOutTime));

        rightPanel.add(Box.createVerticalGlue());

        JButton btnCheck = createActionBtn("FETCH PATIENT DATA", BRAND_TEAL);
        JButton btnConfirm = createActionBtn("FINALIZE DISCHARGE", DANGER);
        JButton btnBack = createActionBtn("BACK TO DASHBOARD", new Color(100, 116, 139));

        rightPanel.add(btnCheck);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnConfirm);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnBack);

        btnCheck.addActionListener(e -> {
            try {
                connection c = new connection();
                ResultSet rs = c.statement.executeQuery("select * from Patient_Info where number = '"+choice.getSelectedItem()+"'");
                if (rs.next()) {
                    lblName.setText(rs.getString("Name"));
                    lblDisease.setText(rs.getString("Patient_Disease")); // Fetching Disease
                    lblRoom.setText(rs.getString("Room_Number"));
                    lblInTime.setText(rs.getString("Time"));
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        btnConfirm.addActionListener(e -> {
            if (lblRoom.getText().equals("—")) {
                JOptionPane.showMessageDialog(this, "Please fetch patient data first!");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this, "Confirm discharge for " + lblName.getText() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    connection c = new connection();
                    c.statement.executeUpdate("delete from Patient_Info where number = '"+choice.getSelectedItem()+"'");
                    c.statement.executeUpdate("update room set Availability = 'Available' where Room_Number = '"+lblRoom.getText()+"'");
                    JOptionPane.showMessageDialog(null, "Patient Discharged Successfully.");
                    dispose();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        btnBack.addActionListener(e -> dispose());

        body.add(leftPanel);
        body.add(rightPanel);
        root.add(body, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createCard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1),
                new EmptyBorder(25, 25, 25, 25)));
        return p;
    }

    private JLabel createSectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(BRAND_TEAL);
        l.setBorder(new EmptyBorder(0, 0, 15, 0));
        return l;
    }

    private JPanel createFieldRow(String labelText, Component comp) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(TEXT_DARK);
        p.add(l, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        return p;
    }

    private JPanel createDisplayRow(String labelText, JLabel valLabel) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 0, 8, 0));
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(TEXT_MUTED);
        p.add(l, BorderLayout.WEST);
        p.add(valLabel, BorderLayout.EAST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return p;
    }

    private void styleValueLabel(JLabel l) {
        l.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        l.setForeground(TEXT_DARK);
    }

    private JButton createActionBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadPatientIDs(Choice c) {
        try {
            connection con = new connection();
            ResultSet rs = con.statement.executeQuery("select number from Patient_Info");
            while (rs.next()) { c.add(rs.getString("number")); }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) { new patient_discharge(); }
}