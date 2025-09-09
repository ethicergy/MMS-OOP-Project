package com.mms.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * BookingSummaryFrame — padded + polished text version
 */
public class BookingSummaryFrame extends JFrame {

    // theme colors
    private static final Color BG = new Color(234, 224, 213);
    private static final Color RECT = new Color(198, 172, 143);
    private static final Color BTN = new Color(34, 51, 59);
    private static final Color BORDER = Color.BLACK;

    public BookingSummaryFrame() {
        setTitle("Booking Summary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // Title
        JLabel title = new JLabel("BOOKING SUMMARY", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setBorder(BorderFactory.createEmptyBorder(36, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Center container
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        // Inner rectangle box
        JPanel rectPanel = new JPanel(new GridBagLayout());
        rectPanel.setBackground(RECT);
        rectPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 2),
                new EmptyBorder(30, 50, 30, 50)   // padding inside the box
        ));
        rectPanel.setPreferredSize(new Dimension(950, 420));

        // Fonts
        Font labelFont = new Font("SansSerif", Font.BOLD, 28);
        Font valueFont = new Font("SansSerif", Font.PLAIN, 28);
        Font totalValueFont = new Font("SansSerif", Font.BOLD, 32);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(20, 0, 20, 0);

        // rows
        rectPanel.add(createRow("Movie:", "Inception", labelFont, valueFont), g);

        g.gridy++;
        rectPanel.add(createRow("Showtime:", "1:00pm  |  Screen 1  |  25/7/25", labelFont, valueFont), g);

        g.gridy++;
        rectPanel.add(createRow("Seats:", "A4, A5, A6", labelFont, valueFont), g);

        g.gridy++;
        rectPanel.add(createRow("Total Price:", "₹120 × 3  =  ₹360", labelFont, totalValueFont), g);

        // add rectPanel centered
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        centerContainer.add(rectPanel, c);

        add(centerContainer, BorderLayout.CENTER);

        // Bottom button
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBackground(BG);
        bottom.setBorder(BorderFactory.createEmptyBorder(16, 0, 36, 0));
        JButton confirm = new JButton("Confirm & Pay ₹360");
        confirm.setBackground(BTN);
        confirm.setForeground(Color.WHITE);
        confirm.setFont(new Font("SansSerif", Font.BOLD, 21));
        confirm.setFocusPainted(false);
        confirm.setPreferredSize(new Dimension(360, 60));
        bottom.add(confirm);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createRow(String leftText, String rightText, Font leftFont, Font rightFont) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JLabel left = new JLabel(leftText);
        left.setFont(leftFont);

        JLabel right = new JLabel(rightText, SwingConstants.RIGHT);
        right.setFont(rightFont);

        // spacing inside each row
        left.setBorder(new EmptyBorder(0, 0, 0, 20));
        right.setBorder(new EmptyBorder(0, 20, 0, 0));

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        row.setPreferredSize(new Dimension(100, 65));

        return row;
    }

    public static void main(String[] args) {
        new BookingSummaryFrame();
    }
}
