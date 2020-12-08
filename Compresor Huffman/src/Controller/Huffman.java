package Controller;

import Model.HuffmanBinaryTree;
import Model.HuffmanNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author María Camila Caicedo Mesa CC:1037656070
 */
public class Huffman {

    private final File fileIN;
    private File fileOUT;
    private String readText;
    private StringBuilder compressedText;
    private String decompressedText;
    private StringBuilder textOUT;
    private String[] indexesArray;
    private ArrayList<HuffmanNode> nodeList;
    private final HuffmanBinaryTree huffmanTree;

    public Huffman(File fileIN) {
        this.huffmanTree = new HuffmanBinaryTree();
        this.fileIN = fileIN;
        this.readText = "";
        this.compressedText = new StringBuilder();
        this.decompressedText = "";
        this.nodeList = new ArrayList<>();
    }

    public String compress() throws IOException {
        textOUT = new StringBuilder();
        textOUT.append("Mensaje: ").append(createToCompress()).append("\n");
        textOUT.append("\nTexto leído:\n").append(readToCompress()).append("\n");
        nodeList = characterCount(readText);
        huffmanTree.createBinaryTree((ArrayList<HuffmanNode>) nodeList.clone());
        huffmanTree.stablishBinaryCode(huffmanTree.getFirstNode(), "");
        textOUT.append("\nDirecctorio:\n").append(nodeListToString(nodeList)).append("\n");
        compressText();
        textOUT.append("\nTexto codificado:\n").append(compressedText).append("\n");
        textOUT.append("\nTexto en ASCII:\n").append(writeToCompressedFile()).append("\n");
        return textOUT.toString();
    }

    public String decompress() throws IOException {
        textOUT = new StringBuilder();
        textOUT.append("Mensaje: ").append(createToDecompress()).append("\n");
        textOUT.append("\nDirecctorio:\n").append(getDirectory());
        textOUT.append("\nTexto en ASCII leído:\n").append(readText).append("\n");
        decompressText();
        textOUT.append("\nTexto codificado:\n").append(decompressedText).append("\n");
        textOUT.append("\nTexto descomprimido y decodificado:\n").append(writeToDecompressedFile()).append("\n");
        return textOUT.toString();
    }

    public String createToCompress() throws IOException {
        fileOUT = new File(fileIN.getParent() + "\\" + fileIN.getName().split("\\.")[0] + ".arq");
        if (!fileOUT.exists()) {
            if (fileOUT.createNewFile()) {
                return "El archivo para la compresión fue creado exitosamente.";
            }
            return "El archivo para la compresión no pudo ser creado.";
        }
        return "El archivo para la compresión ya existe, se sobreescribirá.";
    }

    public String createToDecompress() throws IOException {
        fileOUT = new File(fileIN.getParent() + "\\" + fileIN.getName().split("\\.")[0] + "_decompres.txt");
        if (!fileOUT.exists()) {
            if (fileOUT.createNewFile()) {
                return "El archivo para la descompresión fue creado exitosamente.";
            }
            return "El archivo para la descompresión no pudo ser creado.";
        }
        return "Archivo para la descompresión ya existe, se sobreescribirá.";
    }

    public void compressText() {
        char[] strArray = readText.toCharArray();
        for (char c : strArray) {
            for (HuffmanNode node : nodeList) {
                if (node.getSimbol().equals(c)) {
                    compressedText.append(node.getCode());
                }
            }
        }
    }

    public void decompressText() {
        StringBuilder result = new StringBuilder();
        char[] strArray = readText.toCharArray();
        for (int i = 0; i < strArray.length - 1; i++) {
            if (i < (strArray.length - 3)) {
                result.append(
                        String.format("%8s", Integer.toBinaryString(strArray[i]))
                                .replaceAll(" ", "0")
                );
            } else if (i == (strArray.length - 3)) {
                result.append(
                        String.format("%" + (int) strArray[strArray.length - 2] + "s", Integer.toBinaryString(strArray[i]))
                                .replaceAll(" ", "0")
                );
            }
        }
        decompressedText = result.toString();
    }

    public String readToCompress() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileIN));
        String linea;
        while ((linea = br.readLine()) != null) {
            readText += readText.equals("") ? linea : "\n" + linea;
        }
        return readText;
    }

    public String[] readToDecompress() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileIN));
        String nlIndexes = "";
        String linea;
        StringBuilder linea2 = new StringBuilder();
        String[] lineaArray;
        boolean flag = false, flag2 = false;
        while ((linea = br.readLine()) != null) {
            if (linea.startsWith("¥") || flag) {
                flag = true;
                linea2.append(linea).append((char) 13);
            } else {
                if (linea2.toString().equals("") && !flag2) {
                    flag2 = true;
                    nlIndexes = linea;
                } else {
                    linea2.append(linea2.toString().equals("") ? linea : "¦" + linea);
                }
            }
        }
        indexesArray = nlIndexes.split(" ");
        lineaArray = linea2.toString().split("¥", 2);
        readText = lineaArray[1];
        linea = lineaArray[0];
        lineaArray = linea.split("¦");
        return lineaArray;
    }

    public String writeToCompressedFile() throws IOException {
        String text = compressedText.toString();
        try (PrintWriter escribe = new PrintWriter(fileOUT)) {
            String[] arrayString = text.split("(?<=\\G.{8})");
            System.out.println(text);
            StringBuilder sb = new StringBuilder();
            Arrays.stream(arrayString).forEach(s -> sb.append((char) Integer.parseInt(s, 2)));
            sb.append((char) (arrayString[arrayString.length - 1].length()));
            String output = sb.toString();
            StringBuilder nlIndexes = new StringBuilder();
            int index = 0;
            while (index < text.length()) {
                if (text.regionMatches(index, "00001010", 0, 8)) {
                    nlIndexes.append(nlIndexes.toString().equals("") ? index : " " + index);
                }
                index += 8;
            }
            escribe.print(nlIndexes + "\n" + nodeListToString(nodeList) + "\n" + (char) 165 + output);
            escribe.close();
            return output;
        }
    }

    public String writeToDecompressedFile() throws IOException {
        try (PrintWriter escribe = new PrintWriter(fileOUT)) {
            StringBuilder output = new StringBuilder();
            String[] directoryArray = readToDecompress();
            char[] decompressedArray;
            for (int i = 0; i < directoryArray.length; i++) {
                if (directoryArray[i].equals("NL")) {
                    directoryArray[i] = "\n";
                }
            }
            if (!indexesArray[0].equals("")) {
                decompressedArray = decompressedText.toCharArray();
                for (String indexesArray1 : indexesArray) {
                    decompressedArray[Integer.parseInt(indexesArray1) + 5] = '0';
                    decompressedArray[Integer.parseInt(indexesArray1) + 6] = '1';
                    decompressedArray[Integer.parseInt(indexesArray1) + 7] = '0';
                }
                StringBuilder sb = new StringBuilder();
                for (char c : decompressedArray) {
                    sb.append(c);
                }
                decompressedText = sb.toString();
            }
            int index = 0;
            while (index < decompressedText.length()) {
                for (int i = 1; i < directoryArray.length; i += 2) {
                    if (decompressedText.regionMatches(index, directoryArray[i], 0, directoryArray[i].length())) {
                        index += directoryArray[i].length();
                        output.append(directoryArray[i - 1]);
                    }
                }
            }
            escribe.print(output);
            escribe.close();
            return output.toString();
        }
    }

    public ArrayList<HuffmanNode> characterCount(String inputString) {
        ArrayList<HuffmanNode> localNodeList = new ArrayList<>();
        HashMap<Character, Integer> charCountMap = new HashMap<>();
        char[] strArray = inputString.toCharArray();
        for (char c : strArray) {
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            } else {
                charCountMap.put(c, 1);
            }
        }
        charCountMap.forEach((key, value) -> localNodeList.add(new HuffmanNode(null, null, key, value)));
        localNodeList.sort(new SortByCount());
        return localNodeList;
    }

    public String getDirectory() throws IOException {
        String[] lineaArray = readToDecompress();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < lineaArray.length - 1; i += 2) {
            text.append(lineaArray[i]).append("¦").append(lineaArray[i + 1]).append("\n");
        }
        return text.toString();
    }

    public String nodeListToString(ArrayList<HuffmanNode> nodeList) {
        StringBuilder text = new StringBuilder();
        for (HuffmanNode x : nodeList) {
            if (x.getSimbol().equals((char) 10)) {
                text.append(text.toString().equals("") ? "NL" + "¦" + x.getCode() : "\n" + "NL" + "¦" + x.getCode());
            } else {
                text.append(text.toString().equals("") ? x.getSimbol() + "¦" + x.getCode() : "\n" + x.getSimbol() + "¦" + x.getCode());
            }
        }
        return text.toString();
    }

}
