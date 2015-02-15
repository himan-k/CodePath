package com.codepath.photohunt;

/**
 * Created by Himanshu on 2/14/2015.
 */
public enum size {
    ICON(0), MEDIUM(1), XXLARGE(2), HUGE(3);

    private int index = -1;

    size(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }

}

