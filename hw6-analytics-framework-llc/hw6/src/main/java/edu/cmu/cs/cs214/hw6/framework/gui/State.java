package edu.cmu.cs.cs214.hw6.framework.gui;

import edu.cmu.cs.cs214.hw6.framework.core.DataFrameworkImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
    private final Instruction instruction;
    private final Cell[] dataPlugins;
    private final Cell[] visPlugins;
    private final String dataNumColStyle;
    private final String visNumColStyle;
    private final Button[] buttons;
    private final int visNum;
    private final String chartRecord;

    public State(Instruction instruction, Cell[] dataPlugins, Cell[] visPlugins,
                 String dataNumColStyle, String visNumColStyle, int visNum, Button[] buttons, String chartRecord) {
        this.instruction = instruction;
        this.dataPlugins = dataPlugins;
        this.visPlugins = visPlugins;
        this.dataNumColStyle = dataNumColStyle;
        this.visNumColStyle = visNumColStyle;
        this.visNum = visNum;
        this.buttons = buttons;
        this.chartRecord = chartRecord;
    }

    public static State forState(Instruction instruction, DataFrameworkImpl dataFramework, boolean finishData,
                                 boolean finishVis, Status status, String chartRecord, boolean pieChartUnclickable,
    boolean isMap) {
        Cell[] dataPlugins = getPlugins(dataFramework, finishData, "data", false);
        Cell[] visPlugins = getPlugins(dataFramework, finishVis, "vis", pieChartUnclickable);
        String dataNumColStyle = getNumColStyle(dataFramework, "data");
        String visNumColStyle = getNumColStyle(dataFramework, "vis");
        int visNum = status.getNumButton();
        Button[] buttons = getButtons(dataFramework, status, isMap);
        return new State(instruction, dataPlugins, visPlugins, dataNumColStyle, visNumColStyle, visNum, buttons, chartRecord);
    }

    private static Cell[] getPlugins(DataFrameworkImpl dataFramework, boolean finish, String type, boolean pieChartUnclickable) {
        List<String> pluginNames = dataFramework.getPluginName(type);
        List<String> curPluginNames = dataFramework.getPluginName("other");
        int num = pluginNames.size();
        Cell[] cells = new Cell[num];
        for (int i = 0; i < num; i++) {
            String text = pluginNames.get(i);
            String link = "";
            String clazz = "";
            if (!finish && !curPluginNames.contains(pluginNames.get(i)) && (i != 0 || !pieChartUnclickable)) {
                clazz = "playable";
                if (type.equals("data")) {
                    link = "/data?x=" + i;
                } else {
                    link = "/visualization?x=" + i;
                }
            }
            cells[i] = new Cell(text, clazz, link);
        }
        return cells;
    }

    private static String getNumColStyle(DataFrameworkImpl dataFramework, String type) {
        int numCols = dataFramework.getPluginName(type).size();
        List<String> style = new ArrayList<String>();
        for (int i=0; i<numCols; i++){
            style.add("auto");
        }
        return String.join(" ", style);
    }

    // Change later
    private static Option[] getOptions(DataFrameworkImpl dataFramework, Status status, String type, boolean isMap) {
        // GetOptions();
        List<String> optionStrs = getFinalOptions(dataFramework, status, type, isMap);
        Option[] options = new Option[optionStrs.size()];
        for (int i=0; i<optionStrs.size(); i++){
            String link = "/parameter?option=" + optionStrs.get(i);
            options[i] = new Option(optionStrs.get(i), link);
        }
        return options;
    }

    private static List<String> getFinalOptions(DataFrameworkImpl dataFramework, Status status, String type, boolean isMap) {
        List<String> optionStrs = dataFramework.getOptions(type);
        String previousOption = status.getPreviousSelected();
        String previousPreviousOption = status.getPreviousPreviousSelected();
        if (isMap) {
            optionStrs.remove("date");
            optionStrs.remove("state");
        }
        if (previousOption != null && previousPreviousOption == null) {
            optionStrs.remove(previousOption);
            optionStrs.remove("date");
            optionStrs.remove("state");
        } else if (previousPreviousOption != null) {
            if (previousPreviousOption.equals("date")) {
                optionStrs = dataFramework.getOptions("state");
            } else {
                optionStrs = dataFramework.getOptions("date");
            }
        }
        return optionStrs;
    }

    private static Button[] getButtons(DataFrameworkImpl dataFramework, Status status, boolean isMap) {
        int numButton = status.getNumButton();
        Button[] buttons = new Button[numButton];
        List<String> names = status.getButtonNames();
        List<String> selecteds = status.getSelecteds();
        for(int i = 0; i < numButton; i++) {
            Option[] options = getOptions(dataFramework, status, names.get(i), isMap);
            buttons[i] = new Button(names.get(i), selecteds.get(i), options);
        }
        return buttons;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public Cell[] getDataPlugins() {
        return dataPlugins;
    }

    public Cell[] getVisPlugins() {
        return visPlugins;
    }

    public String getDataNumColStyle() {
        return dataNumColStyle;
    }

    public String getVisNumColStyle() {
        return visNumColStyle;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public int getVisNum() {
        return visNum;
    }

    public String getChartRecord() {
        return chartRecord;
    }

    @Override
    public String toString() {
        return "State{" +
                "instruction=" + instruction +
                ", dataPlugins=" + Arrays.toString(dataPlugins) +
                ", visPlugins=" + Arrays.toString(visPlugins) +
                ", dataNumColStyle='" + dataNumColStyle + '\'' +
                ", visNumColStyle='" + visNumColStyle + '\'' +
                ", buttons=" + Arrays.toString(buttons) +
                ", visNum=" + visNum +
                ", chartRecord=" + chartRecord +
                '}';
    }
}

