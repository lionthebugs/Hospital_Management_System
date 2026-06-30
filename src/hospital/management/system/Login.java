package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.ResultSet;

public class Login extends JFrame {

    private final Color CLR_MAIN    = new Color(10, 15, 30);
    private final Color CLR_ACCENT  = new Color(0, 210, 255);
    private final Color CLR_CARD    = new Color(20, 25, 45);
    private final Color CLR_MUTED   = new Color(120, 135, 160);

    private JTextField userField;
    private JPasswordField passField;
    private boolean isPassVisible = false;

    public Login() {
        setUndecorated(true);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, CLR_MAIN, getWidth(), getHeight(), new Color(20, 30, 60));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
                g2.setColor(new Color(0, 210, 255, 20));
                g2.fillOval(getWidth()-300, -100, 400, 400);
                g2.fillOval(-150, getHeight()-250, 350, 350);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        JPanel sidebar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Path2D path = new Path2D.Double();
                path.moveTo(0, 0);
                path.lineTo(getWidth() - 80, 0);
                path.curveTo(getWidth() - 30, 150, getWidth() + 50, 450, getWidth() - 100, getHeight());
                path.lineTo(0, getHeight());
                path.closePath();
                g2.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 100, 200), getWidth(), getHeight(), new Color(0, 210, 255));
                g2.setPaint(gp);
                g2.fill(path);
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(450, 650));
        sidebar.setOpaque(false);

        JPanel brandArea = new JPanel();
        brandArea.setLayout(new BoxLayout(brandArea, BoxLayout.Y_AXIS));
        brandArea.setOpaque(false);

        JLabel logoIcon = new JLabel("⚕", SwingConstants.CENTER);
        logoIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 100));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandName1 = new JLabel("HOSPITAL");
        brandName1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 42));
        brandName1.setForeground(Color.WHITE);
        brandName1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandName2 = new JLabel("MANAGEMENT SYSTEM");
        brandName2.setFont(new Font("Segoe UI Semilight", Font.BOLD, 28));
        brandName2.setForeground(Color.WHITE);
        brandName2.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandArea.add(logoIcon);
        brandArea.add(brandName1);
        brandArea.add(brandName2);
        sidebar.add(brandArea);
        root.add(sidebar, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setOpaque(false);

        JButton closeBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_MUTED);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int p = 15;
                g2.drawLine(p, p, getWidth() - p, getHeight() - p);
                g2.drawLine(getWidth() - p, p, p, getHeight() - p);
                g2.dispose();
            }
        };
        closeBtn.setBounds(480, 20, 45, 45);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> System.exit(0));
        mainPanel.add(closeBtn);

        JLabel welcome = new JLabel("Security Gateway");
        welcome.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 36));
        welcome.setForeground(Color.WHITE);
        welcome.setBounds(60, 90, 400, 50);
        mainPanel.add(welcome);

        JLabel userTag = new JLabel("USER-ID");
        userTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userTag.setForeground(CLR_ACCENT);
        userTag.setBounds(60, 200, 200, 20);
        mainPanel.add(userTag);

        userField = new JTextField();
        styleInput(userField, 60, 225);
        mainPanel.add(userField);

        JLabel passTag = new JLabel("USER-PASSWORD");
        passTag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passTag.setForeground(CLR_ACCENT);
        passTag.setBounds(60, 300, 200, 20);
        mainPanel.add(passTag);

        passField = new JPasswordField();
        styleInput(passField, 60, 325);
        mainPanel.add(passField);

        JLabel eye = new JLabel("👁");
        eye.setBounds(420, 325, 30, 45);
        eye.setForeground(CLR_MUTED);
        eye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eye.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPassVisible = !isPassVisible;
                passField.setEchoChar(isPassVisible ? (char)0 : '•');
                eye.setForeground(isPassVisible ? CLR_ACCENT : CLR_MUTED);
            }
        });
        mainPanel.add(eye);

        JButton loginBtn = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CLR_ACCENT);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginBtn.setBounds(60, 430, 400, 55);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setForeground(CLR_MAIN);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> handleLogin());
        mainPanel.add(loginBtn);

        KeyListener nav = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN && e.getSource() == userField) passField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_UP && e.getSource() == passField) userField.requestFocus();
            }
        };
        userField.addKeyListener(nav);
        passField.addKeyListener(nav);

        root.add(mainPanel, BorderLayout.CENTER);

        MouseAdapter drag = new MouseAdapter() {
            int pX, pY;
            public void mousePressed(MouseEvent e) { pX = e.getX(); pY = e.getY(); }
            public void mouseDragged(MouseEvent e) { setLocation(getLocation().x + e.getX() - pX, getLocation().y + e.getY() - pY); }
        };
        root.addMouseListener(drag);
        root.addMouseMotionListener(drag);

        setVisible(true);
    }

    private void styleInput(JTextField f, int x, int y) {
        f.setBounds(x, y, 400, 45);
        f.setBackground(CLR_CARD);
        f.setForeground(Color.WHITE);
        f.setCaretColor(CLR_ACCENT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { f.setBorder(BorderFactory.createLineBorder(CLR_ACCENT, 1)); }
            @Override public void focusLost(FocusEvent e) { f.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1)); }
        });
    }

    private void handleLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Authentication failed: Empty fields.");
            return;
        }
        try {
            connection c = new connection();
            ResultSet rs = c.statement.executeQuery("SELECT * FROM login WHERE ID = '" + user + "' AND Pw = '" + pass + "'");
            if (rs.next()) {
                new reception();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Invalid Access Key.", "Security Alert", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        new Login();
    }
}