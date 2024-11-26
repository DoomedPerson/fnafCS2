import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MenuScreen extends JFrame {
    boolean start = false;
    private MenuPanel menuPanel;

    public MenuScreen() {

        // setting up Jframe
        setTitle("Five Night's at Harold's (scary)");
        setSize(896, 896);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

       
        menuPanel = new MenuPanel();
        add(menuPanel);
        setVisible(true);
    }

    // Custom JPanel for mouse
    class MenuPanel extends JPanel {

        public MenuPanel() {
            // Add a MouseListener
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleMouseClick(e.getX(), e.getY());
                }
            });
        }

        // clicking mode
        private void handleMouseClick(int mouseX, int mouseY) {
            // making boxes for input, can be changed if needed
            Rectangle startButton = new Rectangle(360, 600, 200, 80);  // Start button
            Rectangle exitButton = new Rectangle(360, 700, 200, 80);   // Exit button

            // Check if start button was clicked
            if (startButton.contains(mouseX, mouseY)) {

                dispose();

                // You can start the game or go to the night screen. Just both the dispose because of how I did this
            }
            // Check if exit button was clicked
            else if (exitButton.contains(mouseX, mouseY)) {
                System.exit(0);

            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            //background color
            g.setColor(Color.BLACK);
            g.fillRect(0,0,896,896);

            // Title (probably change this)
            g.setFont(new Font("DialogInput", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("Five Nights Town", 240, 100);

            // start
            g.setColor(Color.WHITE);
            g.setFont(new Font("DialogInput", Font.PLAIN, 50));
            g.setColor(Color.WHITE);
            g.drawRect(360, 600, 200, 80);  // Button border
            g.drawString("Start", 380, 660);

            // exit
            g.setColor(Color.WHITE);
            g.drawRect(360, 700, 200, 80);  // Button border
            g.drawString("Exit", 396, 760);


        }

    }


}
