package com.codepath.photohunt;

/**
 * Created by Himanshu on 2/14/2015.
 */
public enum size {
    SMALL(0), MEDIUM(1), LARGE(2), XL(3);

    private final int index;

    size(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

}

