package com.codepath.photohunt.models;

/**
 * Created by Himanshu on 2/14/2015.
 */
public class Photo {
    private String imageUrl;
    private size selectedSize;
    private color selectedColor;
    private imageType selectedType;
    private String website;

    public size getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(size selectedSize) {
        this.selectedSize = selectedSize;
    }

    public color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public imageType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(imageType selectedType) {
        this.selectedType = selectedType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
