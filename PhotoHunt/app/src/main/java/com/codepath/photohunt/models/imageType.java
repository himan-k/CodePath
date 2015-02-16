package com.codepath.photohunt.models;

public enum imageType {
    FACE(0), PHOTO(1), CLIPART(2), LINEART(3);

    private int index = -1;
    imageType(int index) {
        this.index = index;
    }

    public int index() {
        return index;
    }
}
