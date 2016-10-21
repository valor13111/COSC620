package cipher;

import java.util.ArrayList;
import java.util.Random;

/**
 * The General Caesar Algorithm arranges lowercase letters a to z.  Key k is a randomly
 * selected integer from 0 to 25.  When given a plaintext and key, every letter in the
 * plaintext is encrypted to the letter shifted 'k' positions, and will come back to a
 * if needing to shift past z.  In order to decrypt, and only knowing the ciphertext,
 * you can list all of the possible phrases from every key.
 *
 * Vulnerability: since there are only 26 letters in English dictionary, meaning there are
 *                only 26 keys, a brute-force attack could break the ciphertext.
 */
public class generalCaesar {

    private final static int ENGLISH_ALPHABET_SIZE = 26;

    private String phrase[] = new String[26];
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Random random = new Random();

    // example ciphertext
    public String ciphertext = "wdl pgt ndj";

    /**
     * Constructor sets the keys in the ArrayList, and encodes the cipertext.
     */
    public generalCaesar() {
        setKeysInArray();
        decode(ciphertext);
    }

    /**
     * Main program which initializes a constructor.
     *
     * @param args
     */
    public static void main(String args[]) {
        generalCaesar gc = new generalCaesar();
    }

    /**
     * Sets up a for loop to each time make a random key from the arraylist,
     * if it is valid, then remove it, and decode the ciphertext with that key.
     * Each new string of characters is added to the phrases String array.
     *
     * @param encodedtext   the ciphertext
     */
    private void decode(String encodedtext) {

        for (int i = 0; i < ENGLISH_ALPHABET_SIZE; i++) {
            int key = randomKey();
            removeKey(key);
            phrase[i] = phrase(encodedtext, key);
        }

        print(phrase);
    }

    /**
     * Returns a new phrase from the ciphertext and given key.
     *
     * @param encodedtext   the ciphertext
     * @param randomKey     the randomly generated key
     * @return              decoded String from the ciphertext
     */
    private String phrase(String encodedtext, int randomKey) {
        StringBuilder sb = new StringBuilder(encodedtext);

        for (int i = 0; i < encodedtext.length(); i++) {
            if (encodedtext.charAt(i) != ' ') {
                sb.setCharAt(i, attack(encodedtext.charAt(i), randomKey));
            }
        }

        return sb.toString();
    }

    /**
     * Returns a pseudo-random number from the arraylist, if it contains the number.  Each key is
     * generated from lowest value in the arraylist, to the highest value, not index.
     *
     * @return  random key (integer)
     */
    private int randomKey() {
        while (true) {
            int r = random.nextInt(arrayList.get(arrayList.size() - 1)) + arrayList.get(0);

            if (containKey(r)) {
                return r;
            }
        }
    }

    /**
     * Returns if the arraylist contains the given key.
     *
     * @param key
     * @return  true or false
     */
    private boolean containKey(int key) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.contains(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes the value (key) from the arraylist.
     *
     * @param key
     */
    private void removeKey(int key) {
        Object k = key;
        arrayList.remove(k);
    }

    /**
     * Sets the arraylist to contain number 1 through 26.
     */
    private void setKeysInArray() {
        for (int i = 0; i < ENGLISH_ALPHABET_SIZE; i++) {
            arrayList.add(i + 1);
        }
    }

    /**
     * Returns the new character given a starting character and key.  The idea
     * is to subtract the ascii number for a character by the key.  If that number
     * drops below 97, which is the ascii number for lowercase a, then it goes back
     * around to z, and continues to subtract.
     *
     * An example of the logic here: let c = 'w' = '119'
     *                                   key = 25
     *
     * 119 is less than 122 ( 97 + 25)
     *  if we did 119 - 25, this would be 94, 3 ascii characters less than 'a'
     *  so, I take the difference of 97 - (119 - 25) => 97 - 94 = 3
     *
     *  now, return a character with 123 - the difference.
     *  if here, I did 122, which is ascii for 'z', then 122 - 3 would be 119, which is 'w' again,
     *      which is wrong.
     *  so, I did 123 to make up for this
     *
     * @param c     character to attack
     * @param key   generated key
     * @return      newly decoded character
     */
    private char attack(char c, int key) {
        char ch;

        while (true) {
            if (c < (97 + key)) {
                int difference = 97 - (c - key);

                return ch = (char)(123 - difference);
            }

            return ch = (char)(c - key);
        }
    }

    /**
     * Prints the strings given from a String array.
     *
     * @param s     String array
     */
    private void print(String s[]) {
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
    }
}
