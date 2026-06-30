package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class ALL_Patient_Info extends JFrame {


    static final Color BRAND_TEAL  = new Color(0, 109, 119);
    static final Color BG_LIGHT    = new Color(240, 244, 248);
    static final Color TEXT_DARK   = new Color(26, 31, 54);
    static final Color TEXT_MUTED  = new Color(132, 146, 166);

    private JTable table;
    private JLabel countLabel;

    public ALL_Patient_Info() {
        setUndecorated(true);
        setSize(1100, 700);
        setLocationRelativeTo(null);


        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_LIGHT);
        root.setBorder(BorderFactory.createLineBorder(new Color(218, 225, 233), 1));
        setContentPane(root);


        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 233, 237)));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(18, 25, 15, 0));

        JLabel mainTitle = new JLabel("Patient Master Registry");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainTitle.setForeground(TEXT_DARK);

        JLabel subTitle = new JLabel("Comprehensive list of all admitted patients");
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


        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(25, 25, 25, 25));


        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JTextField searchField = new JTextField(" Search patient name or ID...");
        searchField.setPreferredSize(new Dimension(350, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setForeground(TEXT_MUTED);
        searchField.setBorder(new LineBorder(new Color(210, 214, 219), 1, true));


        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if(searchField.getText().equals(" Search patient name or ID...")) {
                    searchField.setText("");
                    searchField.setForeground(TEXT_DARK);
                }
            }
        });

        countLabel = new JLabel("Records: 0");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        countLabel.setForeground(BRAND_TEAL);

        toolbar.add(searchField, BorderLayout.WEST);
        toolbar.add(countLabel, BorderLayout.EAST);
        centerPanel.add(toolbar, BorderLayout.NORTH);


        table = new JTable();
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(235, 245, 245));
        table.setSelectionForeground(BRAND_TEAL);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(0, 45));
        tableHeader.setBackground(new Color(248, 250, 252));
        tableHeader.setForeground(TEXT_DARK);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 233, 237)));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 233, 237), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        root.add(centerPanel, BorderLayout.CENTER);


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 233, 237)));

        JButton backBtn = new JButton("BACK");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(BRAND_TEAL);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        footer.add(backBtn);
        root.add(footer, BorderLayout.SOUTH);


        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String val = searchField.getText();
                TableRowSorter<TableModel> tr = new TableRowSorter<>(table.getModel());
                table.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter("(?i)" + val));
                countLabel.setText("Records: " + table.getRowCount());
            }
        });

        loadData();
        setVisible(true);
    }

    private void loadData() {
        try {
            connection c = new connection();
            ResultSet rs = c.statement.executeQuery("SELECT * FROM Patient_Info");
            table.setModel(DbUtils.resultSetToTableModel(rs));


            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            countLabel.setText("Records: " + table.getRowCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ALL_Patient_Info();
    }
}