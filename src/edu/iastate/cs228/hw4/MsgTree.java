package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A binary tree that stores characters in the leaves.The binary tree is built
 * from an encoded string.The purpose of this class is to reconstruct/unzip a
 * message archived/encoded with a binary-tree-based algorithm with an
 * `.arch` extension.
 *
 * @author Conner Ohnesorge
 */
public class MsgTree {

    /**
     * A static integer used to keep track the index of the encoded string in a
     * private scope.
     */
    private static int staticCharacterIndex = 0;
    /**
     * A static string used to store the encoded string in a private scope.
     */
    private static String encodeString;
    /**
     * A static string used to store the encoding string in a private scope.
     */
    private static String encodingString;
    /**
     * The static string used to store the file name of the inputted file from the
     * user in a private scope.
     */
    private static String inputFileName;
    /**
     * A public character used to store the payload character for this MsgTree
     * object.
     */
    public char payloadCharacter;

    /**
     * The Left MsgTree reference variable in a public scope.
     */
    public MsgTree left;
    /**
     * The Right MsgTree reference variable in a public scope.
     */
    public MsgTree right;

    /**
     * A constructor for a MsgTree object that takes a string representing the
     * encoded string as a parameter.
     *
     * @param EncodedString the given string to be decoded.
     */
    public MsgTree(String EncodedString) {
        // initialize the payloadCharacter to the character at the current static character index.
        this.payloadCharacter = EncodedString.charAt(staticCharacterIndex);

        staticCharacterIndex++;

        // initialize the left MsgTree reference to a new MsgTree object
        // constructed with the character at the static character index.
        this.left = new MsgTree(EncodedString.charAt(staticCharacterIndex));

        // if the left MsgTree reference is an internal node,then set the left MsgTree reference to a new MsgTree object
        // constructed with the encoded string.
        if (this.left.payloadCharacter == '^') {
            this.left = new MsgTree(EncodedString);
        }
        staticCharacterIndex++;
        // initialize the right MsgTree reference to a new MsgTree object constructed with the character at the static character index.
        this.right = new MsgTree(EncodedString.charAt(staticCharacterIndex));

        // if the right MsgTree reference is a leaf node, then set the right MsgTree reference to a new MsgTree object constructed with the encoded string.
        if (this.right.payloadCharacter == '^') {
            this.right = new MsgTree(EncodedString);
        }
    }

    /**
     * A constructor for a single node MsgTree taking a character as a parameter.
     *
     * @param payloadCharacter character being set as the payload character.
     */
    public MsgTree(char payloadCharacter) {
        this.payloadCharacter = payloadCharacter;
    }

    /**
     * Ask user for file name and then decodes message by first taking the `.arch`
     * file given,creates binary tree and then decodes and prints message.In
     * summary/other words, this program reads an encoded message and its tree
     * structure from a file, constructs the binary tree, decodes the message, and
     * then prints the decoded message.
     *
     * @throws FileNotFoundException if the file does not exist or is not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        getFileName(); // get the file name from the user.
        buildCodeStrings(); // build the encoded string and the encoding string.
        MsgTree mainTree = new MsgTree(encodeString); // create a new MsgTree object with the encoded string.
        System.out.println("Character\tCode: \n------------------------"); // print the header.
        printCodes(mainTree, ""); // print the codes for each character in the message tree.
        System.out.println("------------------------\nMessage:"); // print the header.
        String decodedMessage = decode(mainTree, encodingString); // decode the message.
        // Finally, print the decoded message.
        System.out.println(decodedMessage);
        statistics(encodingString, decodedMessage); // print the statistics.
    }

    /**
     * Creates an input scanner, asking the user for input, scans for file name and
     * then sets it as String inputFileName, and, finally, closes the scanner.
     *
     * @throws FileNotFoundException if the file does not exist or is not found
     */
    private static void getFileName() throws FileNotFoundException {
        // Create a scanner object to read from the keyboard(System.in).
        Scanner inputScanner = new Scanner(System.in);
        // Ask the user for the file name.
        System.out.print("Please enter filename to decode: ");
        // Set the input file name to the next line.
        try {
            inputFileName = inputScanner.nextLine().trim();
            new File(inputFileName);
        } catch (Exception e) {
            throw new FileNotFoundException(" File '" + inputFileName + "' does not exist. ");
        }
        inputScanner.close();
    }

    /**
     * Builds strings from the file input variable and sets them to the static
     * variables encodeString and encodingString.
     *
     * @throws FileNotFoundException if the file does not exist or is not found.
     */
    private static void buildCodeStrings() throws FileNotFoundException {
        // Create an input file object with the given file name.
        File inputFile = new File(inputFileName);
        // Create a scanner object to read from the input file.
        Scanner fileScanner;
        // Try to open the file. If the file does not exist, throw an exception.
        try {
            fileScanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(" File '" + inputFile + "' does not exist. ");
        }
        encodeString = fileScanner.nextLine();
        String tempStr = fileScanner.nextLine();
        encodingString = "";
        // Loop through the temporary string, checking for invalid characters.
        for (int x = 0; x < tempStr.length(); x++) {
            if (tempStr.charAt(x) != '1' && tempStr.charAt(x) != '0') {
                encodeString += "\n";
                encodeString += tempStr;
                encodingString = fileScanner.nextLine();
                x = tempStr.length();
            } else {
                encodingString = tempStr;
            }
        }
        fileScanner.close();
    }

    /**
     * Recursive method that Prints the codes for each character in the message
     * tree.
     *
     * @param root        the root of the message tree.
     * @param codeMessage the code message for the current node.
     */
    static void printCodes(MsgTree root, String codeMessage) {
        if (root == null) {
            return;
        }
        // if the root is an internal node, print the payload character and the code message.
        if (root.payloadCharacter != '^') {
            System.out.print(root.payloadCharacter + "\t\t");
            System.out.println(codeMessage);
        }
        // print the left and right subtree codes.
        printCodes(root.left, codeMessage + "0");
        printCodes(root.right, codeMessage + "1");
    }

    /**
     * Decodes a binary message from a tree structure called MsgTree.It traverses
     * the tree according to each character (0 or 1)in the input message and
     * constructs a decoded message.Generally it does the following: starting at
     * root, repeatedly scan one bit going to the left if 0 and right otherwise;
     * finally, print the character at the leaf node.
     *
     * @param codes   a binary search tree for decoding the message.
     * @param message a string of 1's and 0's to be decoded.
     */
    public static String decode(MsgTree codes, String message) {
        // The current tree being decoded.
        MsgTree thisTree = codes;
        // The current character being decoded.
        char currentCharacter;
        // The current character index.
        int currentCharacterIndex = 0;
        // The length of the message.
        int msgLength = message.length();
        // The last index of the message.
        int msgLastIndex = message.length() - 1;
        // The decoded message string builder.
        StringBuilder decodedMessage = new StringBuilder();

        // while the current character index is less than the message length.
        while (currentCharacterIndex < msgLength) {
            currentCharacter = message.charAt(currentCharacterIndex);
            if (currentCharacter == '0') {
                if (thisTree.left == null) {
                    decodedMessage.append(thisTree.payloadCharacter);
                    thisTree = codes;
                } else {
                    thisTree = thisTree.left;
                    currentCharacterIndex = currentCharacterIndex + 1;
                }
            } else if (currentCharacter == '1') {
                if (thisTree.right == null) {
                    decodedMessage.append(thisTree.payloadCharacter);
                    thisTree = codes;
                } else {
                    thisTree = thisTree.right;
                    currentCharacterIndex++;
                }
            }

            // if the current character index is equal to the last index of the message.
            if (currentCharacterIndex == msgLastIndex) {
                if (currentCharacter == '0') {
                    assert thisTree.left != null;
                    decodedMessage.append(thisTree.left.payloadCharacter);
                    currentCharacterIndex++;
                }

                if (currentCharacter == '1') {
                    assert thisTree.right != null;
                    decodedMessage.append(thisTree.right.payloadCharacter);
                    currentCharacterIndex++;
                }
            }
        }
        return decodedMessage.toString();
    }

    /**
     * Calculates and prints the statistics of the encoding(e.g., average bits per
     * character, total characters, and space savings percentage).{EXTRA CREDIT}
     *
     * @param encodingString the string prior to encoding.
     * @param decodedString  the string after decoding.
     */
    static private void statistics(String encodingString, String decodedString) {
        System.out.println("STATISTICS:");
        System.out.printf("Avg bits/char:\t%.1f%n", encodingString.length() / (double) decodedString.length());
        System.out.println("Total Characters:\t" + decodedString.length());
        System.out.printf("Space Saving:\t%.1f%%%n", (1d - decodedString.length() / (double) encodingString.length()) * 100);
    }
}
