import java.util.ArrayList;

public class fourierfilter {
// The fourier transformatin is a very sigma thing in math
// The basic idea in a Discrete Fourier Transformation is to recieve outstanding Frequencies within a given signal

    public double N; // Frequency
    ArrayList<Double> kList = new ArrayList<>();
    double shape = 500;
    double pi = Math.PI;
    ArrayList<Complex> XKList = new ArrayList<>();
    ArrayList<Double> XByFrequency = new ArrayList<>(); //Magnitude of the vector in the complex plane -> Frequency thingy
    ArrayList<Double> PhaseAngle = new ArrayList<>();
    ArrayList<Double> inverseDTF = new ArrayList<>();
    fourierfilter(double N) {
        this.N = N;
    }
    void update(double x) {
        kList.add(x);
        if (kList.size() != N) {
            return;
        }
        int xDecay = 0; double decayFactor = 1;
        for (int k = 0; k < N; k++) {
            Complex summation = new Complex(0,0);
            decayFactor = Math.pow(shape, 2) / (Math.pow(shape, 2) + Math.pow(xDecay, 2));
            for (int n = 0; n < N; n++) {
                double b = -2 * pi * ((k * n) % N) / N;
                double xn = kList.get(n); //input

//                Eulers formula states that e^ix = cos(x) + i sin(x)

                double Ab = Math.cos(b);
                double Bb = Math.sin(b);

                Complex Wx = new Complex(xn * Ab * decayFactor, xn * Bb * decayFactor); //create complex value from eulers formula by x[n]

                summation = summation.sum(Wx);
                xDecay++;
            }
            XKList.add(summation);
            XByFrequency.add(summation.getAmplitudePYTHAG());
            PhaseAngle.add(summation.getPhaseANGLE());

        }
        kList.clear();
        System.out.println(XKList.size());
        cleanFrequencies();
        inverseFourierTransform();
        XKList.clear();
        //        After a certain sampling hz, reset the buffer
    }

    void cleanFrequencies() {
        double threshold = 0.90;
        double avgA = 0; double avgB = 0; double avgF = 0;
        for (Complex complex : XKList) {
            avgA += complex.A;
            avgB += complex.B;
        }
        avgA /= N;
        avgB /= N;
        for (int i = 0; i < N; i ++) {
            Complex complex = XKList.get(i);
            double ampl = complex.getAmplitudePYTHAG();
            if (i > N * threshold) {
                double shapeslope = 1 - Math.abs(1 - (i / N) - threshold);
                complex.A *= shapeslope;
                complex.B *= shapeslope;
            }
//            if (ampl < N * 10) {
//                complex.A *= avgA;
//                complex.B *= avgB;
//            }
        }
    }

    void inverseFourierTransform() {




//        Given some new value X(k), a complex number, how do we return that to x[n] our original value?
//        *We first implement rudimentary filters to flatten out unwated noisy frequency
//        Typically :
//        x[n] = 1 / N * Σ X(k) * W^(-kn) ; 0 <= n <= N-1 ; k = 0 -> N-1
//        Basically, W^(-kn) simplifies to cos(2 * pi * k * n) + i sin(2 * pi * k * n)
//        While the original X(k) sums were : cos(-2 * pi * k * n) + i sin(-2 * pi * k * n)
//        This gives a conjugate value! We can from there return to x[n]

//        However, we can also just use the conjugate value of X(k) right off the bat, and from there apply an FFT
//        to return to x[n]!



        ArrayList<Complex> tempList = new ArrayList<>();
        for (Complex c : XKList) {
            tempList.add(c.conjugation()); //Conjugate values of X(k)
        }
        ArrayList<Complex> newFreq = new ArrayList<>();
        for (int n = 0; n < N; n++) { //Run fft on conjugated values
            Complex summation = new Complex(0,0);
            for (int k = 0; k < N; k++) {
                double b = -(2 * pi * k * n) / N;
                Complex xn = tempList.get(k); //our conjugate
                double Ab = Math.cos(b);
                double Bb = Math.sin(b);
                Complex Wx = xn.mult(new Complex(Ab,Bb));
                summation = summation.sum(Wx);
            }
            newFreq.add(summation);
        }

        for (Complex freq : newFreq) {
            inverseDTF.add(freq.A / N); //discard complex B :(
//            divide by N undo sum
        }
    }
}

class Complex {
    public double A,B;
    Complex(double A, double B) {
        this.A = A;
        this.B = B;
    }
    double getAmplitudePYTHAG() {
        return Math.sqrt(Math.pow(A,2) + Math.pow(B,2));
    }
    double getPhaseANGLE() {
        return Math.atan(B / A);
    }
    Complex sum(Complex addend) {
        return new Complex(addend.A + A, addend.B + B);
    }
    Complex conjugation(){ return new Complex(A, -B); }

    Complex mult(Complex xy) {
//        (a + bi)(x + yi) = a(x + yi) + bi(x + yi) =
//        ax + ayi + bix - y
        double mA = (A * xy.B) + (B * xy.A);
        double mB = (A * xy.A) - (B * xy.B);
        return new Complex(mB, mA);
    }

}
