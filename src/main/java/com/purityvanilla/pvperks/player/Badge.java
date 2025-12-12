package com.purityvanilla.pvperks.player;

public class Badge {
    private final String name;
    private String text;

    public Badge(String name, String text) {
        this.name = name.toLowerCase();
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
