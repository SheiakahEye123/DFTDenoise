import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        final int N = 60;
        System.out.println("Hello world!");
        DataScan scanner = new DataScan();
        ArrayList<Double> input = scanner.scanData("C:/Users/kaide/IdeaProjects/fftdenoise/src/message.txt");
//        System.out.println(input);

        for (int i = 0; i < input.size() % N; i++) {
            input.add((double) 0);
        }
        fourierfilter DFT = new fourierfilter(N);
        for (Double d : input) {
            DFT.update(d);
        }
        System.out.println(input.size());
        System.out.println(DFT.inverseDTF.size());
        MyFrame otherFrame = new MyFrame(input.size(), 700, input, "innput");
//        MyFrame frame = new MyFrame(DFT.XByFrequency.size(), 1080, DFT.XByFrequency, "freq");
        MyFrame thirdf = new MyFrame(DFT.inverseDTF.size(), 700, DFT.inverseDTF, "cleand");


//        MyFrame thirdframe= new MyFrame(DFT.PhaseAngle.size(), 700, DFT.PhaseAngle);


    }
}