package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NEW_PATIENT extends JFrame implements ActionListener {

    static final Color BRAND      = new Color(0, 109, 119);
    static final Color BG_PAGE    = new Color(240, 244, 248);
    static final Color SIDEBAR    = new Color(26, 31, 54);
    static final Color TEXT_DARK  = new Color(44, 62, 80);

    JComboBox<String> comboBox;
    JTextField textFieldNumber, textName, textFieldDisease, textFieldDeposite;
    JRadioButton r1, r2;
    Choice roomChoice;
    JLabel timeLabel;
    JButton btnAdd, btnBack;

    NEW_PATIENT() {
        setUndecorated(true);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setPreferredSize(new Dimension(300, 650));
        sidebar.setBackground(SIDEBAR);

        JLabel placeholder = new JLabel("➕", SwingConstants.CENTER);
        placeholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        placeholder.setForeground(Color.WHITE);
        sidebar.add(placeholder);
        add(sidebar, BorderLayout.WEST);

        JPanel mainContent = new JPanel(null);
        mainContent.setBackground(BG_PAGE);
        mainContent.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Register New Patient");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(BRAND);
        title.setBounds(40, 30, 400, 40);
        mainContent.add(title);

        int labelX = 40, fieldX = 220, startY = 100, gapY = 50;

        createFormLabel("Identity Type:", labelX, startY, mainContent);
        comboBox = new JComboBox<>(new String[]{"Nid Card", "Driving License", "Passport ID"});
        comboBox.setBounds(fieldX, startY, 250, 30);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainContent.add(comboBox);

        createFormLabel("ID Number:", labelX, startY + gapY, mainContent);
        textFieldNumber = createStyledTextField(fieldX, startY + gapY, 250, mainContent);

        createFormLabel("Patient Name:", labelX, startY + (gapY * 2), mainContent);
        textName = createStyledTextField(fieldX, startY + (gapY * 2), 250, mainContent);

        createFormLabel("Gender:", labelX, startY + (gapY * 3), mainContent);
        r1 = new JRadioButton("Male");
        r1.setBounds(fieldX, startY + (gapY * 3), 80, 30);
        r1.setOpaque(false);
        r1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        r2 = new JRadioButton("Female");
        r2.setBounds(fieldX + 90, startY + (gapY * 3), 100, 30);
        r2.setOpaque(false);
        r2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1); bg.add(r2);
        mainContent.add(r1); mainContent.add(r2);

        createFormLabel("Diagnosis:", labelX, startY + (gapY * 4), mainContent);
        textFieldDisease = createStyledTextField(fieldX, startY + (gapY * 4), 250, mainContent);

        createFormLabel("Assign Room:", labelX, startY + (gapY * 5), mainContent);
        roomChoice = new Choice();
        roomChoice.setBounds(fieldX, startY + (gapY * 5), 250, 40);
        roomChoice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadAvailableRooms();
        mainContent.add(roomChoice);

        createFormLabel("Admission Time:", labelX, startY + (gapY * 6), mainContent);
        timeLabel = new JLabel();
        timeLabel.setBounds(fieldX, startY + (gapY * 6), 350, 30);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        timeLabel.setForeground(TEXT_DARK);
        startTimer();
        mainContent.add(timeLabel);

        createFormLabel("Advance Deposit:", labelX, startY + (gapY * 7), mainContent);
        textFieldDeposite = createStyledTextField(fieldX, startY + (gapY * 7), 250, mainContent);

        btnAdd = createModernButton("ADD PATIENT", 100, 530, BRAND);
        btnAdd.addActionListener(this);
        mainContent.add(btnAdd);

        btnBack = createModernButton("BACK", 280, 530, SIDEBAR);
        btnBack.addActionListener(this);
        mainContent.add(btnBack);

        add(mainContent, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createFormLabel(String text, int x, int y, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 30);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_DARK);
        panel.add(label);
    }

    private JTextField createStyledTextField(int x, int y, int w, JPanel panel) {
        JTextField field = new JTextField();
        field.setBounds(x, y, w, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        panel.add(field);
        return field;
    }

    private JButton createModernButton(String text, int x, int y, Color bg) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 160, 45);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void loadAvailableRooms() {
        try {
            connection c = new connection();
            ResultSet resultSet = c.statement.executeQuery("SELECT room_no FROM room WHERE availability = 'Available'");
            while (resultSet.next()) {
                roomChoice.add(resultSet.getString("room_no"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void startTimer() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            timeLabel.setText(sdf.format(new Date()));
        });
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            String idType = (String) comboBox.getSelectedItem();
            String idNum = textFieldNumber.getText();
            String name = textName.getText();
            String gender = r1.isSelected() ? "Male" : "Female";
            String disease = textFieldDisease.getText();
            String room = roomChoice.getSelectedItem();
            String time = timeLabel.getText();
            String deposit = textFieldDeposite.getText();

            if(idNum.isEmpty() || name.isEmpty() || deposit.isEmpty() || room == null) {
                JOptionPane.showMessageDialog(null, "Please fill all required fields and assign a room");
                return;
            }

            try {
                connection c = new connection();
                String q = "INSERT INTO patient_info (ID_Type, number, Name, Gender, Patient_Disease, Room_Number, Time, Deposite, Admission_Date) " +
                        "VALUES('" + idType + "', '" + idNum + "', '" + name + "', '" + gender + "', '" + disease + "', '" + room + "', '" + time + "', '" + deposit + "', CURDATE())";

                String q1 = "UPDATE room SET availability = 'Occupied' WHERE room_no = '" + room + "'";

                c.statement.executeUpdate(q);
                c.statement.executeUpdate(q1);

                JOptionPane.showMessageDialog(null, "Registration Successful");
                setVisible(false);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        } else if (e.getSource() == btnBack) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new NEW_PATIENT();
    }
}