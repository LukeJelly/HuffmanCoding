import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;

/**
 * A simple user interface for my HuffmanTree Project.
 */
public class UserInterface {

    /**
     * When you run this it will ask the user for a file, it will not work if you point
     * it to anything other than a .txt file.  It will then take that file and send 
     * it to the HuffmanCoding class to encode it and decode it.  Then this class
     * will print out all the information about the encoded message and how many bits on average
     * were used. 
     * @param args
     */
    public static void main(String[] args) {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        int returnValue = jfc.showOpenDialog(null);

        File selectedFile;
        if(returnValue == JFileChooser.APPROVE_OPTION){
            selectedFile = jfc.getSelectedFile();
            String original = getFileString(selectedFile);
            HuffmanCoding HC = new HuffmanCoding(selectedFile);
            System.out.println("Original Message:\n" + original);
            System.out.println("Symbol Frequency Table:\n" + HC.MapToString());
            System.out.println("Huffman Tree:");
            System.out.println(HC);
            System.out.println("Encoded Message:\n" + HC.getEncodedString());
            System.out.println("Decoded Message:\n" + HC.getDecodedString());
            System.out.println("Average number of bits per symbol used to encode message: " + HC.averageNumBits());            
        }
        
    }

    private static String getFileString(File inputFile) {
        Scanner in;
        StringBuffer fileString = new StringBuffer();
        try {
            in = new Scanner(inputFile);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Character c;
                for (int i = 0; i < line.length(); i++) {
                    c = line.charAt(i);
                    fileString.append(c);
                }
                if (in.hasNextLine()) {
                    fileString.append("\n");
                }
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
        return fileString.toString().trim();
    }
    
}