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
            System.out.println("Multiply   : " + printOpArray(multiplication(fx, gx)));

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
        } else if (!isGreater(fx, gx) && fx[0] != gx[0]) {
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
        } else if (!isGreater(fx, gx) && fx[0] != gx[0]) {
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
     * @param a         - greater degree polynomial
     * @param b         - lesser degree polynomial
     * @param result    - resulting 2D array holding the coefficients
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
     * Returns the result array holding the coefficients of f(x) * g(x) in GF(p^n).
     *
     * @param fx - polynomial
     * @param gx - polynomial
     * @return     result array
     */
    private int[] multiplication(int[] fx, int[] gx) {
        int[] result;
        int[][] tempResult;

        if (isGreater(fx, gx)) {
            tempResult = new int[fx.length][gx.length];
            tempResult = multHelper(1, fx, gx, tempResult);
        } else if (!isGreater(fx, gx) && fx[0] != gx[0]) {
            tempResult = new int[gx.length][fx.length];
            tempResult = multHelper(-1, gx, fx, tempResult);
        } else {
            tempResult = new int[fx.length + 1][gx.length - 1];
            tempResult = multHelper(0, fx, gx, tempResult);
        }

        // this becomes d(x)
        result = multCombineHelper(tempResult);

        checkField(getPrime(), result);

        result = PLDA(getPrime(), result, mx);

        return result;
    }

    /**
     * Returns the result array, handling the logic of the GF(p^n) multiplication operation.
     *
     * @param op        - number indicating -1, 0, 1 if f(x) is > g(x)
     * @param a         - greater degree polynomial
     * @param b         - lesser degree polynomial
     * @param result    - resulting 2D array holding the coefficients
     * @return          result array
     */
    private int[][] multHelper(int op, int[] a, int[] b, int[][] result) {
        int count = 0;

        switch (op) {
            case -1:
                for (int i = 0; i < b.length - 1; i++) {
                    count = i;
                    for (int j = 0; j < a.length - 1; j++) {
                        result[j + count][i] = b[i + 1] * a[j + 1];
                    }
                }
                break;
            case 0:
                for (int i = 0; i < a.length - 1; i++) {
                    count = i;
                    for (int j = 0; j < b.length - 1; j++) {
                        result[j + count][i] = a[i + 1] * b[j + 1];
                    }
                }
                break;
            case 1:
                for (int i = 0; i < a.length - 1; i++) {
                    count = i;
                    for (int j = 0; j < b.length - 1; j++) {
                        result[j + count][i] = a[i + 1] * b[j + 1];
                    }
                }
                break;
            case 2:
                for (int i = 0; i < a[0]; i++) {
                    for (int j = 0; j < b.length - 1; j++) {
                        result[i][j] = a[i + 1] * b[j + 1];
                    }
                }
        }

        return result;
    }

    /**
     * Returns the result array, handling the logic of the GF(p^n) multiplication operation.
     *
     * @param op        - number indicating -1, 0, 1 if f(x) is > g(x)
     * @param a         - greater degree polynomial
     * @param b         - lesser degree polynomial
     * @param result    - resulting array holding the coefficients
     * @return          result array
     */
    private int[] multHelper(int op, int[] a, int[] b, int[] result) {
        switch (op) {
            case 2:
                int constant = 1;
                int[] res = new int[result.length + 1];
                res[0] = (a[0] + b[0]);
                for (int i = 0; i < b.length - 1; i++) {
                    res[i + 1] = a[constant] * b[i + 1];
                }
                result = res;
                break;
        }

        return result;
    }

    /**
     * Returns the addition of each column into a single array.
     *
     * @param array - 2d array
     * @return        single array with added columns from 2d array
     */
    private int[] multCombineHelper(int[][] array) {
        int[] result = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            int temp = 0;
            for (int j = 0; j < array[0].length; j++) {
                temp += array[i][j];
            }
            result[i] = temp;
        }

        return result;
    }

    /**
     * Polynomail Long Division Algorithm to return the quotient and remainder.
     *
     * @param prime - prime number in GF(p^n)
     * @param nx    - f(x) * g(x) after mod check
     * @param dx    - m(x)
     * @return      int array
     */
    private int[] PLDA(int prime, int[] nx, int[] dx) {
        checkField(prime, nx);
        checkField(prime, dx);
        int[] qx = new int[2];  // initialize qx[0] and qx[1] to zero
        int[] rx = nx;

        while (rx[0] != 0 && rx[0] >= dx[0]) {
            int txDegree = (rx.length - 1) - dx[0];
            int txCoefficient = finiteDivision(prime, rx[0], lead(dx));
            int tx[] = {txDegree, txCoefficient};

            qx[0] += tx[0];
            qx[1] += tx[1];

            int[] r = new int[dx.length];
            r[0] = dx.length - 1;
            rx = subtraction(rx, multHelper(2, qx, dx, r));

            return rx;
        }

        return new int[0];
    }

    /**
     * Returns the quotient of two integers in the finite set (0, ..., p - 1) where p
     * is a positive prime number.  First, find the multiplicative inverse by calling the
     * Extended Euclidean Algorithm.  The first number of the pair of integers it returns
     * is the multiplicative inverse.  Then it computes a * (mult. inverse of b) and returns
     * the result.  It is checked if the result is in the finite set, and if not, then
     * the result is sent through an if statement where if it's less than zero, you add the
     * prime number to bring it in the set.
     *
     * @param prime a prime number
     * @param a an integer
     * @param b an integer
     * @return  quotient || if (quotient < 0) then quotient % prime + prime || quotient % prime
     */
    private int finiteDivision(int prime, int a, int b) {
        int [] inverse = EEA(b, prime);
        int quotient = a * inverse[0];

        if (quotient >= 0 && quotient <= prime - 1) {
            return quotient;
        } else {
            if (quotient < 0) {
                return quotient % prime + prime;
            } else {
                return quotient % prime;
            }
        }
    }

    /**
     * Algorithm EEA (Extended Euclidean Algorithm)
     *
     * @param a an (> 0) integer
     * @param b another (>= 0) integer
     *
     * @return int array (u,v) satisfying u*a + v*b = gcd(a,b)
     */
    private int[] EEA(int a, int b){
        if (b == 0){
            return new int[]{1,0};
        } else {
            int q = a  /b; int r = a % b;
            int[] R = EEA (b ,r);
            return new int[]{R[1], R[0] - q * R[1]};
        }
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
     * Returns lead coefficient.  In f(x), g(x), and m(x), the lead coefficient is index 1.
     *
     * @param a - polynomial
     * @return    lead coefficient
     */
    public int lead(int[] a) {
        return a[1];
    }

    /**
     * Prints contents of polynomial array.
     *
     * @param array - polynomial array
     * @return        String output of array
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
     * Prints conents of polynomial array.
     *
     * @param array - polynomial array
     * @return        String output of array
     */
    public String printPolyArray(int[][] array) {
        String output = "";

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (i == 0 && j == 0) {
                    output += "Degree:\t" + array[i] + "\tCoefficients:\t";
                } else {
                    output += SPACE + array[i][j];
                }
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
