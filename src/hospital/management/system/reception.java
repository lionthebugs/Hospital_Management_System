package hospital.management.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class reception extends JFrame {

    static final Color BG_PAGE    = new Color(240, 244, 248);
    static final Color BG_WHITE   = Color.WHITE;
    static final Color BG_SIDEBAR = new Color(248, 250, 252);
    static final Color BRAND      = new Color(0, 109, 119);
    static final Color TEXT_DARK  = new Color(26, 31, 54);
    static final Color TEXT_MUTED = new Color(132, 146, 166);
    static final Color BORDER_CLR = new Color(238, 240, 244);

    private JLabel timeLabel;

    reception() {
        setTitle("Hospital Management System — Reception");
        setSize(1350, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_PAGE);

        JPanel topNav = new JPanel(new BorderLayout());
        topNav.setBackground(BG_WHITE);
        topNav.setPreferredSize(new Dimension(0, 65));
        topNav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 13));
        brand.setOpaque(false);
        JLabel logoBox = createLogoIcon();
        brand.add(logoBox);

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);
        JLabel mainTitle = new JLabel("Hospital Management System");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleBlock.add(mainTitle);
        brand.add(titleBlock);
        topNav.add(brand, BorderLayout.WEST);

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 17));
        navRight.setOpaque(false);
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        timeLabel.setForeground(TEXT_DARK);
        startLiveClock();
        navRight.add(timeLabel);
        topNav.add(navRight, BorderLayout.EAST);
        add(topNav, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_CLR));
        setupSidebar(sidebar);
        add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));
        setupStatsRow(content);
        content.add(Box.createVerticalStrut(25));

        JPanel tilesGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        tilesGrid.setOpaque(false);

        tilesGrid.add(tile("Add New Patient", "➕", null, new Color(0, 109, 119), () -> new NEW_PATIENT()));
        tilesGrid.add(tile("Patient Records", "👤", null, new Color(45, 106, 79), () -> new ALL_Patient_Info()));
        tilesGrid.add(tile("Patient Discharge", "🚪", null, new Color(224, 92, 92), () -> new patient_discharge()));
        tilesGrid.add(tile("Update Records", "🔄", null, new Color(123, 45, 139), () -> new update_patient_details()));
        tilesGrid.add(tile("Room Management", "🏥", null, new Color(37, 99, 235), () -> new Room()));
        tilesGrid.add(tile("Department", "🏢", null, new Color(217, 119, 6), () -> new Department()));
        tilesGrid.add(tile("Doctor Panel", "🩺", null, new Color(15, 118, 110), () -> new Doctor_Info()));
        tilesGrid.add(tile("Ambulance", "🚑", "Dial 999", new Color(109, 40, 217), () -> new Ambulance_Service()));

        content.add(tilesGrid);
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel tile(String title, String icon, String extra, Color bg, Runnable action) {
        JPanel p = new JPanel() {
            boolean hover = false;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };

        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p.setOpaque(false);
        p.add(Box.createVerticalStrut(15));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        iconLbl.setForeground(Color.WHITE);
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(iconLbl);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBorder(new EmptyBorder(10, 0, 0, 0));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(titleLbl);

        if (extra != null) {
            JLabel extraLbl = new JLabel(extra);
            extraLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            extraLbl.setForeground(new Color(255, 255, 255, 220));
            extraLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(extraLbl);
        }

        JLabel footer = new JLabel("Click to open");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setForeground(new Color(255, 255, 255, 160));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(footer);

        p.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { setHover(p, true); }
            public void mouseExited(MouseEvent e) { setHover(p, false); }
            public void mouseClicked(MouseEvent e) { action.run(); }
        });

        return p;
    }

    private void setHover(JPanel p, boolean h) {
        try {
            java.lang.reflect.Field f = p.getClass().getDeclaredField("hover");
            f.setAccessible(true);
            f.set(p, h);
            p.repaint();
        } catch (Exception ignored) {}
    }

    private JLabel createLogoIcon() {
        JLabel l = new JLabel("", SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BRAND);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Color.WHITE);
                int w = getWidth(), h = getHeight();
                int thickness = 6, length = 20;
                g2.fillRect((w - length) / 2, (h - thickness) / 2, length, thickness);
                g2.fillRect((w - thickness) / 2, (h - length) / 2, thickness, length);
                g2.dispose();
            }
        };
        l.setPreferredSize(new Dimension(40, 40));
        return l;
    }

    private void startLiveClock() {
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM dd yyyy — hh:mm:ss a")));
        });
        timer.start();
    }

    private void setupSidebar(JPanel s) {
        s.add(Box.createVerticalStrut(20));
        s.add(sideLabel("MANAGEMENT"));
        s.add(sideItem("Add Patient", () -> new NEW_PATIENT()));
        s.add(sideItem("Records", () -> new ALL_Patient_Info()));
        s.add(sideItem("Update Billing", () -> new update_patient_details()));
        s.add(Box.createVerticalStrut(10));
        s.add(sideLabel("SERVICES"));
        s.add(sideItem("Ambulance", () -> new Ambulance_Service()));
        s.add(sideItem("Rooms", () -> new Room()));
        s.add(sideItem("Logout", () -> System.exit(0)));
    }

    private JLabel sideLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        l.setBorder(new EmptyBorder(10, 25, 5, 0));
        return l;
    }

    private JButton sideItem(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(220, 40));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setForeground(TEXT_DARK);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(0, 25, 0, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());
        return b;
    }

    private void setupStatsRow(JPanel content) {
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("0", "Patients Today", BRAND));
        stats.add(statCard("5", "Available Rooms", new Color(37, 99, 235)));
        stats.add(statCard("9", "Total Records", new Color(217, 119, 6)));
        stats.add(statCard("8", "Fleet Status", new Color(224, 92, 92)));
        content.add(stats);
    }

    private JPanel statCard(String val, String title, Color accent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_CLR, 1), new EmptyBorder(15, 20, 15, 20)));
        JLabel vLbl = new JLabel(val);
        vLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel tLbl = new JLabel(title);
        tLbl.setForeground(TEXT_MUTED);
        p.add(vLbl, BorderLayout.CENTER);
        p.add(tLbl, BorderLayout.SOUTH);
        return p;
    }

    public static void main(String[] args) {
        new reception();
    }
}