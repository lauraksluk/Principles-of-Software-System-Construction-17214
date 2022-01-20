package edu.cmu.cs.cs214.hw6.framework.gui;

public class Cell {
    private final String text;
    private final String clazz;
    private final String link;

    Cell(String text, String clazz, String link) {
        this.text = text;
        this.clazz = clazz;
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public String getClazz() {
        return clazz;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Cell[" +
                "text='" + text + '\'' +
                ", clazz='" + clazz + '\'' +
                ", link='" + link + '\'' +
                ']';
    }
}
