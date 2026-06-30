package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class Ambulance_Service extends JFrame {

    private final Color CLR_SIDEBAR = new Color(15, 23, 42);
    private final Color CLR_BG      = new Color(248, 250, 252);
    private final Color CLR_ACCENT  = new Color(79, 70, 229);
    private final Color CLR_CARD    = Color.WHITE;
    private final Color CLR_BORDER  = new Color(226, 232, 240);
    private final Color CLR_SUCCESS = new Color(34, 197, 94);
    private final Color CLR_DANGER  = new Color(239, 68, 68);

    private JTable table;
    private JTextField searchField;
    private JLabel countLabel;

    public Ambulance_Service() {
        setUndecorated(true);
        setSize(1150, 800);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CLR_BG);
        root.setBorder(BorderFactory.createLineBorder(CLR_BORDER, 1));
        setContentPane(root);


        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(CLR_SIDEBAR);


        JPanel brandSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        brandSection.setOpaque(false);
        brandSection.add(createModernLogo());

        JLabel brandName = new JLabel("  Ambulance Portal");
        brandName.setForeground(Color.WHITE);
        brandName.setFont(new Font("Inter", Font.BOLD, 20));
        brandSection.add(brandName);

        sidebar.add(brandSection, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);


        JPanel mainArea = new JPanel(new BorderLayout(0, 25));
        mainArea.setOpaque(false);
        mainArea.setBorder(new EmptyBorder(40, 50, 40, 50));


        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGrp = new JPanel(new GridLayout(2, 1, 0, 5));
        titleGrp.setOpaque(false);
        JLabel title = new JLabel("Emergency Fleet Registry");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setForeground(CLR_SIDEBAR);

        countLabel = new JLabel("Total Vehicles Tracked: 0");
        countLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        countLabel.setForeground(new Color(100, 116, 139));

        titleGrp.add(title);
        titleGrp.add(countLabel);
        header.add(titleGrp, BorderLayout.WEST);


        searchField = new JTextField(" Search driver or model...");
        searchField.setPreferredSize(new Dimension(320, 45));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CLR_BORDER, 2, true),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));

        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.add(searchField);
        header.add(searchWrapper, BorderLayout.EAST);

        mainArea.add(header, BorderLayout.NORTH);


        table = new JTable();
        styleAmbulanceTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CLR_CARD);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(CLR_CARD);
        tableCard.setBorder(BorderFactory.createLineBorder(new Color(237, 242, 247), 1));
        tableCard.add(scroll);

        mainArea.add(tableCard, BorderLayout.CENTER);


        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        JButton backBtn = new JButton("CLOSE PORTAL");
        styleActionButton(backBtn);
        backBtn.addActionListener(e -> setVisible(false));
        footer.add(backBtn);

        mainArea.add(footer, BorderLayout.SOUTH);
        add(mainArea, BorderLayout.CENTER);


        setupListeners();
        fetchData();
        setVisible(true);
    }

    private void styleAmbulanceTable() {
        table.setRowHeight(60);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Inter", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(245, 243, 255));
        table.setSelectionForeground(CLR_ACCENT);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 55));
        header.setBackground(new Color(250, 251, 252));
        header.setForeground(new Color(100, 116, 139));
        header.setFont(new Font("Inter", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CLR_BORDER));
    }

    private void styleActionButton(JButton btn) {
        btn.setPreferredSize(new Dimension(180, 48));
        btn.setBackground(CLR_SIDEBAR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Inter", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(null);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(CLR_ACCENT); }
            public void mouseExited(MouseEvent e) { btn.setBackground(CLR_SIDEBAR); }
        });
    }

    private JPanel createModernLogo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_ACCENT);
                g2.fillRoundRect(0, 0, 40, 40, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(10, 20, 30, 20);
                g2.drawLine(20, 10, 20, 30);
                g2.dispose();
            }
            @Override public Dimension getPreferredSize() { return new Dimension(40, 40); }
        };
    }

    private void fetchData() {
        try {
            connection c = new connection();
            String query = "SELECT Driver_Name, Gender, Car_Model, Availability, Location FROM ambulance";
            ResultSet rs = c.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(rs));

            DefaultTableCellRenderer proRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setBorder(new EmptyBorder(0, 10, 0, 10));

                    if (column == 3 && value != null) {
                        String status = value.toString();
                        if (status.equalsIgnoreCase("available")) {
                            label.setForeground(CLR_SUCCESS);
                            label.setText("● AVAILABLE");
                        } else {
                            label.setForeground(CLR_DANGER);
                            label.setText("○ BUSY");
                        }
                        label.setFont(new Font("Inter", Font.BOLD, 11));
                    } else {
                        label.setForeground(new Color(51, 65, 85));
                    }
                    return label;
                }
            };

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(proRenderer);
            }
            countLabel.setText("Total Vehicles Tracked: " + table.getRowCount());

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void setupListeners() {
        searchField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if(searchField.getText().contains("Search")) searchField.setText(""); }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String query = searchField.getText();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        new Ambulance_Service();
    }
}