import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Proof of concept for a HuffmanCoding tree. Works with any .txt file.
 * 
 * @author Luke.Kelly
 * @version 12/2/2019
 * HoffmanCoding
 */
public class HuffmanCoding {
    private HuffmanTreeNode rootOfHuffmanTree;
    private HashMap<Character, HuffmanTreeNode> nodeHashMap;
    private String encodedMessage;
    private String decodedMessage;

    /**
     * Creates the Huffman tree then encodes and decodes the message
     * 
     * @param inFile the file you wish to encode.
     * @throws IllegalArgumentException if the file does not exist.
     * @throws IllegalArgumentException if the file is empty.
     */
    public HuffmanCoding(File inFile) {
        // Check empty file
        long EMPTY_FILE = 0L;
        if (inFile.length() == EMPTY_FILE) {
            throw new IllegalArgumentException("Can not encode an empty file");
        }

        // Create a HashMap that holds all the HuffmanNodes in the HuffmanTree
        buildNodeHashMap(inFile);
        // Use the HashMap to create a Priority Queue that I then can use to make the tree
        PriorityQueue<HuffmanTreeNode> allNodes = new PriorityQueue<>(nodeHashMap.values());
        // Build the HuffmanTree
        buildHuffmanTree(allNodes);
        // Assign a code to all the nodes based on their position in the tree.
        assignCode(this.rootOfHuffmanTree, "");

        // Encode and Decode the message.
        encodeMessage(inFile);
        decodeMessage(this.encodedMessage);
    }

    /**
     * Returns the encoded message
     * 
     * @return the encoded message
     */
    public String getEncodedString() {
        return this.encodedMessage;
    }

    /**
     * Returns the decoded message
     * 
     * @return the decoded message
     */
    public String getDecodedString() {
        return this.decodedMessage;
    }

    /**
     * Decodes the message.
     * 
     * @param msg the encoded message
     * @return a String representing the decoded message
     */
    private void decodeMessage(String msg) {
        StringBuffer output = new StringBuffer();
        HuffmanTreeNode p = this.rootOfHuffmanTree;
        int size = msg.length();
        int i = 0;
        // Check if only one node in the tree.
        if (this.rootOfHuffmanTree.left == null && this.rootOfHuffmanTree.right == null) {
            for (int j = 0; j < size; j++) {
                output.append(this.rootOfHuffmanTree.letter);
            }
        } else {
            for (; i < size; i++) {
                if (p.left == null && p.right == null) {
                    output.append(p.letter);
                    p = this.rootOfHuffmanTree;
                }
                int LorR = 48 - msg.charAt(i);
                if (LorR == 0) {
                    p = p.left;
                } else {
                    p = p.right;
                }
            }
            if (p.letter != null) {
                output.append(p.letter);
            }
        }
        this.decodedMessage = output.toString();
    }

    /**
     * Encodes the message, assumes that the HuffmanTree has already been build,
     * 
     * @param in a scanner pointing to the file that holds the message.
     * @return a String representing the encoded message.
     */
    private void encodeMessage(File inFile) {
        StringBuffer output = new StringBuffer();
        try (Scanner in = new Scanner(inFile)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                findEncodingValueOfLine(line, output);
                hasNextLine(in, output);
            }
        }catch(FileNotFoundException e){
            throw new IllegalArgumentException("File could not be found, check file location.");
        }
        this.encodedMessage = output.toString();
    }

    /**
     * Finds the encoding value for every char in the given String
     * 
     * @param line the String to loop through
     * @param output the StringBuffer to add all the encoding values to.
     */
    private void findEncodingValueOfLine(String line, StringBuffer output){
        Character c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            String encodedChar = nodeHashMap.get(c).encodingValue;
            output.append(encodedChar);
        }
    }

    /**
     * If the scanner has another line of text we want to add a new line char to
     * the of the the StringBuffer.
     *
     * @param in the Scanner to check
     * @param output the StringBuffer to add to.
     */
    private void hasNextLine(Scanner in, StringBuffer output){
        if (in.hasNextLine()) {
            String encodedChar = nodeHashMap.get('\n').encodingValue;
            output.append(encodedChar);
        }
    }

    /**
     * Goes through the hashmap and assigns encodingValues to all leaf nodes.
     * 
     * @param p             A pointer that points to the node being evaluated.
     * @param encodingValue the String to be added as an encodingValue
     */
    private void assignCode(HuffmanTreeNode p, String encodingValue) {
        if (nodeHashMap.size() == 1) {
            p.encodingValue = "0";
        } else {
            if (p != null) {
                p.encodingValue = encodingValue;
                assignCode(p.left, encodingValue + "0");
                assignCode(p.right, encodingValue + "1");
            }
        }
    }

    /**
     * Builds the HuffmanTree
     * 
     * @param nodeProQueue The priority que that holds the nodes
     * @return an HNode pointing to the root of tree
     */
    private void buildHuffmanTree(PriorityQueue<HuffmanTreeNode> nodeProQueue) {
        while (nodeProQueue.size() > 1) {
            nodeProQueue.add(new HuffmanTreeNode(nodeProQueue.poll(),
                                                 nodeProQueue.poll()));
        }
        this.rootOfHuffmanTree = nodeProQueue.poll();
    }

    /**
     * Builds a HasMap that holds all the HuffmanNodes, this method closes the
     * scanner.
     * 
     * @param in a Scanner pointing to the file that holds your data.
     * @return a Hashmap that holds all the Characters in that file, and then an
     *         HNode that represents them.
     */
    private void buildNodeHashMap(File inFile) {
        HashMap<Character, HuffmanTreeNode> HMHNode = new HashMap<>();
        try (Scanner in = new Scanner(inFile)) {
            while (in.hasNextLine()) {
                String line = in.nextLine() + "\n";
                addToNodeHashMap(line,HMHNode);
            }
        }catch(FileNotFoundException e){
            throw new IllegalArgumentException("File could not be found, check file location.");
        }
        this.nodeHashMap = HMHNode;
    }

    /**
     * Loops through the given String and adds all the chars to the HashMap.
     * @param line The string to loop through
     * @param HMHNode the HashMap to add them to.
     */
    private void addToNodeHashMap(String line, HashMap<Character, HuffmanTreeNode> HMHNode){
        byte DEFAULT_FIRST_FREQUENCY = 1;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            //If tempHNode is null the node doesn't exists.
            HuffmanTreeNode tempHNode = HMHNode.get(c);
            if (tempHNode == null) {
                //So we create one
                HMHNode.put(c, new HuffmanTreeNode(c, DEFAULT_FIRST_FREQUENCY));
            } else {
                //If it does we just increase it's frequency.
                tempHNode.frequency++;
            }
        }
    }

    /**
     * Simple to string method to print out the HuffmanTree
     * 
     * @return a String representation of the HuffmanTree.
     */
    public String toString() {
        StringBuffer output = new StringBuffer();
        printSideways(this.rootOfHuffmanTree, "", output);
        return output.toString();
    }

    /**
     * Recursive helper method for toString()
     * 
     * @param root   the node we are printing.
     * @param indent the amount of indent
     * @param output the stringBuffer we are using to create the string.
     */
    private void printSideways(HuffmanTreeNode root, String indent, StringBuffer output) {
        if (root != null) {
            printSideways(root.right, indent + "    ", output);
            Character myChar = root.letter;
            if (myChar != null) {
                // Had to check if the letter was a new line char because that would just put a
                // new line
                // in the output.
                String letter = root.letter == '\n' ? "\\n" : Character.toString(root.letter);
                output.append(indent + "[" + letter + "]F: " + root.frequency + "\n");
            } else {
                output.append(indent + "[]F: " + root.frequency + "\n");
            }
            printSideways(root.left, indent + "    ", output);
        }
    }

    /**
     * Returns a String representing the map with all the characters and their
     * frequency plus all the encoding values.
     * 
     * @return a String representing the Map.
     */
    public String MapToString() {
        PriorityQueue<HuffmanTreeNode> PQ = new PriorityQueue<>();
        for (HuffmanTreeNode HN : nodeHashMap.values()) {
            PQ.add(HN);
        }
        StringBuffer output = new StringBuffer();

        while (!PQ.isEmpty()) {
            HuffmanTreeNode HN = PQ.poll();
            Character myChar = HN.letter;
            if (myChar == '\n') {
                output.append("Value: " + "\\n" + " Frequency: " + HN.frequency + " Encoding Value: " + HN.encodingValue
                        + "\n");
            } else {
                output.append("Value: " + myChar + " Frequency: " + HN.frequency + " Encoding Value: "
                        + HN.encodingValue + "\n");
            }

        }
        return output.toString();
    }

    /**
     * Calculates the average number of bits I used to encode the message
     * 
     * @return a double representing that.
     */
    public double averageNumBits() {
        return (double) (encodedMessage.length()) / decodedMessage.length();
    }

    /**
     * Internal class that represents one character it is the building blocks of the
     * HuffmanTree.
     */
    class HuffmanTreeNode implements Comparable<HuffmanTreeNode> {
        Character letter; // The letter this node represents
        int frequency; // The frequency of this letter
        String encodingValue; // The encoding value of this letter
        HuffmanTreeNode left; // This nodes left child
        HuffmanTreeNode right; // This nodes right child

        // If a node is not a leaf it should not have a letter.

        /**
         * A simple constructor for HNodes that creates one based on frequency and
         * letter
         * 
         * @param letter    the letter this node represents
         * @param frequency It's frequency.
         */
        public HuffmanTreeNode(Character letter, int frequency) {
            this.letter = letter;
            this.frequency = frequency;
        }

        /**
         * Creates a new HNode that is a parent to the given HNodes
         * 
         * @param left  the node you wish to be put on the left
         * @param right the node you wish to be put on the right.
         */
        public HuffmanTreeNode(HuffmanTreeNode left, HuffmanTreeNode right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        /** {@inheritDoc} */
        public int compareTo(HuffmanTreeNode o) {
            HuffmanTreeNode foreign = (HuffmanTreeNode) o;
            if (foreign.frequency == this.frequency) {
                return 0;
            } else if (foreign.frequency > this.frequency) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        /** {@inheritDoc} */
        public boolean equals(Object obj) {
            if (obj instanceof HuffmanTreeNode) {
                HuffmanTreeNode foreign = (HuffmanTreeNode) obj;
                if (foreign.letter == this.letter) {
                    return true;
                }
            }
            return false;
        }

        @Override
        /**
         * Returns a string representation of the HNode.
         * 
         * @return a string representation of the HNode.
         */
        public String toString() {
            return ("Char: " + letter + " Frequency: " + frequency);
        }
    }
}
