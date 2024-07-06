import java.util.ArrayList;

public class fourierfilter {
    public double N; // Frequency
    ArrayList<Double> kList = new ArrayList<>(); // "buffer" of N inputs
    double shape = 500; // Shape of cauchy decay curve
    double pi = Math.PI;
    ArrayList<Complex> XKList = new ArrayList<>(); //list of outputs *complex numbers
    ArrayList<Double> XByFrequency = new ArrayList<>(); //Magnitude of the vector in the complex plane -> Frequency thingy
    ArrayList<Double> temporaryamps = new ArrayList<>(); //temporary list of amplitudes used for denoising by amplitude thresholding
    ArrayList<Double> PhaseAngle = new ArrayList<>(); //phase angle, given by angle of complex coordiante
    ArrayList<Double> inverseDTF = new ArrayList<>(); //inverse of DTF
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
            decayFactor = Math.pow(shape, 2) / (Math.pow(shape, 2) + Math.pow(xDecay, 2)); //decay factor for components throughout summation
            for (int n = 0; n < N; n++) {
                double b = -2 * pi * ((k * n) % N) / N;
                double xn = kList.get(n); //input

//                Eulers formula states that e^ix = cos(x) + i sin(x)

                double Ab = Math.cos(b);
                double Bb = Math.sin(b);
//                complex from eulers formula
                Complex Wx = new Complex(xn * Ab * decayFactor, xn * Bb * decayFactor); //create complex value from eulers formula by x[n]

                summation = summation.sum(Wx);
                xDecay++;
            }
            // save output
            XKList.add(summation);
            XByFrequency.add(summation.getAmplitudePYTHAG());
            temporaryamps.add(summation.getAmplitudePYTHAG());
            PhaseAngle.add(summation.getPhaseANGLE());

        }
        kList.clear();
        cleanFrequencies(); //clean output
        inverseFourierTransform();
        XKList.clear();
        temporaryamps.clear();
        //        After a certain sampling hz, reset the buffer
    }

    void cleanFrequencies() {
        for (int i = 0; i < N; i ++) {
            Complex complex = XKList.get(i);
            Double amp  = temporaryamps.get(i);
//            applying threshold
            if (temporaryamps.get(i) < N * 3.5) {
                complex.A *= 0.75;
                complex.B *= 0.75;
            }
            if (i > 3) {
                complex.A *= 0;
                complex.B *= 0;
                XByFrequency.add(0.0);
            }
            else {
                XByFrequency.add(amp);
            }
        }

    }

    void inverseFourierTransform() {




//        Given some new value X(k), a complex number, how do we return that to x[n] our original value?
//        Typically :
//        x[n] = 1 / N * Σ X(k) * W^(-kn) ; 0 <= n <= N-1 ; k = 0 -> N-1
//        Basically, W^(-kn) simplifies to cos(2 * pi * k * n) + i sin(2 * pi * k * n)
//        While the original X(k) sums were : cos(-2 * pi * k * n) + i sin(-2 * pi * k * n)
//       basically we get a conjugate

//        However, we can also just use the conjugate value of X(K) and pass that thru DFT -> x(n) again



        ArrayList<Complex> tempList = new ArrayList<>();
        for (Complex c : XKList) {
            tempList.add(c.conjugation()); //Conjugate values of X(k)
        }
        ArrayList<Complex> newA = new ArrayList<>();
//        double decayFactor = 1; int xDecay = 0;
        for (int n = 0; n < N; n++) { //Run fft on conjugated values
            Complex summation = new Complex(0,0);
//            decayFactor = Math.pow(shape, 2) / (Math.pow(shape, 2) + Math.pow(xDecay, 2));
            for (int k = 0; k < N; k++) {
                double b = -(2 * pi * k * n) / N;
                Complex xn = tempList.get(k); //our conjugate
                double Ab = Math.cos(b);
                double Bb = Math.sin(b);
                Complex Wx = xn.mult(new Complex(Ab,Bb));
                summation = summation.sum(Wx);
//                xDecay++;s
            }
            newA.add(summation);
        }
        int kern = 3;
        for (int f = 0; f < newA.size(); f++) {
            Complex freq = newA.get(f);
            double a = freq.A;
            if (inverseDTF.size()>kern) {
                for (int i = 1; i < kern; i++) {
                    a += inverseDTF.get(inverseDTF.size()-i) * N;
                }
                a /= kern+1;
                inverseDTF.add(a / N * 1.3); //discard complex B :( and scale to counter act *2
            }
            else {
                inverseDTF.add(freq.A / N); //discard complex B :(
            }
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
    double getAmplitudePYTHAG() { return Math.sqrt(Math.pow(A,2) + Math.pow(B,2)); }
    //pythag in complex plane -> magnitude/amplitude
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
