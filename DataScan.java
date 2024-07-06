import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataScan {
    ArrayList<Double> scanData(String input) throws IOException {
        ArrayList<Double> output = new ArrayList<>();
        BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
        String line;
        try {
            while ((line = read.readLine()) != null) {
                output.add(Double.valueOf(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

}
