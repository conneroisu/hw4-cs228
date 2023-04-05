package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Kenneth Schueman
 * <p>
 * Com S 228 Fall 2022
 * Project 4: Archived Message Reconstruction
 */

public class MsgTree {

public char payloadChar;
public MsgTree left;
public MsgTree right;

private static int staticCharIdx = 0;

private static String encodeStr;
private static String encodingStr;
private static String inputFileName;

/**
 * Takes .arch file, creates binary tree and then decodes and prints message
 *
 * @throws FileNotFoundException    if the file does not exist or is not found
 */
public static void main(String[] args) throws FileNotFoundException
{
    getFileName();

    buildCodeStrings();

    MsgTree mainTree = new MsgTree(encodeStr);

    System.out.println("Character\tCode: \n------------------------");

    printCodes(mainTree, "");

    System.out.println("------------------------\nMessage:");

    decode(mainTree, encodingStr);
}

/**
 * Scans for file name and then sets it as String inputFileName
 *
 * @throws FileNotFoundException if the file does not exist or is not found
 */
private static void getFileName() throws FileNotFoundException
{
    Scanner inputScanner = new Scanner(System.in);

    System.out.print("Please enter filename to decode: ");
    inputFileName = inputScanner.nextLine().trim();

    inputScanner.close();
}

/**
 * Builds strings from file input
 *
 * @throws FileNotFoundException if the file does not exist or is not found
 */
private static void buildCodeStrings() throws FileNotFoundException {

    File inputFile = new File(inputFileName);

    Scanner fileScanner;

    try
    {
        fileScanner = new Scanner(inputFile);
    }

    catch (FileNotFoundException e)
    {
        throw new FileNotFoundException(" File '" + inputFile + "' does not exist. ");
    }

    encodeStr = fileScanner.nextLine();
    String tempStr = fileScanner.nextLine();
    encodingStr = "";

    for (int x = 0; x < tempStr.length(); x++) {
        if (tempStr.charAt(x) != '1' && tempStr.charAt(x) != '0') {
            encodeStr += "\n";
            encodeStr += tempStr;
            encodingStr = fileScanner.nextLine();
            x = tempStr.length();
        } else {
            encodingStr = tempStr;
        }
    }
    fileScanner.close();
}

/**
 * Constructs a tree
 *
 * @param EncodedStr string to be decoded
 */
public MsgTree(String EncodedStr)
{
    this.payloadChar = EncodedStr.charAt(staticCharIdx);

    staticCharIdx++;

    this.left = new MsgTree(EncodedStr.charAt(staticCharIdx));

    if (this.left.payloadChar == '^')
        this.left = new MsgTree(EncodedStr);

    staticCharIdx++;

    this.right = new MsgTree(EncodedStr.charAt(staticCharIdx));

    if (this.right.payloadChar == '^')
        this.right = new MsgTree(EncodedStr);
}

/**
 * Constructor for a single node MsgTree
 *
 * @param payloadChar char being set as payloadChar.
 */
public MsgTree(char payloadChar)
{
    this.payloadChar = payloadChar;

}

/**
 * Prints values from binary tree and their binary codes
 *
 * @param root    = root tree
 * @param codeMsg = binary code
 */
public static void printCodes(MsgTree root, String codeMsg)
{
    if (root == null)
        return;

    if (root.payloadChar != '^')
    {
        System.out.print(root.payloadChar + "\t\t");
        System.out.println(codeMsg);
    }

    printCodes(root.left, codeMsg + "0");
    printCodes(root.right, codeMsg + "1");
}

/**
 * Decodes message using binary tree method
 *
 * @param codes a binary search tree given character codes.
 * @param message   = to be decoded
 */
public static void decode(MsgTree codes, String message)
{
    MsgTree thisTree = codes;

    char curChar;
    int curCharIndex = 0;
    int msgLength = message.length();
    int msgLastIndex = message.length() - 1;

    String decodedMsg = "";

    while (curCharIndex < msgLength)
    {
        curChar = message.charAt(curCharIndex);

        if (curChar == '0')
        {
            if (thisTree.left == null)
            {
                decodedMsg += thisTree.payloadChar;
                thisTree = codes;
            }

            else
            {
                thisTree = thisTree.left;
                curCharIndex = curCharIndex + 1;
            }
        }

        else if (curChar == '1')
        {
            if (thisTree.right == null)
            {
                decodedMsg += thisTree.payloadChar;
                thisTree = codes;
            }

            else
            {
                thisTree = thisTree.right;
                curCharIndex = curCharIndex + 1;
            }
        }

        if (curCharIndex == msgLastIndex)
        {
            if (curChar == '0')
            {
                decodedMsg += thisTree.left.payloadChar;
                curCharIndex = curCharIndex + 1;
            }

            if (curChar == '1')
            {
                decodedMsg += thisTree.right.payloadChar;
                curCharIndex = curCharIndex + 1;
            }
        }
    }

    System.out.println(decodedMsg);
}
}

