package com.mms.UI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminDashboard {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.getContentPane().setBackground(new Color(235, 224, 213));
        frame.setLayout(new BorderLayout(10, 20));            //  Use BorderLayout for main frame 


        // Main panel with BoxLayout (Y_AXIS)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(235, 224, 213));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // margins

        // Title
        JLabel titleLabel = new JLabel("ADMIN DASHBOARD", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // center align


        mainPanel.add(titleLabel);  //added title to main panel
        mainPanel.add(Box.createVerticalStrut(30));    //vertical spacing

        // Movie List Label
        JLabel movieListLabel = new JLabel("MOVIE LIST");
        movieListLabel.setFont(new Font("Arial", Font.BOLD, 24));
        movieListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);   //alignment


        mainPanel.add(movieListLabel);  // added movieListLabel to main panel
        mainPanel.add(Box.createVerticalStrut(25)); 

        // Table Data
        String[] columns = {"Title", "Duration", "Language", "Actions"};
        Object[][] data = {
            {"Inception", "2h 28m", "English", "✏️   🗑️"},
            {"Lokah: Chapter 1", "2h 29m", "Malayalam", "✏️   🗑️"},
            {"Hridayapoorvam", "2h 31m", "Malayalam", "✏️   🗑️"},
            {"F1: The Movie", "2h 35m", "English", "✏️   🗑️"},
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
                        c.setBackground(new Color(198, 172, 143 )); 
                        c.setForeground(Color.white);
                    } else {                                                 //alternate row colour adjustment
                        c.setBackground(new Color(234, 224, 213 ));
                        c.setForeground(Color.black);// 
                    }
                } else {
                    c.setBackground(new Color(201, 173, 167)); // ~ Pale Dogwood   (row hover colour)
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

        // Center text in table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        
     // Actions column renderer/editor
        table.getColumn("Actions").setCellRenderer(new ButtonsRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonsEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500)); // limit height of table
        mainPanel.add(scrollPane);
        
      //border for table
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(2, 2)); // increase cell border thickness

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // Buttons panel (side by side, centered)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(235, 224, 213));

        JButton addMovieBtn = new JButton("Add Movie");
        JButton addShowtimeBtn = new JButton("Add Showtime");

        addMovieBtn.setFont(new Font("Comfortaa", Font.PLAIN, 24));   // add movie button
        addMovieBtn.setBackground(new Color(34, 51, 59));
        addMovieBtn.setForeground(Color.WHITE);


        addShowtimeBtn.setFont(new Font("Comfortaa", Font.PLAIN, 24));    // add show time button
        addShowtimeBtn.setBackground(new Color(34 ,51, 59));
        addShowtimeBtn.setForeground(Color.WHITE);

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addMovieBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(500, 0))); // horizontal  spacing
        buttonPanel.add(addShowtimeBtn);
        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(Box.createVerticalStrut(25));   //spacing
        mainPanel.add(buttonPanel); // added button panel to main panel

        frame.add(mainPanel, BorderLayout.CENTER);  // added main panel to frame

        // Show frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

 // Renderer
    static class ButtonsRenderer extends JPanel implements TableCellRenderer {
        JButton edit = new JButton("✏️");
        JButton delete = new JButton("🗑️");
        JPanel emptyPanel = new JPanel();

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            add(edit);
            add(delete);
            edit.setFocusPainted(false);
            delete.setFocusPainted(false);
            edit.setBackground(new Color(180, 200, 250));
            delete.setBackground(new Color(250, 180, 180));
            emptyPanel.setOpaque(true);
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
        protected JButton edit = new JButton("✏️");
        protected JButton delete = new JButton("🗑️");
        protected JPanel emptyPanel = new JPanel();

        public ButtonsEditor(JCheckBox checkBox) {
            super(checkBox);

            edit.setFocusPainted(false);
            delete.setFocusPainted(false);
            edit.setBackground(new Color(180, 200, 250));
            delete.setBackground(new Color(250, 180, 180));

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

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (table.getValueAt(row, 0) == null) {
                return emptyPanel;
            } else {
                // Check the table's selection model directly
                if (isSelected) {
                    panel.setBackground(new Color(201, 173, 167)); // selected row color
                } else {
                    panel.setBackground(row % 2 == 0 ? new Color(198, 172, 143) : new Color(234, 224, 213));
                }
                return panel;
            }
        }


        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}