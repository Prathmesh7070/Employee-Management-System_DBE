import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.*;

public class StudentManager extends JFrame {

    private Connection conn;
    private JTextField rollField, nameField, courseField, deptField, searchField;
    private JTable table;
    private DefaultTableModel model;

    private JTextField userField;
    private JPasswordField passField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManager::new);
    }

    public StudentManager() {
        showLoginUI();
    }

    // ------------------------------- LOGIN UI -------------------------------
    private void showLoginUI() {
        JFrame login = new JFrame("MySQL Connection");
        login.setSize(420, 270);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setLocationRelativeTo(null);
        login.getContentPane().setBackground(new Color(245, 249, 255));
        login.setLayout(new BorderLayout());

        JLabel heading = new JLabel("Student Manager Login", SwingConstants.CENTER);
        heading.setFont(new Font("Poppins", Font.BOLD, 22));
        heading.setForeground(new Color(30, 60, 120));
        login.add(heading, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userField = new JTextField(15);
        passField = new JPasswordField(15);

        JButton connectBtn = new JButton("Connect →");
        styleButton(connectBtn, new Color(0, 123, 255));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);

        gbc.gridx = 1; gbc.gridy = 2; panel.add(connectBtn, gbc);

        login.add(panel, BorderLayout.CENTER);

        connectBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            connectToDB(login, user, pass);
        });

        login.setVisible(true);
    }

    // ------------------------------- CONNECT DB -------------------------------
    private void connectToDB(JFrame login, String user, String pass) {
        try {
            String url = "jdbc:mysql://localhost:3306/studentdb";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);

            JOptionPane.showMessageDialog(login, "Connected Successfully!");

            login.dispose();
            showMainUI();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(login, "Connection Failed:\n" + ex.getMessage());
        }
    }

    // ------------------------------- MAIN UI -------------------------------
    private void showMainUI() {
        setTitle("Student Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel bgPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(215, 234, 255),
                        0, getHeight(), new Color(180, 200, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bgPanel.setLayout(new BorderLayout(10, 10));
        bgPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Student Management Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setForeground(new Color(25, 55, 125));
        bgPanel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Roll No", "Name", "Course", "Department"}, 0);
        table = new JTable(model);
        table.setSelectionBackground(new Color(102, 255, 102));
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(80, 120, 200));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(new Color(140, 160, 220), 2, true));

        // --------------------------- FORM ---------------------------
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rollField = new JTextField(10);
        nameField = new JTextField(15);
        courseField = new JTextField(15);
        deptField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Roll No:"), gbc);
        gbc.gridx = 1; form.add(rollField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1; form.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; form.add(deptField, gbc);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("✏ Update");
        JButton deleteBtn = new JButton("🗑 Delete");
        JButton clearBtn = new JButton("Clear");
        JButton refreshBtn = new JButton("Refresh");

        styleButton(addBtn, new Color(0, 153, 76));
        styleButton(updateBtn, new Color(255, 153, 51));
        styleButton(deleteBtn, new Color(230, 57, 70));
        styleButton(clearBtn, new Color(102, 102, 255));
        styleButton(refreshBtn, new Color(70, 130, 180));

        gbc.gridx = 0; gbc.gridy = 4; form.add(addBtn, gbc);
        gbc.gridx = 1; form.add(updateBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 5; form.add(deleteBtn, gbc);
        gbc.gridx = 1; form.add(clearBtn, gbc);

        gbc.gridx = 1; gbc.gridy = 6; form.add(refreshBtn, gbc);

        // --------------------------- SEARCH ---------------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn, new Color(51, 102, 255));

        searchPanel.add(new JLabel("Roll No: "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        bgPanel.add(searchPanel, BorderLayout.SOUTH);
        bgPanel.add(form, BorderLayout.WEST);
        bgPanel.add(sp, BorderLayout.CENTER);

        add(bgPanel);
        setVisible(true);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        refreshBtn.addActionListener(e -> loadStudents());
        searchBtn.addActionListener(e -> searchStudent());

        loadStudents();
    }

    // ------------------------------- CRUD FUNCTIONS -------------------------------
    private void addStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();
            String dept = deptField.getText().trim();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO student VALUES (?,?,?,?)");
            ps.setInt(1, roll);
            ps.setString(2, name);
            ps.setString(3, course);
            ps.setString(4, dept);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added!");

            loadStudents();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Error: " + e.getMessage());
        }
    }

    private void updateStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());
            String name = nameField.getText().trim();
            String course = courseField.getText().trim();
            String dept = deptField.getText().trim();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE student SET name=?, course=?, department=? WHERE roll=?"
            );
            ps.setString(1, name);
            ps.setString(2, course);
            ps.setString(3, dept);
            ps.setInt(4, roll);

            int rows = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Updated Successfully!" : "⚠ No Record Found!");

            loadStudents();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Update Error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText());

            PreparedStatement ps = conn.prepareStatement("DELETE FROM student WHERE roll=?");
            ps.setInt(1, roll);

            int rows = ps.executeUpdate();
            JOptionPane.showMessageDialog(this, rows > 0 ? "Deleted Successfully!" : "⚠ No Record Found!");

            loadStudents();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Delete Error: " + e.getMessage());
        }
    }

    private void searchStudent() {
        try {
            int roll = Integer.parseInt(searchField.getText().trim());

            loadStudents(); // refresh table

            boolean found = false;
            int rowIndex = -1;

            for (int i = 0; i < model.getRowCount(); i++) {
                int currentRoll = (int) model.getValueAt(i, 0);

                if (currentRoll == roll) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));

                    rowIndex = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "⚠ No Record Found!");
            } else {
                rollField.setText(String.valueOf(model.getValueAt(rowIndex, 0)));
                nameField.setText(String.valueOf(model.getValueAt(rowIndex, 1)));
                courseField.setText(String.valueOf(model.getValueAt(rowIndex, 2)));
                deptField.setText(String.valueOf(model.getValueAt(rowIndex, 3)));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Search Error: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            model.setRowCount(0);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Load Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        rollField.setText("");
        nameField.setText("");
        courseField.setText("");
        deptField.setText("");
        searchField.setText("");
    }

    // ------------------------------- STYLING -------------------------------
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new RoundedBorder(10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
