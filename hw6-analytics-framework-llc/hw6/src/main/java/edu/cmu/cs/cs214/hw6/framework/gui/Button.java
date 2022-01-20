package edu.cmu.cs.cs214.hw6.framework.gui;

import java.util.Arrays;

public class Button {
    private final String buttonName;
    private final String selected;
    private final Option[] options;

    public Button(String name, String selected, Option[] options) {
        this.buttonName = name;
        this.selected = selected;
        this.options = options;
    }

    public String getSelected() {
        return selected;
    }

    public Option[] getOptions() {
        for (Option option: options) {
            option.toString();
        }
        return options;
    }

    public String getButtonName() {
        return buttonName;
    }

//    public void setButtonName(String buttonName) {
//        this.buttonName = buttonName;
//    }

    @Override
    public String toString() {
        return "Button{" +
                "buttonName='" + buttonName + '\'' +
                ", selected='" + selected + '\'' +
                ", options=" + Arrays.toString(options) +
                '}';
    }
}
