package edu.iastate.cs228.hw4;


import org.junit.Test;

import static org.junit.Assert.*;

public class Test_Msg_Tree {

    @Test
    void testSingleCharacterTree() {
        MsgTree tree = new MsgTree('a');
        assertNotNull(tree);
        assertEquals('a', tree.payloadCharacter);
        assertNull(tree.left);
        assertNull(tree.right);
    }

    @Test
    void testDecodeSimple() {
        String encodedString = "^^a^b";
        MsgTree tree = new MsgTree(encodedString);

        String encodingString = "01";
        String result = MsgTree.decode(tree, encodingString);
        assertEquals("ab", result);
    }

    @Test
    void testDecodeComplex() {
        String encodedString = "^^^a^b^cd";
        MsgTree tree = new MsgTree(encodedString);

        String encodingString = "0011010";
        String result = MsgTree.decode(tree, encodingString);
        assertEquals("bacdb", result);
    }
}

