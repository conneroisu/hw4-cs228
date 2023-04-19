package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A binary tree that stores characters in the leaves.The binary tree is built
 * from an encoded string.The purpose of this class is to reconstruct/unzip a
 * message archived/encoded with a binary-tree-based algorithm with an `.arch`
 * extension. The class includes 7 fields: staticCharacterIndex, encodeString,
 * encodingString, inputFileName, left, right, and payloadCharacter.
 *
 * @author Conner Ohnesorge
 * @summary a binary tree structure used to decode messages from encoded
 *         strings.The publicly scoped class includes constructors and methods
 *         to build the tree, print character codes, decode messages, and
 *         display statistics of the encoding.
 */
public class MsgTree {

    /**
     * A privately scoped static integer used to keep track the index of the encoded
     * string.
     */
    private static int staticCharacterIndex = 0;
    /**
     * A privately scoped static string used to store the encoded string in a
     * private scope.
     */
    private static String encodeString;
    /**
     * A privately scoped static string used to store the encoding string in a
     * private scope.
     */
    private static String encodingString;
    /**
     * The privately scoped static string used to store the file name of the
     * inputted file from the user.
     */
    private static String inputFileName;
    /**
     * A publicly scoped character representing the payload character for this
     * MsgTree object.
     */
    public char payloadCharacter;

    /**
     * The publicly scoped left MsgTree reference variable in a public scope.
     */
    public MsgTree left;
    /**
     * The publicly scoped right MsgTree reference variable.
     */
    public MsgTree right;

    /**
     * A constructor for a MsgTree object that takes a string as a parameter
     * representing an encoded string.First,initializes the payloadCharacter to the
     * character at the current static Increment the static character index.Also,
     * initializes the left MsgTree reference to a new MsgTree object constructed
     * with the character at the static character index.Next,if the left MsgTree
     * reference is an internal node(i.e. has payload character '^'),sets the left
     * MsgTree to a new MsgTree object constructed with the encoded string at the
     * index of our static array index variable.
     *
     * @param EncodedString the encoded string being used to construct the MsgTree
     *                      object.
     */
    public MsgTree(String EncodedString) {
        // initialize the payloadCharacter to the character at the current static character index.
        this.payloadCharacter = EncodedString.charAt(staticCharacterIndex);

        staticCharacterIndex++;

        // initialize the left MsgTree reference to a new MsgTree object
        // constructed with the character at the static character index.
        this.left = new MsgTree(EncodedString.charAt(staticCharacterIndex));

        // if the left MsgTree reference is an internal node
        // set the left MsgTree reference to a new MsgTree object
        // constructed with the encoded string.
        if (this.left.payloadCharacter == '^') {
            this.left = new MsgTree(EncodedString);
        }
        staticCharacterIndex++;
        // initialize the right MsgTree reference to a new MsgTree object
        // constructed with the character at the static character index.
        this.right = new MsgTree(EncodedString.charAt(staticCharacterIndex));

        // if the right MsgTree reference is an internal node,
        // then set the right MsgTree reference to a new MsgTree object
        // constructed with the encoded string.
        if (this.right.payloadCharacter == '^') {
            this.right = new MsgTree(EncodedString);
        }
    }

    /**
     * A constructor for a single node MsgTree taking a character as a parameter.
     * Sets the payload character to the given character upon construction.
     *
     * @param payloadCharacter character being set as the payload character.
     */
    public MsgTree(char payloadCharacter) {
        this.payloadCharacter = payloadCharacter;
    }

    /**
     * Ask user for file name and then decodes message by first taking the `.arch`
     * file given,creates binary tree and then decodes and prints message.In other
     * words, this program reads an encoded message and its tree structure from a
     * file, constructs the binary tree, decodes the message, and then prints the
     * decoded message.
     *
     * @throws FileNotFoundException if the file does not exist or is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        getFileName();
        buildCodeStrings();
        MsgTree mainTree = new MsgTree(encodeString);

        System.out.println("character\tcode: \n------------------------");
        printCodes(mainTree, "");
        System.out.println("------------------------\nMESSAGE:");
        String decodedMessage = decode(mainTree, encodingString);

        System.out.println(decodedMessage);
        statistics(encodingString, decodedMessage); // print the statistics.
    }

    /**
     * Private method that retrieves an input from the user, ensuring that the file
     * exists, and sets said input to the static variable inputFileName, the class
     * variable for MsgTree that stores the file name. Creates an input scanner,
     * asking the user for input, scans for file name and then sets it as String
     * inputFileName, and, finally, closes the scanner.
     *
     * @throws FileNotFoundException if the file does not exist or is not found
     */
    private static void getFileName() throws FileNotFoundException {
        // Create a scanner object to read from the keyboard(System.in).
        Scanner inputScanner = new Scanner(System.in);
        // Ask the user for the file name.
        System.out.print("Please enter filename to decode: ");
        // Set the input file name to the next line.
        boolean isNotArch = false;
        try {
            inputFileName = inputScanner.nextLine().trim();
            File nf = new File(inputFileName);
            if (!nf.getAbsolutePath().endsWith(".arch")) {
                isNotArch = true;
                throw new FileNotFoundException();
            }
        } catch (Exception e) {
            if (isNotArch) {
                throw new FileNotFoundException(" File '" + inputFileName + "' does not have the .arch extension needed per specifications. ");
            } else {
                throw new FileNotFoundException(" File '" + inputFileName + "' does not exist. ");
            }
        }
        inputScanner.close();
    }


    /**
     * Builds strings from the file input variable and sets them to the static
     * variables encodeString and encodingString.It performs the following
     * operations: creates a file object, initializes a scanner object, reads the
     * first line, reads the second line, creates a string builder object,
     * initializes an empty string, iterates through each character in the second
     * line, appends a new line character and the second line to the string builder
     * object, reads the
     *
     * @throws FileNotFoundException if the file does not exist or is not found.
     */
    private static void buildCodeStrings() throws FileNotFoundException {
        // Create an input file object with the given file name.
        File inputFile = new File(inputFileName);
        // Create a scanner object to read from the input file.
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(" File '" + inputFile + "' does not exist. ");
        }
        encodeString = fileScanner.nextLine();
        String tempStr = fileScanner.nextLine();
        StringBuilder sb = new StringBuilder().append(encodeString);
        encodingString = "";
        for (int x = 0; x < tempStr.length(); x++) {
            if (tempStr.charAt(x) != '1' && tempStr.charAt(x) != '0') {
                sb.append("\n");
                sb.append(tempStr);
                encodingString = fileScanner.nextLine();
                x = tempStr.length();
            } else {
                encodingString = tempStr;
            }
        }
        encodeString = sb.toString();
        fileScanner.close();
    }

    /**
     * Recursive function to print the character codes in a binary tree, taking two
     * parameters: root, which is a node in the binary tree, and codeMessage, which
     * is the accumulated binary code string from traversing the tree. Overall, the
     * function performs the following operations: if the root is null, it means the
     * tree is empty or traversal reached a non-existing node, so the function
     * returns without doing anything; if pl, the payload character, is not '1', the
     * current node is assumed to have a character payload;if pl is not ' 1 , the
     * current node is assumed to have a character payload;if the payload, pl, is a
     * new line character, the function prints the escape sequence "In" followed by
     * three tabs ;otherwise, the function prints the character payload (pl)
     * followed by three tabs. Otherwise, the function prints the character payload
     * (pl) followed by three tabs; after printing the character/escape sequence, it
     * prints the codeMessage containing the binary code for that character:
     * finally, recursively calls itself for the left subtree with a codeMessage
     * appended with '0' and for the right subtree with the codeMessage appended
     * with '1'.
     *
     * @param root        the root of the message tree of type MsgTree.
     * @param codeMessage the code message for the current node of type String.
     */
    static void printCodes(MsgTree root, String codeMessage) {
        if (root == null) {
            return;
        }
        char pl = root.payloadCharacter;
        // if the root is an internal node,
        if (pl != '^') {
            if (pl == '\n') {
                System.out.print("\\" + "n" + "\t\t\t");
            } else {
                System.out.print(root.payloadCharacter + "\t\t\t");
            }
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
     * character, the total characters in the decoded string, and the space savings
     * percentage).Average bits per character are calculated by dividing the length
     * of the encoding string by the length of the decoded string. Total Characters
     * are calculated using the length of the decoded string. Space savings
     * percentage is calculated by subtracting the length of the decoded string from
     * the length of the encoding string and dividing the result by the length of
     * the encoding string.
     *
     * @param encodingString the string representing the encoding.
     * @param decodedString  the string representing the decoded message.
     */
    static private void statistics(String encodingString, String decodedString) {
        System.out.println("STATISTICS:");
        System.out.printf("Avg bits/char:\t%.1f%n", encodingString.length() / (double) decodedString.length());
        System.out.println("Total Characters:\t" + decodedString.length());
        System.out.printf("Space Saving:\t%.1f%%%n", (1d - decodedString.length() / (double) encodingString.length()) * 100);
    }
}
