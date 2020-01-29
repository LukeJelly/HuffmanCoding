import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Proof of concept for a HoffmanCoding tree. Works with any .txt file.
 * 
 * @author Luke.Kelly
 * @version 12/2/2019
 */
public class HoffmanCoding {
    private HoffmanTreeNode rootOfHoffmanTree;
    private HashMap<Character, HoffmanTreeNode> nodeHashMap;
    private String encodedMessage;
    private String decodedMessage;

    /**
     * Creates the hoffman tree then encodes and decodes the message
     * 
     * @param inFile the file you wish to encode.
     * @throws IllegalArgumentException if the file does not exist.
     * @throws IllegalArgumentException if the file is empty.
     */
    public HoffmanCoding(File inFile) {
        // Check empty file
        long EMPTY_FILE = 0L;
        if (inFile.length() == EMPTY_FILE) {
            throw new IllegalArgumentException("Can not encode an empty file");
        }

        // Create a HashMap that holds all the HoffmanNodes in the HoffmanTree
        buildNodeHashMap(inFile);
        // Use the HashMap to create a Priority Que that I then can use to make the tree
        PriorityQueue<HoffmanTreeNode> allNodes = new PriorityQueue<>(nodeHashMap.values());
        // Build the HoffmanTree
        buildHoffmanTree(allNodes);
        // Assign a code to all the nodes based on their position in the tree.
        assignCode(this.rootOfHoffmanTree, "");

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
        HoffmanTreeNode p = this.rootOfHoffmanTree;
        int size = msg.length();
        int i = 0;
        // Check if only one node in the tree.
        if (this.rootOfHoffmanTree.left == null && this.rootOfHoffmanTree.right == null) {
            for (int j = 0; j < size; j++) {
                output.append(this.rootOfHoffmanTree.letter);
            }
        } else {
            for (; i < size; i++) {
                if (p.left == null && p.right == null) {
                    output.append(p.letter);
                    p = this.rootOfHoffmanTree;
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
     * Encodes the message
     * 
     * @param in a scanner pointing to the file that holds the message.
     * @return a String representing the encoded message.
     */
    private void encodeMessage(File inFile) {
        StringBuffer output = new StringBuffer();
        try (Scanner in = createScanner(inFile)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Character c;
                for (int i = 0; i < line.length(); i++) {
                    c = line.charAt(i);
                    String encodedChar = nodeHashMap.get(c).encodingValue;
                    output.append(encodedChar);
                }
                if (in.hasNextLine()) {
                    String encodedChar = nodeHashMap.get('\n').encodingValue;
                    output.append(encodedChar);
                }
            }
        }
        this.encodedMessage = output.toString();
    }

    /**
     * Creates a scanner object that points to the file you are encoding and
     * decoding.
     * 
     * @param f the File's location
     * @return a Scanner pointing to that file.
     */
    private Scanner createScanner(File f) {
        Scanner in;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File could not be found, check file location.");
        }
        return in;
    }

    /**
     * Goes through the hashmap and assigns encodingValues to all leaf nodes.
     * 
     * @param p             A pointer that points to the node being evaluated.
     * @param encodingValue the String to be added as an encodingValue
     */
    private void assignCode(HoffmanTreeNode p, String encodingValue) {
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
     * Builds the hoffmanTree
     * 
     * @param nodeProQueue The priority que that holds the nodes
     * @return an HNode pointing to the root of tree
     */
    private void buildHoffmanTree(PriorityQueue<HoffmanTreeNode> nodeProQueue) {
        while (nodeProQueue.size() > 1) {
            nodeProQueue.add(new HoffmanTreeNode(nodeProQueue.poll(), nodeProQueue.poll()));
        }
        this.rootOfHoffmanTree = nodeProQueue.poll();
    }

    /**
     * Builds a HasMap that holds all the HoffmanNodes, this method closes the
     * scanner.
     * 
     * @param in a Scanner pointing to the file that holds your data.
     * @return a Hashmap that holds all the Characters in that file, and then an
     *         HNode that represents them.
     */
    private void buildNodeHashMap(File inFile) {
        HashMap<Character, HoffmanTreeNode> HMHNode = new HashMap<>();
        byte DEFAULT_FIRST_FREQUENCY = 1;
        try (Scanner in = createScanner(inFile)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                line += "\n";
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    HoffmanTreeNode tempHNode = HMHNode.get(c);
                    if (tempHNode == null) {
                        HMHNode.put(c, new HoffmanTreeNode(c, DEFAULT_FIRST_FREQUENCY));
                    } else {
                        tempHNode.frequency++;
                    }
                }
            }
        }
        this.nodeHashMap = HMHNode;
    }

    /**
     * Simple to string method to print out the HoffmanTree
     * 
     * @return a String representation of the HoffmanTree.
     */
    public String toString() {
        StringBuffer output = new StringBuffer();
        printSideways(this.rootOfHoffmanTree, "", output);
        return output.toString();
    }

    /**
     * Recursive helper method for toString()
     * 
     * @param root   the node we are printing.
     * @param indent the amount of indent
     * @param output the stringBuffer we are using to create the string.
     */
    private void printSideways(HoffmanTreeNode root, String indent, StringBuffer output) {
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
        PriorityQueue<HoffmanTreeNode> PQ = new PriorityQueue<>();
        for (HoffmanTreeNode HN : nodeHashMap.values()) {
            PQ.add(HN);
        }
        StringBuffer output = new StringBuffer();

        while (!PQ.isEmpty()) {
            HoffmanTreeNode HN = PQ.poll();
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
     * HoffmanTree.
     */
    class HoffmanTreeNode implements Comparable<HoffmanTreeNode> {
        Character letter; // The letter this node represents
        int frequency; // The frequency of this letter
        String encodingValue; // The encoding value of this letter
        HoffmanTreeNode left; // This nodes left child
        HoffmanTreeNode right; // This nodes right child

        // If a node is not a leaf it should not have a letter.

        /**
         * A simple constructor for HNodes that creates one based on frequency and
         * letter
         * 
         * @param letter    the letter this node represents
         * @param frequency It's frequency.
         */
        public HoffmanTreeNode(Character letter, int frequency) {
            this.letter = letter;
            this.frequency = frequency;
        }

        /**
         * Creates a new HNode that is a parent to the given HNodes
         * 
         * @param left  the node you wish to be put on the left
         * @param right the node you wish to be put on the right.
         */
        public HoffmanTreeNode(HoffmanTreeNode left, HoffmanTreeNode right) {
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        /** {@inheritDoc} */
        public int compareTo(HoffmanTreeNode o) {
            HoffmanTreeNode foreign = (HoffmanTreeNode) o;
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
            if (obj instanceof HoffmanTreeNode) {
                HoffmanTreeNode foreign = (HoffmanTreeNode) obj;
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