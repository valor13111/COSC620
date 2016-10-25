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

            System.out.println("Prime  " + getPrime());
            System.out.println("MX     " + printPolyArray(mx));
            System.out.println("FX     " + printPolyArray(fx));
            System.out.println("GX     " + printPolyArray(gx));

            System.out.println("Addition   : " + printOpArray(addition(fx, gx)));
            System.out.println("Subtraction: " + printOpArray(subtraction(fx, gx)));

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
        if (lineCount > ROWS_PER_PROBLEM) {
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

        // start at fx[i + 1] because mx[0] is placeholder for degree
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

        // start at gx[i + 1] because mx[0] is placeholder for degree
        for (int i = 0; i < x.length; i++) {
            gx[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * Returns the result array holding the coefficients of f(x) + g(x) in GF(p^n).
     *
     * @param fx - polynomial
     * @param gx - polynomial
     * @return     result array
     */
    private int[] addition(int[] fx, int[] gx) {
        int[] result;

        if (isGreater(fx, gx)) {
            result = new int[fx.length - 1];
            result = addHelper(1, fx, gx, result);
        } else if (!isGreater(fx, gx)) {
            result = new int[gx.length - 1];
            result = addHelper(-1, gx, fx, result);
        } else {
            result = new int[fx.length - 1];
            result = addHelper(0, fx, gx, result);
        }

        checkField(getPrime(), result);

        return result;
    }

    /**
     * Returns the result array, handling the logic of the GF(p^n) addition operation.
     *
     * @param op        - number indicating -1, 0, 1 if f(x) is > g(x)
     * @param greater   - greater degree polynomial
     * @param lesser    - lesser degree polynomial
     * @param result    - resulting array holding the coefficients
     * @return          result array
     */
    private int[] addHelper(int op, int[] greater, int[] lesser, int[] result) {
        int difference;
        int lesserStart;

        switch (op) {
            case -1:
                difference = greater[0] - lesser[0];
                lesserStart = 1;

                for (int i = 0; i < difference; i++) {
                    result[i] = greater[i + 1];
                }
                for (int i = difference; i < greater.length - 1; i++) {
                    result[i] = greater[i + 1] + lesser[lesserStart++];
                }
                break;
            case 0:
                for (int i = 0; i < greater.length - 1; i++) {
                    result[i] = greater[i + 1] + lesser[i + 1];
                }
                break;
            case 1:
                difference = greater[0] - lesser[0];
                lesserStart = 1;

                for (int i = 0; i < difference; i++) {
                    result[i] = greater[i + 1];
                }
                for (int i = difference; i < greater.length - 1; i++) {
                    result[i] = greater[i + 1] + lesser[lesserStart++];
                }
                break;
        }

        return result;
    }

    /**
     * Returns the result array holding the coefficients of f(x) - g(x) in GF(p^n).
     *
     * @param fx - polynomial
     * @param gx - polynomial
     * @return     result array
     */
    private int[] subtraction(int[] fx, int[] gx) {
        int[] result;

        if (isGreater(fx, gx)) {
            result = new int[fx.length - 1];
            result = subHelper(1, fx, gx, result);
        } else if (!isGreater(fx, gx)) {
            result = new int[gx.length - 1];
            result = subHelper(-1, gx, fx, result);
        } else {
            result = new int[fx.length - 1];
            result = subHelper(0, fx, gx, result);
        }

        checkField(getPrime(), result);

        return result;
    }

    /**
     * Returns the result array, handling the logic of the GF(p^n) subtraction operation.
     *
     * @param op        - number indicating -1, 0, 1 if f(x) is > g(x)
     * @param a   - greater degree polynomial
     * @param b    - lesser degree polynomial
     * @param result    - resulting array holding the coefficients
     * @return          result array
     */
    private int[] subHelper(int op, int[] a, int[] b, int[] result) {
        int difference;
        int lesserStart;

        switch (op) {
            case -1:
                difference = a[0] - b[0];
                lesserStart = 1;

                for (int i = 0; i < difference; i++) {
                    // multiply by -1 since essentialy we are doing 0 - a[i + 1]
                    result[i] = a[i + 1] * -1;
                }
                for (int i = difference; i < a.length - 1; i++) {
                    result[i] = b[lesserStart++] - a[i + 1];
                }
                break;
            case 0:
                for (int i = 0; i < a.length - 1; i++) {
                    result[i] = a[i + 1] - b[i + 1];
                }
                break;
            case 1:
                difference = a[0] - b[0];
                lesserStart = 1;

                for (int i = 0; i < difference; i++) {
                    result[i] = a[i + 1];
                }
                for (int i = difference; i < a.length - 1; i++) {
                    result[i] = a[i + 1] - b[lesserStart++];
                }
                break;
        }

        return result;
    }

    /**
     * Checks the array for each value in it, making sure they are a part of the
     * finite field.
     *
     * @param prime - Zn where n is a positive prime number
     * @param array - result array to check
     */
    private void checkField(int prime, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!(array[i] >= 0 && array[i] < prime)) {
                array[i] = mod(array[i], prime);
            }
        }
    }

    /**
     * Performs mod on two numbers.
     *
     * @param a - an integer
     * @param b - a positive integer
     * @return  a mod b
     */
    public int mod(int a, int b) {
        if (a >= 0) {
            return a % b;
        } else {
            return a % b + b;
        }
    }

    /**
     * Returns boolean true if all values of the array are zero.
     *
     * @param array - result array
     * @return      - boolean
     */
    public boolean isZeros(int[] array) {
        int sum = 0;

        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }

        if (sum == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a boolean value if the first element in a is greater than or equal to the
     * first element in b.
     *
     * @param a - int array
     * @param b - int array
     * @return    true or false
     */
    public boolean isGreater(int[] a, int[] b) {
        if (a[0] > b[0]) {
            return true;
        } else {
            return false;
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

    /**
     * Prints contents of polynomial array.
     *
     * @param array - polynomial array
     * @return  String output of array
     */
    public String printOpArray(int[] array) {
        String output = "Result Coefficients:\t";

        if (isZeros(array)) {
            output += "0";
        } else {
            for (int i = 0; i < array.length; i++) {
                if (!(i == 0 && array[i] == 0)) {
                    output += array[i] + SPACE;
                }
            }
        }

        return output;
    }

}
