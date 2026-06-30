package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class update_patient_details extends JFrame {

    update_patient_details(){
        setTitle("RCMC - Update Billing & Room Shift");
        JPanel panel = new JPanel();
        panel.setBounds(5,5,940,490);
        panel.setBackground(new Color(0, 109, 119));
        panel.setLayout(null);
        add(panel);

        setUndecorated(true);

        JLabel label1 = new JLabel("Billing & Room Management");
        label1.setBounds(124,25,300,30);
        label1.setFont(new Font("Segoe UI",Font.BOLD,22));
        label1.setForeground(Color.white);
        panel.add(label1);

        JLabel label2 = new JLabel("Select Patient:");
        label2.setBounds(25,88,120,20);
        label2.setFont(new Font("Segoe UI",Font.PLAIN,16));
        label2.setForeground(Color.white);
        panel.add(label2);

        Choice choice = new Choice();
        choice.setBounds(248,85,200,25);
        panel.add(choice);

        try {
            connection c = new connection();
            ResultSet resultSet = c.statement.executeQuery("select * from Patient_Info");
            while (resultSet.next()) {
                choice.add(resultSet.getString("Name"));
            }
        } catch (Exception e) { e.printStackTrace(); }

        JLabel label3 = new JLabel("Current Room:");
        label3.setBounds(25,129,120,20);
        label3.setFont(new Font("Segoe UI",Font.PLAIN,16));
        label3.setForeground(Color.white);
        panel.add(label3);

        JTextField textFieldR = new JTextField();
        textFieldR.setBounds(248,129,200,25);
        panel.add(textFieldR);

        JLabel label4 = new JLabel("Check-In-Time:");
        label4.setBounds(25,174,120,20);
        label4.setFont(new Font("Segoe UI",Font.PLAIN,16));
        label4.setForeground(Color.white);
        panel.add(label4);

        JTextField textFieldINTime = new JTextField();
        textFieldINTime.setBounds(248,174,200,25);
        textFieldINTime.setEditable(false);
        panel.add(textFieldINTime);

        JLabel label5 = new JLabel("Paid Amount (Tk):");
        label5.setBounds(25,216,200,20);
        label5.setFont(new Font("Segoe UI",Font.PLAIN,16));
        label5.setForeground(Color.white);
        panel.add(label5);

        JTextField textFieldAmount = new JTextField();
        textFieldAmount.setBounds(248,216,200,25);
        panel.add(textFieldAmount);

        JLabel label6 = new JLabel("Pending Amount (Tk):");
        label6.setBounds(25,261,200,20);
        label6.setFont(new Font("Segoe UI",Font.BOLD,16));
        label6.setForeground(new Color(255, 214, 102));
        panel.add(label6);

        JTextField textFieldPending = new JTextField();
        textFieldPending.setBounds(248,261,200,25);
        textFieldPending.setEditable(false);
        panel.add(textFieldPending);


        JButton checkIn = new JButton("Calculate");
        checkIn.setBounds(100,378,110,35);
        checkIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(checkIn);

        checkIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = choice.getSelectedItem();
                try {
                    connection c = new connection();
                    ResultSet rs1 = c.statement.executeQuery("select * from Patient_Info where Name = '" + name + "'");
                    if (rs1.next()) {
                        textFieldR.setText(rs1.getString("Room_Number"));
                        textFieldINTime.setText(rs1.getString("Time"));
                        textFieldAmount.setText(rs1.getString("Deposite"));
                    }


                    ResultSet rs2 = c.statement.executeQuery("select price from room where room_no = '" + textFieldR.getText() + "'");
                    if (rs2.next()) {
                        double price = rs2.getDouble("price");
                        double paid = Double.parseDouble(textFieldAmount.getText());
                        double pending = price - paid;
                        textFieldPending.setText(String.valueOf(pending));
                    } else {
                        JOptionPane.showMessageDialog(null, "Room not found in inventory!");
                    }
                } catch (Exception e1) { e1.printStackTrace(); }
            }
        });

        JButton update = new JButton("Update Shift");
        update.setBounds(230, 378, 120, 35);
        update.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(update);

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection c = new connection();
                    String name = choice.getSelectedItem();
                    String newRoom = textFieldR.getText();
                    String newDeposit = textFieldAmount.getText();

                    String q = "update Patient_Info set Room_Number = '"+newRoom+"', Deposite = '"+newDeposit+"' where Name = '"+name+"'";
                    String q2 = "update room set availability = 'Occupied' where room_no = '"+newRoom+"'";

                    c.statement.executeUpdate(q);
                    c.statement.executeUpdate(q2);

                    JOptionPane.showMessageDialog(null, "Billing and Room Shift Updated Successfully");
                    setVisible(false);
                } catch (Exception E) { E.printStackTrace(); }
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(370,378,100,35);
        panel.add(back);
        back.addActionListener(e -> setVisible(false));

        setSize(900,500);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args){
        new update_patient_details();
    }
}