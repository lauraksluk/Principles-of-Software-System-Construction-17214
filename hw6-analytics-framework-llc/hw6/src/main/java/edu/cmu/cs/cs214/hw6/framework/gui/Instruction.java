package edu.cmu.cs.cs214.hw6.framework.gui;

public class Instruction {
    private final String text;

    public Instruction(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
