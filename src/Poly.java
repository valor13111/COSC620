import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This program will take an input file, and read line by line differentiating the powers and
 * coefficients of f(x) and g(x).  It outputs the addition, subtraction, and multiplication
 * of the polynomials in a file, 'output.txt.'
 *
 * @author Tyler Reed
 * @version 2016.9.14
 */
public class Poly {

    public int[] arrayFX;
    public int[] arrayGX;
    public int[] resultPolynomial;
    // used for multiplication polynomial
    public int[][] mp;

    // input and output file are in same directory of java file
    public final String INPUT_FILE     = "input.txt";
    public final String OUTPUT_FILE    = "output.txt";
    public final String ADDITION       = "Polynomial Addition       : ";
    public final String SUBTRACTION    = "Polynomial Subtraction    : ";
    public final String MULTIPLICATION = "Polynomial Multiplication : ";
    public final String SPACE          = " ";
    public final String ZERO           = "0";

    BufferedReader br = null;
    FileWriter fw = null;

    public Poly() throws IOException {
        String fileLine = null;
        int lineCounter = 1;

        try {
            br = new BufferedReader(new FileReader(INPUT_FILE));
            fw = new FileWriter(OUTPUT_FILE);
            while ((fileLine = br.readLine()) != null) {
                checkCounter(lineCounter++, fileLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        fw.close();
    }

    public static void main(String arg[]) throws Exception {
        Poly p = new Poly();
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
        if (lineCount > 4) {
            lineCount = 1;
        }

        switch (lineCount) {
            case 1: degreeFX(fileLine);
                break;
            case 2: coefficientsFX(fileLine);
                break;
            case 3: degreeGX(fileLine);
                break;
            case 4: coefficientsGX(fileLine);
                printPoly(addPolynomials(arrayFX, arrayGX, resultPolynomial), 1);
                printPoly(subPolynomials(arrayFX, arrayGX, resultPolynomial), 2);
                printPoly(multPolynomials(arrayFX, arrayGX), 3);
                break;
        }
    }

    /**
     * This method gets the degree of FX from the input file, and then stores it into
     * an array named arrayFX[].  The degree is the first index in the array.  The array
     * length is the power + 2, this gives one for the degree place holder, and another for the 'zero'
     * degree.
     *
     * I.E. degree of FX = 2, so example polynomial is 2x^2 + 4x + 1.  There are three coefficients,
     * so we need to add one to the length of the array to make up for the zero degree.
     *
     * @param fileLine
     */
    private void degreeFX(String fileLine) {
        String[] x = fileLine.split(SPACE);
        int aLength = Integer.parseInt(x[0]);

        arrayFX = new int[aLength + 2];
        arrayFX[0] = aLength;
    }

    /**
     * The coefficients are placed into the arrayFX starting at index 1, since index 0 is reserved
     * for the degree of the polynomial.
     *
     * @param fileLine
     */
    private void coefficientsFX(String fileLine) {
        String[] x = fileLine.split(SPACE);

        for (int i = 0; i < arrayFX.length - 1; i++) {
            arrayFX[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * This method gets the degree of GX from the input file, and then stores it into
     * an array named arrayGX[].  The degree is the first index in the array.  The array
     * length is the power + 2, this gives one for the degree place holder, and another for the 'zero'
     * degree.
     *
     * I.E. degree of GX = 2, so example polynomial is 2x^2 + 4x + 1.  There are three coefficients,
     * so we need to add one to the length of the array to make up for the zero degree.
     *
     * @param fileLine
     */
    private void degreeGX(String fileLine) {
        String[] x = fileLine.split(SPACE);
        int aLength = Integer.parseInt(x[0]);

        arrayGX = new int[aLength + 2];
        arrayGX[0] = aLength;
    }

    /**
     * The coefficients are placed into the arrayGX starting at index 1, since index 0 is reserved
     * for the degree of the polynomial.
     *
     * @param fileLine
     */
    private void coefficientsGX(String fileLine) {
        String[] x = fileLine.split(SPACE);

        for (int i = 0; i < arrayGX.length - 1; i++) {
            arrayGX[i + 1] = Integer.parseInt(x[i]);
        }
    }

    /**
     * Adds two polynomials together, which are stored in two arrays.
     *
     * @param arrayFX
     * @param arrayGX
     * @return  result int[] array
     */
    private int[] addPolynomials(int[] arrayFX, int[] arrayGX, int[] result) {
        int indexFXGX = 1;

        // FX and GX have the same degree
        if (arrayFX[0] == arrayGX[0]) {
            result = new int[arrayFX.length - 1];

            for (int i = 0; i < result.length; i++) {
                result[i] = arrayFX[i + 1] + arrayGX[i + 1];
            }
            // polynomial FX has greater degree
        } else if (hasGreaterDegree(arrayFX, arrayGX)) {
            // creates the result array length to be arrayFX length - 1
            // int difference is equal to the degree of FX - degree of GX
            result = new int[arrayFX.length - 1];
            int difference = arrayFX[0] - arrayGX[0];

            // loop to keep the numbers which are a higher degree than the other polynomial
            for (int i = 0; i < difference; i++) {
                result[i] = arrayFX[i + 1];
            }
            for (int i = difference; i < result.length; i++) {
                result[i] = arrayFX[i + 1] + arrayGX[indexFXGX++];
            }
            // polynomial GX has greater degree
        } else {
            // creates the result array length to be arrayGX length - 1
            // int difference is equal to the degree of GX - degree of FX
            result = new int[arrayGX.length - 1];
            int difference = arrayGX[0] - arrayFX[0];

            // loop to keep the numbers which are a higher degree than the other polynomial
            // but, since we are doing FX - GX, they must be multiplied by -1 since we are subtracting
            for (int i = 0; i < difference; i++) {
                result[i] = arrayGX[i + 1];
            }
            for (int i = difference; i < result.length; i++) {
                result[i] = arrayFX[indexFXGX++] + arrayGX[i + 1];
            }
        }

        return result;
    }

    /**
     * Subtracts two polynomials together, which are stored in two arrays.
     *
     * If the degree of the polynomials are not the same, the corresponding if statements will keep the
     * numbers which are of higher degree than the others.  If GX > FX, then each number is multiplited by one.
     * Then each for loop will subtract the corresponding numbers of which are the same degree.
     *
     * @param arrayFX
     * @param arrayGX
     */
    private int[] subPolynomials(int[] arrayFX, int[] arrayGX, int[] result) {
        int indexFXGX = 1;

        // FX and GX have the same degree
        if (arrayFX[0] == arrayGX[0]) {
            result = new int[arrayFX.length - 1];

            for (int i = 0; i < result.length; i++) {
                result[i] = arrayFX[i + 1] - arrayGX[i + 1];
            }
            // polynomial FX has greater degree
        } else if (hasGreaterDegree(arrayFX, arrayGX)) {
            // creates the result array length to be arrayFX length - 1
            // int difference is equal to the degree of FX - degree of GX
            result = new int[arrayFX.length - 1];
            int difference = arrayFX[0] - arrayGX[0];

            // loop to keep the numbers which are a higher degree than the other polynomial
            for (int i = 0; i < difference; i++) {
                result[i] = arrayFX[i + 1];
            }
            for (int i = difference; i < result.length; i++) {
                result[i] = arrayFX[i + 1] - arrayGX[indexFXGX++];
            }
            // polynomial GX has greater degree
        } else {
            // creates the result array length to be arrayGX length - 1
            // int difference is equal to the degree of GX - degree of FX
            result = new int[arrayGX.length - 1];
            int difference = arrayGX[0] - arrayFX[0];

            // loop to keep the numbers which are a higher degree than the other polynomial
            // but, since we are doing FX - GX, they must be multiplied by -1 since we are subtracting
            for (int i = 0; i < difference; i++) {
                result[i] = arrayGX[i + 1] * -1;
            }
            for (int i = difference; i < result.length; i++) {
                result[i] = arrayFX[indexFXGX++] - arrayGX[i + 1];
            }
        }

        return result;
    }

    /**
     * Multiplies two polynomials together, which are stored in two arrays.
     *
     * If the degree of FX and GX are the same, then the multiplication of each number is added to the row of the result array.
     * If the degree of FX > GX, or GX > FX, then the multiplicaion of each number is added to the column of the result array.
     *
     * @param arrayFX
     * @param arrayGX
     */
    private int[] multPolynomials(int[] arrayFX, int[] arrayGX) {
        int count;

        if (arrayFX[0] == arrayGX[0]) {
            mp = new int[arrayFX.length][arrayGX.length];
            for (int i = 0; i < arrayFX.length - 1; i++) {
                for (int j = 0; j < arrayGX.length - 1; j++) {
                    mp[j][i] = arrayFX[i + 1] * arrayGX[j + 1];
                }
            }
        } else if (hasGreaterDegree(arrayFX, arrayGX)) {
            mp = new int[arrayFX.length + 1][arrayGX.length];
            for (int i = 0; i < arrayFX.length - 1; i++) {
                // increases the 2d array by one after each iteration through the nested loop
                // this essentially makes sure the degrees that are being multiplied stay on the
                // same row in the 2d array
                count = i;
                for (int j = 0; j < arrayGX.length - 1; j++) {
                    mp[count++][i] = arrayFX[i + 1] * arrayGX[j + 1];
                }
            }
        } else {
            mp = new int[arrayFX.length][arrayGX.length + 1];
            for (int i = 0; i < arrayFX.length - 1; i++) {
                // increases the 2d array by one after each iteration through the nested loop
                // this essentially makes sure the degrees that are being multiplied stay with
                // each other in the 2d array
                count = i;
                for (int j = 0; j < arrayGX.length - 1; j++) {
                    mp[i][count++] = arrayFX[i + 1] * arrayGX[j + 1];
                }
            }
        }

        return finalizeMultPolynomial(mp, resultPolynomial);
    }

    /**
     * Converts the 2d array into a single-dimensional array by adding all numbers within each
     * array index together.
     *
     * This is an example if FX has a higher degree than GX.
     * I.E. int[3][3]: [ 2x^2,  0,   0 ]
     *                 [ 0   , 2x,   2 ]
     *                 [ 0   , 1x,  -3 ]
     *
     * This adds up each column to the resulting single int array to be: int[3] = {2, 3, -1}
     *
     * If GX had higher degree, then the rows are added up instead of columns.
     *
     * @param array
     * @param result
     * @return  int[] result
     */
    private int[] finalizeMultPolynomial(int[][] array, int[] result) {
        int sum = 0;

        if (array[0].length == array.length) {
            result = new int[array.length];

            for (int i = 0; i < array[0].length; i++) {
                for (int j = 0; j < array.length; j++) {
                    sum += array[i][j];
                }
                result[i] = sum;
                sum = 0;
            }
        }
        // if there are more rows than columns, meaning FX has higher degree
        else if (array[0].length > array.length) {
            result = new int[array[0].length];

            for (int i = 0; i < array[0].length - 1; i++) {
                for (int j = 0; j < array.length - 1; j++) {
                    sum += array[j][i];
                }
                result[i] = sum;
                sum = 0;
            }
            // if there are mor columns than rows, meaning GX has higher degree
        } else {
            result = new int[array.length];

            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[0].length; j++) {
                    sum += array[i][j];
                }
                result[i] = sum;
                sum = 0;
            }
        }

        return result;
    }

    /**
     * Returns true if the result polynomial is zero.
     * I.E. f(x) + g(x), f(x) - g(x), or f(x) * g(x) = 0
     *
     * @param array
     * @return  true or false
     */
    private boolean checkZeroSum(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Writes the formatted output to a file.  A switch case is used here for future
     * addition of other operations.  When the print method is called elsewhere in the program,
     * the numbers in the second parameter are specific to what operation is performed.
     *
     * Case Numbers:
     * 1 - Addition
     * 2 - Subtraction
     * 3 - Multiplication
     *
     * @param array
     * @param number - this is used to denote which operation is taking place.
     */
    private void printPoly(int[] array, int number) {
        String output = "";

        // switch case for outputting string to file
        switch (number) {
            case 1: output = ADDITION;
                break;
            case 2: output = SUBTRACTION;
                break;
            case 3: output = MULTIPLICATION;
                break;
        }

        // if only zero's exist in result array, output zero
        if (checkZeroSum(array)) {
            output += ZERO;
            try {
                fw.write(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // if there are other numbers in the array > 0, then it will start
            // at the index of the first number > 0
            // this is to get rid of any leading zero's in the output file
        } else {
            int num = findFirstNonZero(array);
            for (int i = num; i < array.length; i++) {
                output += "" + array[i] + SPACE;
            }
            output += "\n";
            try {
                fw.write(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Finds the first non-zero in the resultPolynomial array.
     * This allows for correct writing to the text file, getting rid of any leading
     * zero's for polynomials that aren't equal to zero.
     *
     * @param array
     * @return  first non-zero index
     */
    private int findFirstNonZero(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                return i;
            }
        }

        return array.length;
    }

    /**
     * Returns true if first parameter array has greater degree, else second parameter array
     * has the greater degree.
     *
     * @param arrayFX
     * @param arrayGX
     * @return  true or false
     */
    private boolean hasGreaterDegree(int[] arrayFX, int[] arrayGX) {
        if (arrayFX[0] >= arrayGX[0]) {
            return true;
        }

        return false;
    }
}
