package Recursion.Huffman;

import java.io.*;
import java.net.URL;
import java.util.*;

/*****************************************************************************
 * Algorithms and Data Structures. LTAT.03.005
 * 2020/2021 autumn
 *
 * Subject:
 *      Huffman's Algorithm
 *
 * Author:
 *      Silver Kolde
 *
 * Task:
 *      Compress text using Huffman's algorithm.
 *
 *****************************************************************************/

public class Huffman {

    // these helpers are for checking purposes later on
    private static List<Node<Character>> sortedNodes;
    private static HashMap<Character, String> cipherForChecking;


// ================================================================================================================== //
//                                      DRIVER CODE                                                                   //
// ================================================================================================================== //
    public static void main(String[] args) {

//        String input = "Put your own text here";
        String input = loeSisseSuurTekstiFail("http://norvig.com/big.txt");
        Result result = packUp(input);
        String fileName = "packed.txt";
        writeToFile(result.cipher, result.packedUpText, fileName);
        int bitCount = result.packedUpText.length(); // 28550654, if used text from "http://norvig.com/big.txt"

        /**
         * This code writes the packed text to file as String, hence it actually does not pack up.
         * Should convert each "1" and "0" to a single bit and then write to file.
         * But it was not mandatory, doing that is not challenging, I'm not going to waste time on that.
         *
         * If using text from "http://norvig.com/big.txt", then cipher size is 2kB.
         * Packed bitCount is 28550654, that's about 3569 kB.
         *
         * Original text size is 6212 kB, so I managed to compress it down to 57.5% of original size.
         */

        // Reading in the data and verifying success

        HashMap<String, Character> cipher = readCipherFromFile(fileName);
        String binary = readBinary(fileName);
        String unpacked = unpack(binary, cipher);

        assert input.equals(unpacked);

        System.out.println("Packing up and unpacking the text was successful!");

        // Checking if the most common characters have the shortest binary codes

        int x = 10;
        char[] a = findXmostPopularSymbols(x); // last is most popular
        List<Integer> binCodeLengths = findXshortestCodes(x); // last is shortest

        // Compare the lists
        boolean everythingOK = true;
        for (int i = 0; i < a.length; i++) {
            int frequentSymbolIndex = cipherForChecking.get(a[i]).length();
            int shortCodeIndex = binCodeLengths.get(i);
            if (shortCodeIndex != frequentSymbolIndex) {
                everythingOK = false;
                break;
            }
        }
        if (everythingOK)
            System.out.println("Most frequent symbols have the shortest codes.");
        else
            System.out.println("Oops! Something went wrong :/");
    }

// ================================================================================================================== //
//                                     PACKING UP                                                                     //
// ================================================================================================================== //

    public static Result packUp(String text){
        // Sorted <symbol, frequency> pairs
        Map<Character, Integer> frequencies = mapSymbolsAndFreqs(text);

        Node<Character> huffman = createHuffmanTree(frequencies);
        HashMap<Character, String> cipher = createCipher(frequencies.keySet(), huffman);

        cipherForChecking = new HashMap<>(cipher); // for verifying results later on
        // Packing up
        StringBuilder packedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            packedText.append(cipher.get(text.charAt(i)));

        return new Result(packedText.toString(), switchKeyAndValue(cipher));
    }

    private static Map<Character, Integer> mapSymbolsAndFreqs(String text) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (frequencies.containsKey(c))
                frequencies.put(c, frequencies.get(c)+1);
            else
                frequencies.put(c, 1);
        }
        return frequencies;
    }

    private static Node<Character> createHuffmanTree(Map<Character, Integer> frequencies) {
        // Create a node out of each character and add to list
        List<Node<Character>> leaves = new ArrayList<>();
        for (Character ch : frequencies.keySet()) {
            leaves.add(new Node<>(ch, frequencies.get(ch)));
        }
        leaves.sort(Node::compareTo);
        sortedNodes = new ArrayList<>(leaves); // for checking purposes later on

        while (leaves.size() > 1) {
            // At each step well merge two smallest trees into one
            Node<Character> newNode = new Node<>();
            newNode.right = leaves.remove(0);
            newNode.left = leaves.remove(0);
            // Add up the frequencies
            newNode.frequency = newNode.right.frequency + newNode.left.frequency;
            leaves.add(newNode);
            // I should've used my own heap instead of list, but I'm too lazy to refactor right now
            leaves.sort(Node::compareTo);
        }
        // The last one put together is mr Huffman
        return leaves.get(0);
    }

// ==================================== Cipher ====================================================================== //

    private static HashMap<Character, String> createCipher(Set<Character> symbols, Node<Character> huffman) {
        HashMap<Character, String> cipher = new HashMap<>();
        for (Character ch : symbols) {
            String code = huffmanGiveMeCode(huffman, ch, "");
            cipher.put(ch, code);
        }
        return cipher;
    }

    private static String huffmanGiveMeCode(Node<Character> root, Character key, String code) {
        if (root == null)
            return "";
        if (root.right == null && root.left == null) {
            return (root.info == key) ? code : "";
        }
        String result = "";
        result += huffmanGiveMeCode(root.right, key, code + "0");
        result += huffmanGiveMeCode(root.left, key, code + "1");
        return result;
    }

    // Makes unpacking easier
    private static HashMap<String, Character> switchKeyAndValue(HashMap<Character, String> cipher) {
        HashMap<String, Character> switched = new HashMap<>();
        for (Character ch : cipher.keySet()) {
            switched.put(cipher.get(ch), ch);
        }
        return switched;
    }

// ================================================================================================================== //
//                                     UNPACKING                                                                      //
// ================================================================================================================== //

    public static String unpack(String code, HashMap<String, Character> cipher){

        StringBuilder current = new StringBuilder();
        StringBuilder unpacked = new StringBuilder();
        // Add bit by bit and check if cypher gives a match for "current"
        for (int i = 0; i < code.length(); i++) {
            current.append(code.charAt(i));
            if (cipher.get(current.toString()) != null) {        // if found a char
                unpacked.append(cipher.get(current.toString())); // append to unpacked
                current = new StringBuilder();                   // clear the current
            }
        }
        return unpacked.toString();
    }

// ================================================================================================================== //
//                                   VERIFYING CODE LENGTHS                                                           //
// ================================================================================================================== //

    /**
     * @param x the length of the list to be returned
     * @return shortest codes
     */
    private static List<Integer> findXshortestCodes(int x) {
        // Code lengths to list
        List<String> binCodes = new ArrayList<>(cipherForChecking.values());
        List<Integer> binCodeLengths = new ArrayList<>();
        for (String code : binCodes) {
            binCodeLengths.add(code.length());
        }
        // Sort and reverse
        Collections.sort(binCodeLengths);
        Collections.reverse(binCodeLengths);
        // Return x shortest
        return  (binCodeLengths.size() <= x) ? binCodeLengths :
                binCodeLengths.subList(binCodeLengths.size()-x, binCodeLengths.size());
    }


    private static char[] findXmostPopularSymbols(int x) {
        // If there are more symbols than x, we'll take the last 10 only.
        List<Node<Character>> list = (sortedNodes.size() <= x) ? sortedNodes :
                sortedNodes.subList(sortedNodes.size()-x, sortedNodes.size());
        // Create a separate list for node values
        char[] returnable = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            returnable[i] = list.get(i).info;
        }
        return returnable;
    }

// ================================================================================================================== //
//                                                                                                                    //
//                                   I/O OPERATIONS                                                                   //
//                                                                                                                    //
// ================================================================================================================== //

    /**
     * @return Binary string (e.g "10001111010")
     */
    private static String readBinary(String fileName) {
        StringBuilder binary = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            int c;
            boolean readingCipher = true;
            while ((c = in.read()) != -1) {
                if ((char) c == '}') {
                    readingCipher = false;
                    continue;
                }
                if (!readingCipher)
                    binary.append((char) c);
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return binary.toString();
    }

    /**
     * @return Cipher from file (N: {0=a, 100=v, 111=n, 1101=m, 1100=g, 1011=e, 1010=p})
     */
    private static HashMap<String, Character> readCipherFromFile(String fileName) {
        String cipherStr = cipherStringFromFile(fileName);
        String[] keyValuePairs = cipherStr.split(", ");
        HashMap<String, Character> cipher = new HashMap<>();
        for (String pair : keyValuePairs) {
            String[] binSymbol = pair.split("=");
            // If symbol is "=", we split manually
            if (binSymbol.length == 1 && pair.charAt(pair.length()-1) == '=') {
                binSymbol = new String[]{pair.substring(0, pair.length()-2), "="};
            }
            cipher.put(binSymbol[0], binSymbol[1].charAt(0));
        }
        return cipher;
    }


    private static String cipherStringFromFile(String fileName) {
        StringBuilder cipherStr = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            int c;
            while ((c = in.read()) != -1) {
                char symbol = (char) c;
                if (symbol == '{') continue;
                if (symbol == '}') break;
                cipherStr.append(symbol);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cipherStr.toString();
    }

    private static void writeToFile(HashMap<String, Character> cipher, String packedText, String fileName) {
        try (OutputStream out = new FileOutputStream(fileName)) {
            out.write(String.valueOf(cipher).getBytes());
            out.write(packedText.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loeSisseSuurTekstiFail(String url)  {
        StringBuilder wholeText = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new URL(url).openConnection().getInputStream())))
        {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                wholeText.append(inputLine);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return wholeText.toString();
    }
}

class Result {
    public final String packedUpText;
    public final HashMap<String, Character> cipher;

    public Result(String packedUpText, HashMap<String, Character> cipher) {
        this.packedUpText = packedUpText;
        this.cipher = cipher;
    }
}

class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
    T info;
    Node<T> left;
    Node<T> right;
    int x; // position for printing
    int frequency;

    Node() { }

    Node(T info, int frequency) {
        this.info = info;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Node<T> o) {
        return Integer.compare(this.frequency, o.frequency);
    }

    @Override
    public String toString() {
        int width = markPositions(this, 1) * 2; // *2, sest reserveerime kaks kohta igale tipule
        int height = findHeight(this);

        StringBuilder result = new StringBuilder("");
        for (int i = 1; i <= height; i++) {
            String row = printRow(this, i, new StringBuilder(" ".repeat(width)));
            result.append(row).append("\n");
        }
        return result.toString();
    }

    private String printRow(Node<T> node, int level, StringBuilder row) {
        if (node == null)
            return row.toString();
        if (level == 1) {
            String inf = (node.info == null) ? "" : String.valueOf(node.info);
            String str = inf + ":" + node.frequency;
            str = (str.length() == 1) ? " "+str : str;
            row.replace(node.x*2, (node.x*2)+2, str);
            return row.toString();
        }
        printRow(node.right, level-1, row);
        printRow(node.left, level-1, row);

        return row.toString();
    }

    private int markPositions(Node<T> node, int position) {
        if (node == null)
            return position-1;
        int left = markPositions(node.right, position);
        node.x = left + 1;
        int right = markPositions(node.left, left + 2);
        return right;
    }

    private int findHeight(Node<T> node) {
        if (node == null)
            return 0;
        int leftHeight = 0, rightHeight = 0;
        leftHeight += 1 + findHeight(node.right);
        rightHeight += 1 + findHeight(node.left);
        return Math.max(leftHeight, rightHeight);
    }
}
