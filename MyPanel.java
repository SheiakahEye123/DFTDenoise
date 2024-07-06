import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class MyPanel extends JPanel {
    int xsize, ysize;
    ArrayList<Double> X;
    String title; int N;

    MyPanel(int xsize, int ysize, ArrayList<Double> X, String title, int N) {
        this.xsize = xsize;
        this.ysize = ysize;
        this.X = X;
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.title = title;
        this.N = N;
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawString(title,xsize / 2, 50);
        g2D.setColor(new Color(100,0,100));


        for (int x = 0; x < X.size(); x++) {
            if (x % N == 0) {
                g2D.setColor(new Color(25,0,100));
                g2D.drawLine(x, ysize, x, 500);
                continue;
            }
            else {
                g2D.setColor(new Color(100,0,100));
            }
            g2D.drawLine(x, ysize, x, (ysize - (int) Math.round(X.get(x))));
        }


    }
}
