import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <Class Description Here>
 *
 * @author Tyler
 * @version 2016.21.10
 */
public class GF2 {
    private final int ROWS_PER_PROBLEM = 7;
    private final String INPUT_FILE = "input.txt";
    private final String OUTPUT_FILE = "output.txt";
    private final String SPACE = " ";

    private int prime;
    private int[] mx;
    private int[] fx;
    private int[] gx;

    BufferedReader br = null;
    FileWriter fw = null;

    /**
     * Constructor takes no arguments, and sets up file reading and writing.  The main
     * proponent is the while loop, which says sets the String fileLine to the next line
     * ready to be read from, and while it is not null, set the array (of size 3) to the
     * number corresponding to each line in the file.
     *
     * Line 1: the positive prime number
     * Line 2: the degree of m(x)
     * Line 3: the coefficients of m(x)
     * Line 4: the degree of f(x)
     * Line 5: the coefficients of f(x)
     * Line 6: the degree of g(x)
     * Line 7: the coefficients of g(x)
     *
     * @throws IOException
     */
    public GF2() throws IOException {
        String fileLine = null;
        int lineCounter = 1;

        try {
            br = new BufferedReader(new FileReader(INPUT_FILE));
            fw = new FileWriter(OUTPUT_FILE);
            while ((fileLine = br.readLine()) != null) {
                checkCounter(lineCounter++, fileLine);
            }

            System.out.println(getPrime());
            System.out.println(printPolyArray(mx));
            System.out.println(printPolyArray(fx));
            System.out.println(printPolyArray(gx));

            // Sort of a debug print to the console to make sure the file was found and ready successfully
            System.out.print("Successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        fw.close();
    }

    public static void main(String args[]) throws IOException {
        GF2 gf2 = new GF2();
    }

    /**
     * This method checks the line counter, and will invoke a method depending
     * on the number. It will pass the String of the line the program is currently on
     * in the input file.
     *
     * Case Numbers:
     * 1 - gets the degree for FX
     * 2 - gets the coefficients for FX
     * 3 - gets the degree for GX
     * 4 - gets the coefficients for GX
     *
     * @param lineCount int
     * @param fileLine String
     */
    private void checkCounter(int lineCount, String fileLine) {
        if (lineCount > 7) {
            lineCount = 1;
        }

        switch (lineCount) {
            case 1: setPrime(fileLine);
                break;
            case 2: degreeMX(fileLine);
                break;
            case 3: coefficientsMX(fileLine);
                break;
            case 4: degreeFX(fileLine);
                break;
            case 5: coefficientsFX(fileLine);
                break;
            case 6: degreeGX(fileLine);
                break;
            case 7: coefficientsGX(fileLine);
        }
    }

    /**
     * Sets the prime number of GF(p^n).
     *
     * @param fileLine - String number
     */
    private void setPrime(String fileLine) {
        prime = Integer.parseInt(fileLine);
    }

    /**
     * Returns the prime number field.
     *
     * @return  prime
     */
    private int getPrime() {
        return prime;
    }

    /**
     * Sets the degree of m(x).
     *
     * @param fileLine - String number
     */
    private void degreeMX(String fileLine) {
        mx = new int[Integer.parseInt(fileLine) + 2];
        mx[0] = Integer.parseInt(fileLine);
    }

    /**
     * Sets the coefficients of m(x).
     *
     * @param fileLine - String number(s)
     */
    private void coefficientsMX(String fileLine) {
        String[] x = fileLine.split(SPACE);

        // start at mx[i + 1] because mx[0] is placeholder for degree
        for (int i = 0; i < x.length; i++) {
            mx[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * Sets the degree of f(x).
     *
     * @param fileLine - String number
     */
    private void degreeFX(String fileLine) {
        fx = new int[Integer.parseInt(fileLine) + 2];
        fx[0] = Integer.parseInt(fileLine);
    }

    /**
     * Sets the coefficients of f(x).
     *
     * @param fileLine - String number(s)
     */
    private void coefficientsFX(String fileLine) {
        String[] x = fileLine.split(SPACE);

        // start at mx[i + 1] because mx[0] is placeholder for degree
        for (int i = 0; i < x.length; i++) {
            fx[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * Sets the degree of g(x).
     *
     * @param fileLine - String number
     */
    private void degreeGX(String fileLine) {
        gx = new int[Integer.parseInt(fileLine) + 2];
        gx[0] = Integer.parseInt(fileLine);
    }

    /**
     * Sets the coefficients of g(x).
     *
     * @param fileLine - String number(s)
     */
    private void coefficientsGX(String fileLine) {
        String[] x = fileLine.split(SPACE);

        // start at mx[i + 1] because mx[0] is placeholder for degree
        for (int i = 0; i < x.length; i++) {
            gx[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * Prints contents of polynomial array.
     *
     * @param array - polynomial array
     * @return  String output of array
     */
    public String printPolyArray(int[] array) {
        String output = "";

        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                output += "Degree:\t" + array[i] + "\tCoefficients:\t";
            } else {
                output += SPACE + array[i];
            }
        }

        return output;
    }

}
