package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

import java.util.List;
import java.util.Map;

/**
 * Class holding bundled data to be shown with visualization plugin.
 * Includes raw data in the format of a Map, and what the x-coordinate and y-coordinate
 * represent.
 */
public class ChartInfo {
    private Map<String, List<Long>> data;
    private String xLabel;
    private String yLabel;

    /**
     * Constructor to initialize an instance of ChartInfo, holding related information
     * for visualization plugin to show.
     * @param d the raw dataset to show
     * @param x String representation of the meaning for x-axis
     * @param y String representation of the meaning for y-axis
     */
    public ChartInfo(Map<String, List<Long>> d, String x, String y) {
        data = d;
        xLabel = x;
        yLabel = y;
    }

    /**
     * Obtains the raw dataset stored to be shown on GUI.
     * @return the raw data
     */
    public Map<String, List<Long>> getData() {
        return data;
    }

    /**
     * Returns what the x-coordinate is supposed to represent.
     * @return the x-axis label
     */
    public String getXLabel() {
        return xLabel;
    }

    /**
     * Returns what the y-coordinate is supposed to represent.
     * @return the y-axis label
     */
    public String getYLabel() {
        return yLabel;
    }
}
