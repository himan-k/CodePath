package com.codepath.photohunt.models;

public enum color {
        BLACK(0), BLUE(1), BROWN(2), GRAY(3), GREEN(4), ORANGE(5), PINK(6), PURPLE(7), RED(8), TEAL(8), WHITE(10), YELLOW(11);

        private int index = -1;
        color(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }

    }

