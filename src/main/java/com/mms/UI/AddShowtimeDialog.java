package com.mms.UI;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import com.mms.models.Movie;
import com.mms.controllers.ShowtimeController;
import com.mms.util.InputValidator;
import com.mms.util.Logger;


public class AddShowtimeDialog extends JDialog {
    Color bgColor = new Color(198, 172, 143);
    private final ShowtimeController showtimeController = new ShowtimeController();
    public AddShowtimeDialog(JFrame parent) {
        super(parent, "Add New Showtime", true);
        setSize(650, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(235, 224, 213)));

        java.util.List<Movie> movies = showtimeController.getAllMovies();
        JComboBox<Movie> movieComboBox = new JComboBox<>();
        for (Movie movie : movies) {
            movieComboBox.addItem(movie);
        }
        // Set custom renderer to display movie titles
        movieComboBox.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Movie) {
                    setText(((Movie) value).getTitle());
                }
                return this;
            }
        });

        JLabel movieLabel = new JLabel("Select Movie:");
        JLabel startDateLabel = new JLabel("Start Date:");
        JLabel endDateLabel = new JLabel("End Date:");
        JLabel numShowsLabel = new JLabel("Number of Shows per Day:");
        JLabel screenLabel = new JLabel("Screen Number:");

        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner numShowsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JSpinner screenSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        List<JPanel> panels = new ArrayList<>();

        JPanel timeFieldsPanel = new JPanel();
        timeFieldsPanel.setBackground(new Color(234, 224, 213));
        timeFieldsPanel.setLayout(new BoxLayout(timeFieldsPanel, BoxLayout.Y_AXIS));

        // Dynamic time fields for shows
        java.util.List<JPanel> timePickers = new java.util.ArrayList<>();
        java.util.List<JTextField> hourFields = new java.util.ArrayList<>();
        java.util.List<JTextField> minuteFields = new java.util.ArrayList<>();
        java.util.List<JComboBox<String>> amPmFields = new java.util.ArrayList<>();

        // Helper to update time pickers
        Runnable updateTimePickers = () -> {
            timeFieldsPanel.removeAll();
            timePickers.clear(); hourFields.clear(); minuteFields.clear(); amPmFields.clear();
            int numShows = (Integer) numShowsSpinner.getValue();
            
            timeFieldsPanel.setLayout(new GridBagLayout());
            GridBagConstraints timeGbc = new GridBagConstraints();
            timeGbc.insets = new Insets(5, 5, 5, 5);
            timeGbc.anchor = GridBagConstraints.WEST;
            
            for (int i = 0; i < numShows; i++) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                row.setBackground(new Color(234, 224, 213));
                row.setBorder(BorderFactory.createEtchedBorder());
                
                JLabel showLabel = new JLabel("Show " + (i+1) + ":");
                showLabel.setFont(showLabel.getFont().deriveFont(Font.BOLD));
                showLabel.setPreferredSize(new Dimension(60, 25));
                
                JTextField hourField = new JTextField(3);
                hourField.setHorizontalAlignment(JTextField.CENTER);
                JTextField minuteField = new JTextField(3);
                minuteField.setHorizontalAlignment(JTextField.CENTER);
                JComboBox<String> amPmField = new JComboBox<>(new String[]{"AM", "PM"});
                
                row.add(showLabel);
                row.add(hourField);
                row.add(new JLabel(":"));
                row.add(minuteField);
                row.add(amPmField);
                
                timeGbc.gridx = 0; timeGbc.gridy = i;
                timeGbc.fill = GridBagConstraints.HORIZONTAL;
                timeGbc.weightx = 1.0;
                timeFieldsPanel.add(row, timeGbc);
                
                timePickers.add(row);
                hourFields.add(hourField);
                minuteFields.add(minuteField);
                amPmFields.add(amPmField);
            }
            timeFieldsPanel.revalidate();
            timeFieldsPanel.repaint();
        };

        numShowsSpinner.addChangeListener(e -> updateTimePickers.run());
        updateTimePickers.run(); // Initial

        JButton addButton = new JButton("Add Showtimes");
        JButton cancelButton = new JButton("Cancel");
        addButton.setBackground(new Color(34, 51, 59));
        addButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(34, 51, 59));
        cancelButton.setForeground(Color.WHITE);
        // Main panel with proper layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        panels.add(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel with GridBagLayout for better alignment
        JPanel formPanel = new JPanel(new GridBagLayout());
        panels.add(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 0: Movie selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(movieLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(movieComboBox, gbc);
        
        // Row 1: Start date
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(startDateLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(startDateSpinner, gbc);
        
        // Row 2: End date
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(endDateLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(endDateSpinner, gbc);
        
        // Row 3: Number of shows
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(numShowsLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(numShowsSpinner, gbc);
        
        // Row 4: Screen number
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(screenLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        formPanel.add(screenSpinner, gbc);
        
        // Row 5: Show times label
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel showTimesLabel = new JLabel("Show Times:");
        showTimesLabel.setFont(showTimesLabel.getFont().deriveFont(Font.BOLD, 14f));
        formPanel.add(showTimesLabel, gbc);
        
        // Row 6: Time fields panel
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        JScrollPane timeScrollPane = new JScrollPane(timeFieldsPanel);
        timeScrollPane.setPreferredSize(new Dimension(500, 200));
        timeScrollPane.setBorder(BorderFactory.createTitledBorder("Show Times"));
        formPanel.add(timeScrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panels.add(buttonPanel);
        for (JPanel p : panels) {
            p.setBackground(bgColor);
        }
        addButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        pack();

        addButton.addActionListener(e -> {
            Movie selectedMovie = (Movie) movieComboBox.getSelectedItem();
            java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
            java.util.Date endDate = (java.util.Date) endDateSpinner.getValue();
            int numShows = (Integer) numShowsSpinner.getValue();
            int screenNumber = (Integer) screenSpinner.getValue();

            // Gather time fields
            java.util.List<String> hours = new java.util.ArrayList<>();
            java.util.List<String> minutes = new java.util.ArrayList<>();
            java.util.List<String> amPm = new java.util.ArrayList<>();
            for (int i = 0; i < numShows; i++) {
                hours.add(hourFields.get(i).getText().trim());
                minutes.add(minuteFields.get(i).getText().trim());
                amPm.add((String) amPmFields.get(i).getSelectedItem());
            }

            java.time.LocalDate startLocalDate = new java.sql.Date(startDate.getTime()).toLocalDate();
            java.time.LocalDate endLocalDate = new java.sql.Date(endDate.getTime()).toLocalDate();

            // Use InputValidator for field checks
            if (selectedMovie == null || InputValidator.isNullOrEmpty(hours.get(0)) || InputValidator.isNullOrEmpty(minutes.get(0))) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                java.util.List<java.time.LocalTime> showTimes = showtimeController.parseShowTimes(numShows, hours, minutes, amPm);
                int count = showtimeController.createShowtimes(selectedMovie, startLocalDate, endLocalDate, screenNumber, showTimes);
                JOptionPane.showMessageDialog(this, count + " showtimes added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for hours and minutes.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                Logger.log(ex);
                JOptionPane.showMessageDialog(this, "Error adding showtimes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelButton.addActionListener(e -> dispose());
        setVisible(true);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new AddShowtimeDialog(frame);
    }
}
