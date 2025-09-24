package com.mms.UI;

import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import com.mms.dao.MovieDAO;
import com.mms.models.Movie;
import java.awt.*;

public class AdminDashboard_2 extends JFrame {

    public AdminDashboard_2() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        getContentPane().setBackground(new Color(235, 224, 213));
        setLayout(new BorderLayout(10, 20));
        setIconImage(new ImageIcon("title-logo.png").getImage());

        //Logout Button

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

        JTable table = loadMovies();

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
        table.getColumn("Actions").setCellEditor(new ButtonsEditor(this, new JCheckBox()));

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

        addMovieBtn.setFont(new Font("Arial", Font.BOLD, 18));
        addMovieBtn.setBackground(new Color(34, 51, 59));
        addMovieBtn.setForeground(Color.WHITE);
        addMovieBtn.addActionListener(e -> {
            new AddMovieDialog(this, this::refreshMovieTable);
        });

        addShowtimeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        addShowtimeBtn.setBackground(new Color(34, 51, 59));
        addShowtimeBtn.setForeground(Color.WHITE);
        addShowtimeBtn.addActionListener(e -> {
            new AddShowtimeDialog(this);
            // Refresh the table after adding a showtime if needed
        });

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        logoutBtn.setBackground(new Color(34, 51, 59));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame_1();
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addMovieBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        buttonPanel.add(addShowtimeBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(200, 0)));
        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createHorizontalGlue());

        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
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
            btn.setFont(new Font("Arial", Font.BOLD, 14));
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

    private JTable movieTable;
    private DefaultTableModel tableModel;

    public JTable loadMovies(){
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> movies = movieDAO.getAllMovies();
        String[] columns = {"Title", "Duration", "Language", "Actions", "MovieId"};
        Object[][] data = new Object[movies.size()][5];
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            String title = movie.getTitle();
            String duration = movie.getDuration() + " mins";
            String language = movie.getLanguage();
            data[i] = new Object[]{title, duration, language, null, movie.getMovieId()};
        }
        tableModel = new DefaultTableModel(data, columns);
        movieTable = new JTable(tableModel) {
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
        movieTable.getColumnModel().getColumn(4).setMinWidth(0);
        movieTable.getColumnModel().getColumn(4).setMaxWidth(0);
        movieTable.getColumnModel().getColumn(4).setWidth(0);
        return movieTable;
        
    }
    
    public void refreshMovieTable() {
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> movies = movieDAO.getAllMovies();
        tableModel.setRowCount(0); // Clear existing rows
        for (Movie movie : movies) {
            String title = movie.getTitle();
            String duration = movie.getDuration() + " mins";
            String language = movie.getLanguage();
            tableModel.addRow(new Object[]{title, duration, language, null, movie.getMovieId()});
        }
    }
    // Editor
    class ButtonsEditor extends DefaultCellEditor {
        protected JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        protected JButton edit = new JButton("Edit");
        protected JButton delete = new JButton("Delete");
        protected JPanel emptyPanel = new JPanel();
        private JFrame parentFrame;
        public ButtonsEditor(JFrame parentFrame, JCheckBox checkBox) {
            super(checkBox);
            styleButton(edit, new Color(34, 51, 59));
            styleButton(delete, new Color(34, 51, 59));
            this.parentFrame = parentFrame;
            panel.add(edit);
            panel.add(delete);
            emptyPanel.setOpaque(true);
        }

        private void styleButton(JButton btn, Color bg) {
            btn.setFocusPainted(false);
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
        {
            // Clear existing listeners to prevent multiple registrations
            for (java.awt.event.ActionListener al : edit.getActionListeners()) {
                edit.removeActionListener(al);
            }
            for (java.awt.event.ActionListener al : delete.getActionListeners()) {
                delete.removeActionListener(al);
            }
            
            int movieid = (int) table.getModel().getValueAt(row, 4);
            edit.addActionListener(e -> {
                try {
                    // Stop editing immediately to prevent multiple triggers
                    fireEditingStopped();
                    
                    MovieDAO movieDAO = new MovieDAO();
                    Movie movie = movieDAO.getMoviebyId(movieid);
                    if (movie != null) {
                        // Create callback for when movie is updated
                        Runnable updateCallback = () -> {
                            Movie updatedMovie = movieDAO.getMoviebyId(movieid);
                            if (parentFrame instanceof AdminDashboard_2) {
                                tableModel.setValueAt(updatedMovie.getTitle(), row, 0);
                                tableModel.setValueAt(updatedMovie.getDuration() + " mins", row, 1);
                                tableModel.setValueAt(updatedMovie.getLanguage(), row, 2);
                                ((AdminDashboard_2) parentFrame).refreshMovieTable();
                            }
                        };
                        new AddMovieDialog(parentFrame, movie, updateCallback);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, "Movie not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Error opening edit dialog: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            delete.addActionListener(e -> {
                try {
                    // Stop editing immediately to prevent multiple triggers
                    fireEditingStopped();
                    
                    int confirm = JOptionPane.showConfirmDialog(parentFrame, 
                        "Are you sure you want to delete this movie?", 
                        "Confirm Delete", 
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        MovieDAO movieDAO = new MovieDAO();
                        boolean success = movieDAO.deleteMovie(movieid);
                        if (success) {
                            tableModel.removeRow(row);
                            JOptionPane.showMessageDialog(parentFrame, "Movie deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Cannot delete movie. It may have associated showtimes.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Error deleting movie: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
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
    public static void main(String[] args) {
        new AdminDashboard_2();
    }
}    