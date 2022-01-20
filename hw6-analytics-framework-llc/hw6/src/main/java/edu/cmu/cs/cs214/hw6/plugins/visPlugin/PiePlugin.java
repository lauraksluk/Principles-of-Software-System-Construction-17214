package edu.cmu.cs.cs214.hw6.plugins.visPlugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.VisPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;

import java.util.List;

/**
 * Implementation of the visualization plugin to display a pie chart.
 */
public class PiePlugin implements VisPlugin{
    private int CHART_WIDTH;
    private int CHART_HEIGHT;
    private static final String pluginName = "PiePlugin";

    /**
     * Default constructor for implementation of pie chart visualization plugin.
     */
    public PiePlugin() {
        this.CHART_HEIGHT = 600;
        this.CHART_WIDTH = 800;
    }

    /**
     * Constructor for implementation of pie chart visualization plugin.
     * @param width width of chart
     * @param height height of chart
     */
    public PiePlugin(int width, int height){
        this.CHART_WIDTH = width;
        this.CHART_HEIGHT = height;
    }

    /**
     * Retrieves the name of the plugin.
     * @return the name of the plugin
     */
    @Override
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Called (only once) when the plugin is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before extracting data (if necessary).
     *
     * @param framework The {@link DataFramework} instance with which the plug-in
     *                  was registered.
     */
    @Override
    public void register(DataFramework framework) {

    }

    /**
     * Create charts based on the dimensions users choose
     * @param data class holding relevant data, and the type of data for x and y axes.
     * @return a String representation for displaying the chart, in Javascript.
     */
    public String createChartString(ChartInfo data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script>");
        stringBuilder.append("var data = [{values: [");
        List<String> keySet = data.getData().keySet().stream().toList();
        for (int i = 0; i < keySet.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(data.getData().get(keySet.get(i)).get(0));
        }
        stringBuilder.append("], labels:[");
        for (int i = 0; i < keySet.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("'");
            stringBuilder.append(keySet.get(i));
            stringBuilder.append("'");
        }
        stringBuilder.append("], type:");
        stringBuilder.append("'pie'}]; var layout = {height: 400, width:500, title: 'Vaccine type distribution'}; " +
                "Plotly.newPlot('myDiv',data, layout);</script>");
        return stringBuilder.toString();
    }
}