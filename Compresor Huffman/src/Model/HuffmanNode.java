package Model;

/**
 *
 * @author María Camila Caicedo Mesa
 */
public class HuffmanNode {

    private HuffmanNode leftChild, rightChild;
    private Character simbol;
    private String code;
    private int count;

    public HuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild, Character simbol, int count) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.simbol = simbol;
        this.count = count;
        this.code = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HuffmanNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(HuffmanNode leftChild) {
        this.leftChild = leftChild;
    }

    public HuffmanNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(HuffmanNode rightChild) {
        this.rightChild = rightChild;
    }

    public Character getSimbol() {
        return simbol;
    }

    public void setSimbol(Character simbol) {
        this.simbol = simbol;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
