package Model;

import Controller.SortByCount;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author María Camila Caicedo Mesa CC:1037656070
 */
public class HuffmanBinaryTree {

    private HuffmanNode firstNode;

    public HuffmanBinaryTree() {
        this.firstNode = null;
    }

    public HuffmanNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(HuffmanNode firstNode) {
        this.firstNode = firstNode;
    }

    public void createBinaryTree(ArrayList<HuffmanNode> nodeList) {
        HuffmanNode x;
        while (nodeList.size() > 1) {
            x = new HuffmanNode(nodeList.get(0), nodeList.get(1), null,
                    nodeList.get(0).getCount() + nodeList.get(1).getCount());
            nodeList.remove(1);
            nodeList.remove(0);
            nodeList.add(x);
            Collections.sort(nodeList, new SortByCount());
        }
        firstNode = nodeList.get(0);
    }

    public void stablishBinaryCode(HuffmanNode node, String prefix) {
        if (node != null) {
            if (node.getSimbol() != null) {
                if (!prefix.equals("")) {
                    node.setCode(prefix);
                } else {
                    node.setCode("1");
                }
            }
            stablishBinaryCode(node.getLeftChild(), prefix + "0");
            stablishBinaryCode(node.getRightChild(), prefix + "1");
        }
    }
}
