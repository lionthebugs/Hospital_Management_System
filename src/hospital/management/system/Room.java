package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class Room extends JFrame {

    private JTable table;

    private final Color CLR_NAV     = new Color(30, 41, 59);  // Slate 800
    private final Color CLR_BG      = new Color(248, 250, 252); // Slate 50
    private final Color CLR_PRIMARY = new Color(14, 165, 233); // Sky Blue
    private final Color CLR_SUCCESS = new Color(34, 197, 94);  // Green 500
    private final Color CLR_DANGER  = new Color(239, 68, 68);   // Red 500

    public Room() {
        setUndecorated(true);
        setSize(1000, 700);
        setLocationRelativeTo(null);


        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CLR_BG);
        root.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        setContentPane(root);

        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(CLR_NAV);


        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(0, 0, 120, 120);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(35, 45, 50, 35, 5, 5); // Bed Frame
                g2.drawLine(45, 45, 45, 80);
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(120, 120));
        sidebar.add(iconCircle);
        root.add(sidebar, BorderLayout.WEST);


        JPanel main = new JPanel(new BorderLayout(0, 25));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(40, 50, 40, 50));


        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Room Inventory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(CLR_NAV);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Real-time Bed Allocation Status");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 116, 139));
        header.add(subtitle, BorderLayout.SOUTH);

        main.add(header, BorderLayout.NORTH);


        table = new JTable();
        styleRoomTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createLineBorder(new Color(237, 242, 247), 1));
        tableCard.add(scroll);
        main.add(tableCard, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        JButton backBtn = new JButton("DISMISS VIEW");
        styleButton(backBtn);
        backBtn.addActionListener(e -> setVisible(false));
        footer.add(backBtn);
        main.add(footer, BorderLayout.SOUTH);

        root.add(main, BorderLayout.CENTER);

        loadDatabaseData();
        setVisible(true);
    }

    private void styleRoomTable() {
        table.setRowHeight(55);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(241, 245, 249));
        table.setSelectionBackground(new Color(240, 249, 255));
        table.setSelectionForeground(CLR_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 50));
        header.setBackground(new Color(250, 251, 252));
        header.setForeground(new Color(100, 116, 139));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
    }

    private void styleButton(JButton btn) {
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setBackground(CLR_NAV);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(null);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(CLR_PRIMARY); }
            public void mouseExited(MouseEvent e) { btn.setBackground(CLR_NAV); }
        });
    }

    private void loadDatabaseData() {
        try {
            connection c = new connection();
            String query = "SELECT room_no AS 'Room No', availability AS 'Status', price AS 'Daily Rate', bed_type AS 'Bed Type' FROM room";
            ResultSet resultSet = c.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));

            table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(JLabel.CENTER);

                    String status = (value != null) ? value.toString() : "";
                    if (status.equalsIgnoreCase("Available")) {
                        label.setForeground(CLR_SUCCESS);
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    } else if (status.equalsIgnoreCase("Occupied")) {
                        label.setForeground(CLR_DANGER);
                    } else {
                        label.setForeground(new Color(71, 85, 105));
                    }
                    return label;
                }
            });


            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        new Room();
    }
}