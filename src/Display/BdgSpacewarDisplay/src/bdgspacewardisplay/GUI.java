package bdgspacewardisplay;

import java.awt.*;
import static java.awt.Color.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI {
    private static JFrame frame;
    private static JPanel contentPane;
    private static AnimationPanel animation;
    static final int SIZE = 600;
    static World W;
    static GameStatus GS_ = null;
    static GameStatus GS;
    static Scanner sc;
    static Timer T;
    static final int timerStep = 33;
    static String S;
    
    static {
        try {
            sc = new Scanner(new FileReader("be.txt"));
        } catch (FileNotFoundException ex) {
            System.err.println("Hajaj! Baj van! A fájl elbújt.");
        }
        double tmp[] = {0,0,0,0,100,100};
        W = new World(-1, tmp);
   }
    
    private static void createAndShowGUI() {
        frame = new JFrame("bdg.spacewar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        
        animation = new AnimationPanel();
        contentPane.add(animation);
        
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
                T = new Timer(timerStep, updateTimerAction);
                T.setInitialDelay(timerStep);
                T.setCoalesce(false);
                T.start();
            }
        });
    }
    
    private static final ActionListener updateTimerAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // új helyzetek létrehozása
            if (sc.hasNextLine()) S = sc.nextLine();
            if (GS_ == null) {
                GS_ = new GameStatus(S);
                for (SpaceObject so : GS_.obj) if (so instanceof World) W = (World) so;
            }
            GS = new GameStatus(S);
            // újrarajzolás
            animation.paintImmediately(0, 0, animation.getWidth(), animation.getHeight());
        }
    };
    
    static BufferedImage readImage(String s) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(GUI.class.getResourceAsStream("/images/"+s));
        } catch (IOException /*| IllegalArgumentException */ e) {
            System.err.println("Hiba a kép olvasásánál.");
        }
        return img;
    }
}

class AnimationPanel extends JPanel {
    public AnimationPanel() {
        setBorder(BorderFactory.createLineBorder(BLACK));
        setBackground(WHITE);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GUI.SIZE,GUI.SIZE);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Draw D = new Draw((Graphics2D) g, GUI.SIZE, GUI.SIZE);
        D.setScale(GUI.W.XMIN, GUI.W.XMAX, GUI.W.YMIN, GUI.W.YMAX);
        D.setScale(-1000, 1000, -1000, 1000);
        if (GUI.GS != null) GUI.GS.draw(D, GUI.W);
        if (GUI.GS_ != null) GUI.GS_.drawMap(D, GUI.W);
    }
}