package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;

public class Department extends JFrame {


    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color ACCENT_COLOR = new Color(52, 152, 219);
    private final Color TEXT_COLOR = new Color(44, 62, 80);

    Department() {

        setUndecorated(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout());


        JPanel header = new JPanel();
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(800, 60) );
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));

        JLabel title = new JLabel("HOSPITAL DEPARTMENTS");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));


        JTable table = new JTable();
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);


        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(TEXT_COLOR);
        tableHeader.setPreferredSize(new Dimension(100, 40));


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        footer.setBackground(BG_COLOR);

        JButton b1 = new JButton("BACK");
        b1.setPreferredSize(new Dimension(120, 40));
        b1.setBackground(PRIMARY_COLOR);
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.setBorderPainted(false);
        b1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b1.addActionListener(e -> setVisible(false));
        footer.add(b1);
        add(footer, BorderLayout.SOUTH);


        try {
            connection c = new connection();
            String q = "select dep_name as 'Department Name', phone_no as 'Contact Number' from department";
            ResultSet resultSet = c.statement.executeQuery(q);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));


            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Department();
    }
}