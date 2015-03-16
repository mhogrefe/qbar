package jas.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class KsubSet<E> implements Iterable<List<E>> {
    private final List<E> set; // Iterable<E> also ok

    private final int k;

    public KsubSet(List<E> set, int k) {
        if (set == null) {
            throw new IllegalArgumentException("null set not allowed");
        }
        if (k < 0 || k > set.size()) {
            throw new IllegalArgumentException("k out of range");
        }
        this.set = set;
        this.k = k;
    }

    public Iterator<List<E>> iterator() {
        if (k == 1) {
            return new OneSubSetIterator<>(set);
        }
        return new KsubSetIterator<>(set, k);
    }

}

class KsubSetIterator<E> implements Iterator<List<E>> {
    private final int k;

    private final List<E> rest;

    private E current;

    private Iterator<List<E>> recIter;

    private final Iterator<E> iter;

    public KsubSetIterator(List<E> set, int k) {
        if (set == null || set.size() == 0) {
            throw new IllegalArgumentException("null or empty set not allowed");
        }
        if (k < 2 || k > set.size()) {
            throw new IllegalArgumentException("k out of range");
        }
        this.k = k;
        iter = set.iterator();
        current = iter.next();
        rest = new LinkedList<>(set);
        rest.remove(0);
        if (k == 2) {
            recIter = new OneSubSetIterator<>(rest);
        } else {
            recIter = new KsubSetIterator<>(rest, k - 1);
        }
    }

    public boolean hasNext() {
        return recIter.hasNext() || (iter.hasNext() && rest.size() >= k);
    }

    public List<E> next() {
        if (recIter.hasNext()) {
            List<E> next = new LinkedList<>(recIter.next());
            next.add(0, current);
            return next;
        }
        if (iter.hasNext()) {
            current = iter.next();
            rest.remove(0);
            if (rest.size() < k - 1) {
                throw new NoSuchElementException("invalid call of next()");
            }
            if (k == 2) {
                recIter = new OneSubSetIterator<>(rest);
            } else {
                recIter = new KsubSetIterator<>(rest, k - 1);
            }
            return this.next();
        }
        throw new NoSuchElementException("invalid call of next()");
    }

    public void remove() {
        throw new UnsupportedOperationException("cannnot remove subsets");
    }
}

class OneSubSetIterator<E> implements Iterator<List<E>> {

    private final Iterator<E> iter;

    public OneSubSetIterator(List<E> set) {
        if (set == null || set.size() == 0) {
            iter = null;
            return;
        }
        iter = set.iterator();
    }


    /**
     * Test for availability of a next subset.
     *
     * @return true if the iteration has more subsets, else false.
     */
    public boolean hasNext() {
        return iter != null && iter.hasNext();
    }


    /**
     * Get next subset.
     *
     * @return next subset.
     */
    public List<E> next() {
        List<E> next = new LinkedList<>();
        next.add(iter.next());
        return next;
    }


    /**
     * Remove the last subset returned from underlying set if allowed.
     */
    public void remove() {
        throw new UnsupportedOperationException("cannnot remove subsets");
    }

}
