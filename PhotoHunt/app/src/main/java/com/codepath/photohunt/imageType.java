package com.codepath.photohunt;

public enum imageType {
    FACE(0), PHOTO(1), CLIPART(2), LINEART(3);

    private final int index;
    imageType(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
