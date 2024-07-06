import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class MyPanel extends JPanel {
    int xsize, ysize;
    ArrayList<Double> X;
    String title;

    MyPanel(int xsize, int ysize, ArrayList<Double> X, String title) {
        this.xsize = xsize;
        this.ysize = ysize;
        this.X = X;
        this.setPreferredSize(new Dimension(xsize,ysize));
        this.title = title;
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawString(title,xsize / 2, 50);
        g2D.setColor(new Color(100,0,100));


        for (int x = 0; x < X.size(); x++) {
            g2D.drawLine(x, ysize, x, (ysize - (int) Math.round(X.get(x))));
        }


    }
}
