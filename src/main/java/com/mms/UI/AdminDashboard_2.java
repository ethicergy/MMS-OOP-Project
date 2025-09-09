package com.mms.UI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminDashboard_2 extends JFrame {

    public AdminDashboard_2() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        getContentPane().setBackground(new Color(235, 224, 213));
        setLayout(new BorderLayout(10, 20));
        setIconImage(new ImageIcon("title-logo.png").getImage());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(235, 224, 213));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("ADMIN DASHBOARD", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        JLabel movieListLabel = new JLabel("MOVIE LIST");
        movieListLabel.setFont(new Font("Arial", Font.BOLD, 24));
        movieListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(movieListLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        String[] columns = {"Title", "Duration", "Language", "Actions"};
        Object[][] data = {
            {"Inception", "2h 28m", "English", ""},
            {"Lokah: Chapter 1", "2h 29m", "Malayalam", ""},
            {"Hridayapoorvam", "2h 31m", "Malayalam", ""},
            {"F1: The Movie", "2h 35m", "English", ""},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        };

        JTable table = new JTable(data, columns) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(198, 172, 143));
                        c.setForeground(Color.white);
                    } else {
                        c.setBackground(new Color(234, 224, 213));
                        c.setForeground(Color.black);
                    }
                } else {
                    c.setBackground(new Color(201, 173, 167));
                }
                return c;
            }
        };

        table.setRowHeight(55);
        table.setFont(new Font("Arial", Font.BOLD, 22));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(188, 155, 103));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setPreferredSize(new Dimension(0,40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Custom action buttons
        table.getColumn("Actions").setCellRenderer(new ButtonsRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonsEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
        mainPanel.add(scrollPane);

        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(2, 2));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(235, 224, 213));

        JButton addMovieBtn = new JButton("Add Movie");
        JButton addShowtimeBtn = new JButton("Add Showtime");

        addMovieBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        addMovieBtn.setBackground(new Color(34, 51, 59));
        addMovieBtn.setForeground(Color.WHITE);

        addShowtimeBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        addShowtimeBtn.setBackground(new Color(34, 51, 59));
        addShowtimeBtn.setForeground(Color.WHITE);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addMovieBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        buttonPanel.add(addShowtimeBtn);
        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AdminDashboard_2();
    }

    // Renderer
    static class ButtonsRenderer extends JPanel implements TableCellRenderer {
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JPanel emptyPanel = new JPanel();

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            styleButton(edit, new Color(34, 51, 59));
            styleButton(delete, new Color(34, 51, 59));
            add(edit);
            add(delete);
            emptyPanel.setOpaque(true);
        }

        private void styleButton(JButton btn, Color bg) {
            btn.setFocusPainted(false);
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            boolean isNullRow = table.getValueAt(row, 0) == null;
            JPanel target = isNullRow ? emptyPanel : this;
            if (isSelected) {
                target.setBackground(new Color(201, 173, 167));
            } else {
                target.setBackground(row % 2 == 0 ? new Color(198, 172, 143) : new Color(234, 224, 213));
            }
            return target;
        }
    }

    // Editor
    static class ButtonsEditor extends DefaultCellEditor {
        protected JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        protected JButton edit = new JButton("Edit");
        protected JButton delete = new JButton("Delete");
        protected JPanel emptyPanel = new JPanel();

        public ButtonsEditor(JCheckBox checkBox) {
            super(checkBox);
            styleButton(edit, new Color(34, 51, 59));
            styleButton(delete, new Color(34, 51, 59));
            panel.add(edit);
            panel.add(delete);

            edit.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Edit clicked");
                fireEditingStopped();
            });
            delete.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Delete clicked");
                fireEditingStopped();
            });
            emptyPanel.setOpaque(true);
        }

        private void styleButton(JButton btn, Color bg) {
            btn.setFocusPainted(false);
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (table.getValueAt(row, 0) == null) {
                return emptyPanel;
            } else {
                panel.setBackground(isSelected ? new Color(201, 173, 167) 
                                               : (row % 2 == 0 ? new Color(198, 172, 143) : new Color(234, 224, 213)));
                return panel;
            }
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
