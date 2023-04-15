package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * @author Conner Ohnesorge
 *         <p>
 *         Com S 228 Spring 2023 Homework 4: Archived Message Reconstruction
 *         </p>
 */

/**
 * A message tree is a binary tree that stores characters in the leaves. The
 * internal nodes of the tree store the character '^'. The left subtree of a
 * node with payload '^' corresponds to code 0, and the right subtree
 * corresponds to code 1. The message tree is constructed from a string of
 * characters.
 */
public class MsgTree {

/**
 * Static integer used to keep track of the index of the encoded string
 */
private static int staticCharacterIndex = 0;
/**
 * String used to store the encoded string
 */
private static String encodeString;
/**
 * String used to store the encoding string
 */
private static String encodingString;
/**
 * String used to store the file name
 */
private static String inputFileName;
/**
 * Character used to store the payload character
 */
public char payloadCharacter;

/**
 * The Left MsgTree reference.
 */
public MsgTree left;
/**
 * The Right MsgTree reference.
 */
public MsgTree right;

/**
 * Constructor for a MsgTree object.A MsgTree object is created from a string of
 * characters.
 *
 * @param EncodedStr the given string to be decoded
 */
public MsgTree(String EncodedStr) {
    // initialize the payloadCharacter to the character at the current static character index.
    this.payloadCharacter = EncodedStr.charAt(staticCharacterIndex);
    // increment the static character index.
    staticCharacterIndex++;
    // initialize the left MsgTree reference to a new MsgTree object constructed with the character at the static character index.
    this.left = new MsgTree(EncodedStr.charAt(staticCharacterIndex));
    // if the left MsgTree reference is a leaf node, then set the left MsgTree reference to a new MsgTree object constructed with the encoded string.
    if (this.left.payloadCharacter == '^') {
        this.left = new MsgTree(EncodedStr);
    }
    // increment the static character index.
    staticCharacterIndex++;
    // initialize the right MsgTree reference to a new MsgTree object constructed with the character at the static character index.
    this.right = new MsgTree(EncodedStr.charAt(staticCharacterIndex));
    // if the right MsgTree reference is a leaf node, then set the right MsgTree reference to a new MsgTree object constructed with the encoded string.
    if (this.right.payloadCharacter == '^') {
        this.right = new MsgTree(EncodedStr);
    }
}

/**
 * Constructor for a single node MsgTree
 *
 * @param payloadCharacter char being set as payloadChar.
 */
public MsgTree(char payloadCharacter) {
    this.payloadCharacter = payloadCharacter;
}

/**
 * Ask user for file name and then decodes message by first taking the .arch
 * file given,creates binary tree and then decodes and prints message
 *
 * @throws FileNotFoundException if the file does not exist or is not found
 */
public static void main(String[] args) throws FileNotFoundException {
    getFileName();
    buildCodeStrings();
    MsgTree mainTree = new MsgTree(encodeString);
    System.out.println("Character\tCode: \n------------------------");
    printCodes(mainTree, "");
    System.out.println("------------------------\nMessage:");
    decode(mainTree, encodingString);
}

/**
 * Scans for file name and then sets it as String inputFileName
 *
 * @throws FileNotFoundException if the file does not exist or is not found
 */
private static void getFileName() throws FileNotFoundException {
    Scanner inputScanner = new Scanner(System.in);
    System.out.print("Please enter filename to decode: ");
    inputFileName = inputScanner.nextLine().trim();

    inputScanner.close();
}

/**
 * Builds strings from the file input variable and sets them to the static
 * variables encodeString and encodingString
 *
 * @throws FileNotFoundException if the file does not exist or is not found
 */
private static void buildCodeStrings() throws FileNotFoundException {

    // Create a input file object with the given file name.
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
 * Prints the codes for each character in the message tree.
 *
 * @param root    = root tree
 * @param codeMsg = binary code
 */
public static void printCodes(MsgTree root, String codeMsg) {
    // if the root is null, then return.
    if (root == null) {
        return;
    }

    // if the root is a leaf node, then print the payload character and the code message.
    if (root.payloadCharacter != '^') {
        System.out.print(root.payloadCharacter + "\t\t");
        System.out.println(codeMsg);
    }

    // print the left and right subtree codes.
    printCodes(root.left, codeMsg + "0");
    printCodes(root.right, codeMsg + "1");
}

/**
 * Decodes message using binary tree method
 *
 * @param codes   a binary search tree given character codes.
 * @param message = to be decoded
 */
public static void decode(MsgTree codes, String message) {
    MsgTree thisTree = codes;

    char curChar;
    int curCharIndex = 0;
    int msgLength = message.length();
    int msgLastIndex = message.length() - 1;

    StringBuilder decodedMsg = new StringBuilder();

    while (curCharIndex < msgLength) {
        curChar = message.charAt(curCharIndex);

        if (curChar == '0') {
            if (thisTree.left == null) {
                decodedMsg.append(thisTree.payloadCharacter);
                thisTree = codes;
            } else {
                thisTree = thisTree.left;
                curCharIndex = curCharIndex + 1;
            }
        } else if (curChar == '1') {
            if (thisTree.right == null) {
                decodedMsg.append(thisTree.payloadCharacter);
                thisTree = codes;
            } else {
                thisTree = thisTree.right;
                curCharIndex = curCharIndex + 1;
            }
        }

        if (curCharIndex == msgLastIndex) {
            if (curChar == '0') {
                assert thisTree.left != null;
                decodedMsg.append(thisTree.left.payloadCharacter);
                curCharIndex = curCharIndex + 1;
            }

            if (curChar == '1') {
                assert thisTree.right != null;
                decodedMsg.append(thisTree.right.payloadCharacter);
                curCharIndex = curCharIndex + 1;
            }
        }
    }

    System.out.println(decodedMsg);
}
}

