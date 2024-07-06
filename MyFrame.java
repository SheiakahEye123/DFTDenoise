import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyFrame extends JFrame {
    MyPanel panel;
    MyFrame(int xsize, int ysize, ArrayList<Double> X, String title) {
        panel = new MyPanel(xsize,ysize,X, title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(xsize,ysize);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(500, 500));
    }
}

