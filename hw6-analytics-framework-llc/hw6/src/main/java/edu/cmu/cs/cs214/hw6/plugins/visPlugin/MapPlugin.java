package edu.cmu.cs.cs214.hw6.plugins.visPlugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.VisPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;
import java.util.*;

/**
 * Implementation of the visualization plugin for displaying data in the form of a heatmap (using map of USA).
 */
public class MapPlugin implements VisPlugin {
    private static final String pluginName = "MapPlugin";
    private static final List<String> states = List.of("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
            "SD", "TN", "TX", "UT", "VA", "WA", "WV", "WI", "WY", "DC");

    // TODO: Fill in mapbox token here
    private static final String MAP_API_KEY = "";

    /**
     * Default constructor for the implementation of the heatmap visualization plugin.
     */
    public MapPlugin() {
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
     * @param chartInfo class holding relevant data, and the type of data for x and y axes.
     * @return a String representation for displaying the chart, in Javascript.
     */
    @Override
    public String createChartString(ChartInfo chartInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<script>" +
                "var data = [{\n" +
                " type: \"choroplethmapbox\", name: \"US states\", geojson: \"https://raw.githubusercontent.com/python-visualization/folium/master/examples/data/us-states.json\", locations: [ \"AL\", \"AK\", \"AZ\", \"AR\", \"CA\", \"CO\", \"CT\", \"DE\", \"FL\", \"GA\",\n" +
                "                                                       \"HI\", \"ID\", \"IL\", \"IN\", \"IA\", \"KS\", \"KY\", \"LA\", \"ME\", \"MD\",\n" +
                "                                                       \"MA\", \"MI\", \"MN\", \"MS\", \"MO\", \"MT\", \"NE\", \"NV\", \"NH\", \"NJ\",\n" +
                "                                                       \"NM\", \"NY\", \"NC\", \"ND\", \"OH\", \"OK\", \"OR\", \"PA\", \"RI\", \"SC\",\n" +
                "                                                       \"SD\", \"TN\", \"TX\", \"UT\", \"VA\", \"WA\", \"WV\", \"WI\", \"WY\", \"DC\" ],\n" +
                "z: [");
        for (int i = 0; i < states.size(); i++) {
            String key = states.get(i);
            if (i != 0) {
                stringBuilder.append(",");
            }
            if (chartInfo.getData().containsKey(key)) {
                stringBuilder.append(chartInfo.getData().get(key).get(0));
            } else {
                stringBuilder.append(0);
            }
        }
        stringBuilder.append("], colorbar: {y: 0, yanchor: \"bottom\", title: {text: \"US states\", side: \"right\"}}}\n" +
                " ]; var layout = {mapbox: {style: \"dark\", center: {lon: -110, lat: 50}, zoom: 0.8}, width: 1200, height: 600, margin: {t: 0, b: 0}};\n " +
                "var config = {mapboxAccessToken: ");
        stringBuilder.append("\"");
        stringBuilder.append(MAP_API_KEY);
        stringBuilder.append("\"");
        stringBuilder.append("}; Plotly.newPlot('myDiv', data, layout, config);</script>");
        return stringBuilder.toString();
    }
}
