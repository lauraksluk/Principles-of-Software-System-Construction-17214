package edu.cmu.cs.cs214.hw6.plugins.visPlugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.VisPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;

import java.util.*;

/**
 * Implementation of the visualization plugin to display a line plot.
 */
public class PlotPlugin implements VisPlugin {
    private String pluginName = "PlotPlugin";
    private static final List<String> states = List.of("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VA", "WA", "WV", "WI", "WY", "DC");

    /**
     * Constructor for implementation of line plot visualization plugin.
     */
    public PlotPlugin() {

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

    private List<List<Long>> prepareDataMonth(List<String> xTrueValues, List<Long> yValues) {
        List<Long> xPlotValues = new ArrayList<>();
        for (String key : xTrueValues) {
            String kTransformed = key.substring(6, 10) + key.substring(0, 2);
            xPlotValues.add(Long.parseLong(kTransformed));
        }
        return sortXYValues(xPlotValues, yValues);
    }

    private List<List<Long>> sortXYValues(List<Long> xValues, List<Long> yValues) {
        List<List<Long>> xyValues = new ArrayList<>();
        for (int i = 0; i < yValues.size(); i++) {
            List<Long> inner = new ArrayList<>();
            inner.add(xValues.get(i));
            inner.add(yValues.get(i));
            xyValues.add(inner);
        }
        Collections.sort(xyValues, new Comparator<List<Long>>() {
            @Override
            public int compare(List<Long> o1, List<Long> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });
        return xyValues;
    }

    private List<List<Long>> sortXYValues(List<Long> textNum, List<Long> xValues, List<Long> yValues) {
        List<List<Long>> xyValues = new ArrayList<>();
        for (int i = 0; i < yValues.size(); i++) {
            List<Long> inner = new ArrayList<>();
            inner.add(textNum.get(i));
            inner.add(xValues.get(i));
            inner.add(yValues.get(i));
            xyValues.add(inner);
        }
        Collections.sort(xyValues, new Comparator<List<Long>>() {
            @Override
            public int compare(List<Long> o1, List<Long> o2) {
                return o1.get(1).compareTo(o2.get(1));
            }
        });
        return xyValues;
    }

    /**
     * Create charts based on the dimensions users choose
     * @param chartInfo class holding relevant data, and the type of data for x and y axes.
     * @return a String representation for displaying the chart, in Javascript.
     */
    @Override
    public String createChartString(ChartInfo chartInfo) {
        Map<String, List<Long>> data = chartInfo.getData();
        List<String> xTrueValues = new ArrayList<>();

        List<Long> xPlotValues = new ArrayList<>();
        List<Long> yValues = new ArrayList<>();

        String xLabel = chartInfo.getXLabel();
        String yLabel = chartInfo.getYLabel();

        for (String key : data.keySet()) {
            // x-axis : date or state
            if (data.get(key).size() == 1) {
                xTrueValues.add(key);
                yValues.add(data.get(key).get(0));
                // general type x-y axes
            } else if (data.get(key).size() == 2) {
                xPlotValues.add(data.get(key).get(0));
                yValues.add(data.get(key).get(1));
            }
        }
        List<List<Long>> xYValues = new ArrayList<>();
        List<Long> toPlotXValues = new ArrayList<>();
        List<Long> toPlotYValues = new ArrayList<>();

        List<String> texts = new ArrayList<>();
        // general type x-y axes
        if (xTrueValues.size() == 0) {
            texts = data.keySet().stream().toList();
            List<Long> textNum = new ArrayList<>();
            for (int i = 0; i < texts.size(); i++) {
                textNum.add((long)states.indexOf(texts.get(i)));
            }
            xYValues = sortXYValues(textNum, xPlotValues, yValues);
            texts = new ArrayList<>();
            for (List<Long> inner : xYValues) {
                texts.add(states.get(Math.toIntExact(inner.get(0))));
                toPlotXValues.add(inner.get(1));
                toPlotYValues.add(inner.get(2));
            }
        }

        // date x-axis
        if (xLabel.equalsIgnoreCase("date")) {
            xYValues = prepareDataMonth(xTrueValues, yValues);
            for (List<Long> inner : xYValues) {
                toPlotXValues.add(inner.get(0));
                toPlotYValues.add(inner.get(1));
            }
        }
        // state x-axis
        if (xLabel.equalsIgnoreCase("state")) {
            return createChartByState(xTrueValues, yValues, xLabel, yLabel);
            // date x-axis
        } else if (xLabel.equalsIgnoreCase("date")) {
            return createChartByDate(toPlotXValues, toPlotYValues, xLabel, yLabel);
        } else {
            return createChartByGeneralType(toPlotXValues, toPlotYValues, texts, xLabel, yLabel);
        }
    }

    private String createChartByDate(List<Long> toPlotXValues, List<Long> toPlotYValues, String xLabel, String yLabel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script>var trace = {x: [");
        for (int i = 0; i < toPlotXValues.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("'");
            String old = toPlotXValues.get(i).toString();
            String updated = old.substring(0, 4) + "-" + old.substring(4, 6);
            stringBuilder.append(updated);
            stringBuilder.append("'");
        }
        stringBuilder.append("], y: [");
        for (int i = 0; i < toPlotXValues.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(toPlotYValues.get(i));
        }
        stringBuilder.append("],\n" +
                "    type: 'scatter'\n" +
                "};\n" +
                "var data = [trace];\n" +
                "var layout = {\n" +
                "    title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append(" vs. ");
        stringBuilder.append(yLabel);
        stringBuilder.append("',");
        stringBuilder.append("xaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append("'},");
        stringBuilder.append("yaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(yLabel);
        stringBuilder.append("'},");
        stringBuilder.append(
                "    height: 800, width: 600,\n" +
                        "    showlegend: false\n" +
                        "};\n" +
                        "\n" +
                        "Plotly.newPlot('myDiv', data, layout, {scrollZoom: true});</script>");
        return stringBuilder.toString();
    }

    private String createChartByGeneralType(List<Long> toPlotXValues, List<Long> toPlotYValues, List<String> texts, String xLabel, String yLabel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script>var trace = {\n" +
                "  x: [");
        for (int i = 0; i < toPlotXValues.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(toPlotXValues.get(i));
        }
        stringBuilder.append("], y:[");
        for (int i = 0; i < toPlotXValues.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(toPlotYValues.get(i));
        }
        stringBuilder.append("], mode: 'line+markers', connectgaps: true, text: [");
        for (int i = 0; i < texts.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("'");
            stringBuilder.append(texts.get(i));
            stringBuilder.append("'");
        }
        stringBuilder.append("], marker: {    color: 'rgb(255, 217, 102)',\n" +
                "    size: 12\n" +
                "  },\n" +
                "  type: 'scatter'\n" +
                "}; var data = [trace]; var layout = {\n" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append(" vs. ");
        stringBuilder.append(yLabel);
        stringBuilder.append("',");
        stringBuilder.append("xaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append("'},");
        stringBuilder.append("yaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(yLabel);
        stringBuilder.append("'},");
        stringBuilder.append(
                "height: 800, width: 600, \n" +
                        "  showlegend: false\n" +
                        "};\n" +
                        "Plotly.newPlot('myDiv', data, layout);</script>");
        return stringBuilder.toString();
    }

    private String createChartByState(List<String> states, List<Long> yValues, String xLabel, String yLabel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script>var trace = {x: [");
        for (int i = 0; i < states.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("'");
            stringBuilder.append(states.get(i));
            stringBuilder.append("'");
        }
        stringBuilder.append("], y: [");
        for (int i = 0; i < states.size(); i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(yValues.get(i));

        }
        stringBuilder.append("],\n" +
                "    type: 'scatter'\n" +
                "};\n" +
                "var data = [trace];\n" +
                "var layout = {\n" +
                "    title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append(" vs. ");
        stringBuilder.append(yLabel);
        stringBuilder.append("',");
        stringBuilder.append("xaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(xLabel);
        stringBuilder.append("'},");
        stringBuilder.append("yaxis: {" +
                "title: ");
        stringBuilder.append("'");
        stringBuilder.append(yLabel);
        stringBuilder.append("'},");
        stringBuilder.append(
                "    height: 800, width: 600,\n" +
                        "    showlegend: false\n" +
                        "};\n" +
                        "\n" +
                        "Plotly.newPlot('myDiv', data, layout, {scrollZoom: true});</script>");
        return stringBuilder.toString();
    }
}