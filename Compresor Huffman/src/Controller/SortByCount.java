package Controller;

import Model.HuffmanNode;
import java.util.Comparator;

/**
 *
 * @author María Camila Caicedo Mesa CC:1037656070
 */
public class SortByCount implements Comparator<HuffmanNode> {

    @Override
    public int compare(HuffmanNode t, HuffmanNode t1) {
        return t.getCount() - t1.getCount();
    }

}
