package edu.cmu.cs.cs214.hw6.framework.gui;

public class Option {
    private final String name;
    private final String link;

    public Option(String name, String link){
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Option{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
