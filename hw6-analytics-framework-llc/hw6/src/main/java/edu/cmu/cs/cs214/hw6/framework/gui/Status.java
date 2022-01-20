package edu.cmu.cs.cs214.hw6.framework.gui;

import java.util.ArrayList;
import java.util.List;

public class Status {
    private List<String> buttonNames;
    private List<String> selecteds;
    private int curIndex;
    private int numButton;

    public Status(int numButton) {
        selecteds = new ArrayList<>();
        for (int i = 0; i < numButton; i++) {
            selecteds.add("");
        }
        curIndex = 0;
        buttonNames = getButtonNames(numButton);
        this.numButton = numButton;
    }

    public void addCurSelected(String name) {
        System.out.println("curIndex is " + curIndex + "option is " + name);
        selecteds.set(curIndex, name);
        curIndex++;
    }

    public boolean isFinish() {
        if (curIndex == numButton) {
            return true;
        }
        return false;
    }

    private List<String> getButtonNames(int num) {
        List<String> names = new ArrayList<>();
        switch (num) {
            case 1:
                names.add("state");
                break;
            case 2:
                names.add("fields");
                names.add("date");
                break;
            case 3:
                names.add("fields");
                names.add("fields");
                names.add("fields");
                break;
        }
        return names;
    }

    public int getNumButton() {
        return numButton;
    }

    public List<String> getButtonNames() {
        return buttonNames;
    }

    public List<String> getSelecteds() {
        return selecteds;
    }

    public String getPreviousSelected() {
        if (curIndex < 1) {
            return null;
        }
        return selecteds.get(curIndex - 1);
    }

    public String getPreviousPreviousSelected() {
        if (curIndex < 2) {
            return null;
        }
        return selecteds.get(curIndex - 2);
    }
}
