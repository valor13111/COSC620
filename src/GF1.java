import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This project implements the first type of finite field Zp, and four operations
 * “+”, “-”, “*”, and “/”, respectively. Specifically, this program will read a modulus p and
 * two elements x and y from a file named “input.txt”. Then the program will create a file
 * named “output.txt” and write the results to “output.txt”.
 *
 * @author Tyler Reed
 * @version 2016.9.27
 */
public class GF1 {

    public final String INPUT_FILE     = "input.txt";
    public final String OUTPUT_FILE    = "output.txt";

    private BufferedReader br = null;
    private FileWriter fw = null;

    private int[] array = new int[3];

    /**
     * Constructor takes no arguments, and sets up file reading and writing.  The main
     * proponent is the while loop, which says sets the String fileLine to the next line
     * ready to be read from, and while it is not null, set the array (of size 3) to the
     * number corresponding to each line in the file.
     *
     * Line 1: the positive prime number
     * Line 2: the first integer
     * Line 3: the second integer
     *
     * @throws IOException
     */
    public GF1() throws IOException {
        String fileLine = null;
        int arrayCounter = 0;

        try {
            br = new BufferedReader(new FileReader(INPUT_FILE));
            fw = new FileWriter(OUTPUT_FILE);
            while ((fileLine = br.readLine()) != null) {
                // takes the string number from input line and parses it to an integer
                // array[0] = prime, array[1] = int a, array[2] = int b
                array[arrayCounter++] = Integer.parseInt(fileLine);
            }

            // this helps to make it easier to understand the responsibilites of each number in the array
            int prime = array[0];
            int a = array[1];
            int b = array[2];

            // calculates necessary operations formatted and ready to be printed to a file
            String output = calculate(prime, a, b);
            printToFile(output);

            // Sort of a debug print to the console to make sure the file was found and ready successfully
            System.out.print("Successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        fw.close();
    }

    public static void main(String args[]) throws IOException {
        GF1 gf1 = new GF1();
    }

    /**
     * Returns the sum result of two integers in the finite set (0, ..., p - 1) where p
     * is a positive prime number.  It performs normal addition between two numbers, a and b,
     * and if result is between 0 and p - 1, then it is the result; otherwise perform mod p
     * on the result to bring the result back to the set.
     *
     * @param prime a prime number
     * @param a an integer
     * @param b an integer
     * @return  (a + b) || (a + b) mod prime
     */
    private int finiteAddition(int prime, int a, int b) {
        int result;

        if (a + b <= prime - 1 && a + b >= 0) {
            result = a + b;
        } else {
            result = (a + b) % prime;
        }

        return result;
    }

    /**
     * Returns the difference of two integers in the finite set (0, ..., p - 1) where p
     * is a positive prime number.  It performs normal subtraction between two numbers, a and b,
     * and if the result is between 0 and p - 1, then it is the result; otherwise perform
     * mod p on the result and adds the prime number to bring the result back to the set.
     *
     * @param prime a prime number
     * @param a an integer
     * @param b an integer
     * @return  (a - b) || prime + ((a - b) mod prime
     */
    private int finiteSubtraction(int prime, int a, int b) {
        int result;

        if (a - b <= prime - 1 && a - b >= 0) {
            result = a - b;
        } else {
            result = prime + ((a - b) % prime);
        }

        return result;
    }

    /**
     * Returns the product of two integers in the finite set (0, ..., p - 1) where p
     * is a positive prime number.  It performs normal multiplication between two numbers,
     * a and b, and if the result is between 0 and p - 1, then it is the result; otherwise
     * perform mod p on the result to bring the result back to the set.
     *
     * @param prime a prime number
     * @param a an integer
     * @param b an integer
     * @return  (a * b) || (a * b) mod prime
     */
    private int finiteMultiplication(int prime, int a, int b) {
        int result;

        if (a * b <= prime - 1 && a * b >= 0) {
            result = a * b;
        } else {
            result = ((a * b) % prime);
        }

        return result;
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
     * Prints the string to a file.
     *
     * @param s a string
     */
    private void printToFile(String s) {
        try {
            fw.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the String of the calculation of a prime number, and two integers.  This is
     * a conglomerate of all the necessary operations.
     *
     * @param prime
     * @param a
     * @param b
     * @return  output - the formatted version ready to be written to a file
     */
    private String calculate(int prime, int a, int b) {
        String output = "";

        output += Integer.toString(finiteAddition(prime, a, b))       + "\n";
        output += Integer.toString(finiteSubtraction(prime, a, b))    + "\n";
        output += Integer.toString(finiteMultiplication(prime, a, b)) + "\n";
        output += Integer.toString(finiteDivision(prime, a, b))       + "\n";

        return output;
    }
}
