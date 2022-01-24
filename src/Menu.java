import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    private optionPanel panel;
    private int state = -1;

    Menu() {
        this.panel = new optionPanel();
        this.setTitle("Java Knight 2D");
        this.add(panel);
        this.setSize(new Dimension(1280, 720));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(this.getMinimumSize());
        this.setResizable(false);
        this.setVisible(true);
    }

    private class optionPanel extends JPanel {

        optionPanel() {
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            {
                // creates the border and layout manager for this gui
                this.setBorder(new EmptyBorder(10, 10, 10, 10));
                this.setLayout(new GridBagLayout());

                // creates the constraints for our components, centers elements
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridwidth = GridBagConstraints.REMAINDER;
                constraints.anchor = GridBagConstraints.NORTH;

                // sets the font for and creates the title text
                JLabel title = new JLabel("Java Knight 2D");
                title.setFont(new Font("Monospaced", Font.PLAIN, 25));
                JLabel subtitle = new JLabel("by Wajeeh, Jon, Ray, Shivansh, and Victor");
                subtitle.setFont(new Font("Monospaced", Font.PLAIN, 15));
                this.add(title, constraints);
                this.add(subtitle, constraints);

                // changes the constraints for buttons
                constraints.anchor = GridBagConstraints.CENTER;
                constraints.fill = GridBagConstraints.HORIZONTAL;

                // creates a panel for our button as well as the buttons themselves
                JPanel buttons = new JPanel(new GridBagLayout());
                JButton play = new JButton("Play");
                JButton edit = new JButton("Edit");
                JButton quit = new JButton("Quit");

                // adds the buttons to the panel
                buttons.add(play, constraints);
                buttons.add(edit, constraints);
                buttons.add(quit, constraints);

                // weighty specifies spacing between elements
                constraints.weighty = 1;
                this.add(buttons, constraints);

                // add action listeners for each button
                play.addActionListener(new playButtonListener());
                edit.addActionListener(new editButtonListener());
                quit.addActionListener(new quitButtonListener());
            }
        }
    }

    // ******************************* Action Listeners ******************************* //

    class playButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play Button Clicked");
            Object[] buttons = {"Singleplayer", "Coop", "Multiplayer"};
            int optionDialog = JOptionPane.showOptionDialog(null, "Pick an Option", "Menu",
                    JOptionPane.YES_NO_CANCEL_OPTION, 0, null, buttons, buttons[2]);

            if (optionDialog == JOptionPane.YES_OPTION){
                state = 0;
                setVisible(false);
                dispose();
            }

            if (optionDialog == JOptionPane.NO_OPTION){
                state = 1;
                setVisible(false);
                dispose();
            }

            if (optionDialog == JOptionPane.CANCEL_OPTION){
                state = 2;
                setVisible(false);
                dispose();
            }
        }
    }

    class editButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Edit Button Clicked");
        }
    }

    class quitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    @Override
    public int getState() {
        return state;
    }
}