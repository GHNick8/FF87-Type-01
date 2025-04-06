package game;

import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        /*
            Compile & Run

            javac game/*.java 

            java game.Game 
            
         */

         JFrame window = new JFrame("FF87 Type-01");
         GamePanel gamePanel = new GamePanel();

         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         window.setResizable(false);
         window.setContentPane(gamePanel);
         window.pack();
         window.setLocationRelativeTo(null);
         window.setVisible(true);

         gamePanel.startGameThread();
    }
}