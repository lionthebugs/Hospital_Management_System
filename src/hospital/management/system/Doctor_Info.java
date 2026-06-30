package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class Doctor_Info extends JFrame {


    private final Color CLR_NAV_DARK  = new Color(15, 23, 42);   // Deep Navy
    private final Color CLR_CANVAS    = new Color(248, 250, 252); // Soft Gray
    private final Color CLR_ACTION    = new Color(79, 70, 229);   // Indigo
    private final Color CLR_BORDER    = new Color(226, 232, 240);
    private final Color CLR_TEXT_HD   = new Color(30, 41, 59);
    private final Color CLR_TEXT_BODY = new Color(71, 85, 105);

    private JTable table;
    private JTextField searchField;
    private JLabel countLabel;

    public Doctor_Info() {
        setUndecorated(true);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CLR_CANVAS);
        root.setBorder(BorderFactory.createLineBorder(CLR_BORDER, 1));
        setContentPane(root);


        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBackground(CLR_NAV_DARK);

        JPanel brandContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
        brandContainer.setOpaque(false);

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(CLR_ACTION);
                g2.fillRoundRect(0, 0, 45, 45, 12, 12);


                g2.setColor(Color.WHITE);
                g2.fillRect(20, 10, 5, 25);
                g2.fillRect(10, 20, 25, 5);
                g2.dispose();
            }
        };
        logoIcon.setPreferredSize(new Dimension(45, 45));
        logoIcon.setOpaque(false);

        JLabel logoText = new JLabel(" DR. PANEL");
        logoText.setForeground(Color.WHITE);
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 22));

        brandContainer.add(logoIcon);
        brandContainer.add(logoText);
        sidebar.add(brandContainer, BorderLayout.NORTH);


        JPanel navMenu = new JPanel(null);
        navMenu.setOpaque(false);
        sidebar.add(navMenu, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 30));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);


        JPanel titleGrp = new JPanel(new GridLayout(2, 1, 0, 5));
        titleGrp.setOpaque(false);
        JLabel head = new JLabel("Doctor Management");
        head.setFont(new Font("Segoe UI", Font.BOLD, 28));
        head.setForeground(CLR_TEXT_HD);

        countLabel = new JLabel("Total Registered Staff: 0");
        countLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        countLabel.setForeground(CLR_TEXT_BODY);
        titleGrp.add(head);
        titleGrp.add(countLabel);
        topBar.add(titleGrp, BorderLayout.WEST);

        searchField = new JTextField("Search by any detail...");
        searchField.setPreferredSize(new Dimension(320, 45));
        searchField.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(new Color(148, 163, 184));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CLR_BORDER, 2, true),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox.setOpaque(false);
        searchBox.add(searchField);
        topBar.add(searchBox, BorderLayout.EAST);

        main.add(topBar, BorderLayout.NORTH);

        table = new JTable();
        styleProfessionalTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(CLR_BORDER, 1));
        card.add(scroll);
        main.add(card, BorderLayout.CENTER);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionBar.setOpaque(false);

        JButton exitBtn = new JButton("EXIT VIEW");
        styleActionButton(exitBtn);
        exitBtn.addActionListener(e -> setVisible(false));

        actionBar.add(exitBtn);
        main.add(actionBar, BorderLayout.SOUTH);

        root.add(main, BorderLayout.CENTER);

        attachListeners();
        fetchDoctorData();
        setVisible(true);
    }

    private void styleProfessionalTable() {
        table.setRowHeight(55);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(CLR_ACTION);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 45));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(100, 116, 139));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    private void styleActionButton(JButton btn) {
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setBackground(CLR_ACTION);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(null);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(CLR_ACTION.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(CLR_ACTION); }
        });
    }

    private void attachListeners() {
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if(searchField.getText().contains("Search")) {
                    searchField.setText("");
                    searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    searchField.setForeground(CLR_TEXT_HD);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String val = searchField.getText();
                TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
                table.setRowSorter(trs);
                trs.setRowFilter(RowFilter.regexFilter("(?i)" + val));
                countLabel.setText("Total Registered Staff: " + table.getRowCount());
            }
        });
    }

    private void fetchDoctorData() {
        try {
            connection c = new connection();
            String query = "SELECT DoctorID, Name, Specialization, Email, Phone, Salary FROM Doctor_Info";
            ResultSet rs = c.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));

            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(JLabel.CENTER);
            for(int i=0; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

            countLabel.setText("Total Registered Staff: " + table.getRowCount());
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new Doctor_Info();
    }
}