import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import org.junit.Test;

import java.util.*;

public class TestHoffmanCoding {

    @Test
    public void timeInfiniteJest(){
        String fileLocation = "C:\\Users\\LukeJ\\OneDrive\\Documents\\School\\North Seattle\\2019\\Fall 2019\\CSC 143\\Assignments\\Assignment 6\\tests\\InfiniteJest.txt";
        File inputFile = new File(fileLocation);
        double startTime = System.nanoTime();
        new HoffmanCoding(inputFile);
        double endTime = System.nanoTime();
        double timeElapsed = endTime - startTime;
        System.out.println("Time Elapsed in milliseconds: " + (timeElapsed/1000000));
    }

    @Test
    public void testHelloWorld() {
        String fileLocation = "C:\\Users\\LukeJ\\OneDrive\\Documents\\School\\North Seattle\\2019\\Fall 2019\\CSC 143\\Assignments\\Assignment 6\\tests\\test.txt";
        File inputFile = new File(fileLocation);
        HoffmanCoding test = new HoffmanCoding(inputFile);
        String finalFile = getFileString(inputFile);
        String decodedMessage = test.getDecodedString();
        assertEquals(finalFile, decodedMessage);
    }

    @Test
    public void testHolesBook() {
        String fileLocation = "C:\\Users\\LukeJ\\OneDrive\\Documents\\School\\North Seattle\\2019\\Fall 2019\\CSC 143\\Assignments\\Assignment 6\\tests\\holes.txt";
        File inputFile = new File(fileLocation);
        HoffmanCoding test = new HoffmanCoding(inputFile);
        String finalFile = getFileString(inputFile);
        String decodedMessage = test.getDecodedString();
        assertEquals(finalFile, decodedMessage);
    }

    @Test
    public void testToString(){
        String fileLocation = "C:\\Users\\LukeJ\\OneDrive\\Documents\\School\\North Seattle\\2019\\Fall 2019\\CSC 143\\Assignments\\Assignment 6\\tests\\test.txt";
        File inputFile = new File(fileLocation);
        HoffmanCoding test = new HoffmanCoding(inputFile);
        String outString = test.toString();
        System.out.println(outString);
    }

    @Test
    public void testNumBitsAvg(){
        String fileLocation = "C:\\Users\\LukeJ\\OneDrive\\Documents\\School\\North Seattle\\2019\\Fall 2019\\CSC 143\\Assignments\\Assignment 6\\tests\\holes.txt";
        File inputFile = new File(fileLocation);
        HoffmanCoding test = new HoffmanCoding(inputFile);
        assertEquals(4.64817, test.averageNumBits(), 0.00001);
    }

    private String getFileString(File inputFile) {
        Scanner in;
        StringBuffer fileString = new StringBuffer();
        try {
            in = new Scanner(inputFile);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // line = cleanTextContent(line);
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
            fail("File not found you IDIOT!");
        }
        return fileString.toString();
    }
}