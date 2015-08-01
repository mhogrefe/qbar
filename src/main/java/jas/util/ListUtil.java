package jas.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * List utilities. For example map functor on list elements.
 *
 * @author Heinz Kredel
 */
public class ListUtil {
    //Map a unary function to the list.
    public static <C, D> List<D> map(List<C> list, Function<C, D> f) {
        if (list == null) {
            return null;
        }
        List<D> nl;
        if (list instanceof ArrayList) {
            nl = new ArrayList<>(list.size());
        } else if (list instanceof LinkedList) {
            nl = new LinkedList<>();
        } else {
            throw new RuntimeException("list type not implemented");
        }
        for (C c : list) {
            D n = f.apply(c);
            nl.add(n);
        }
        return nl;
    }
}
