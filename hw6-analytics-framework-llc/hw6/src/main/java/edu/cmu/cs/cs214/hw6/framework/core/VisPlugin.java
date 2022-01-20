package edu.cmu.cs.cs214.hw6.framework.core;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;

/**
 * Interface for visualization plugins. Defines methods for showing data for visualization.
 */
public interface VisPlugin {

    /**
     * Retrieves the name of the plugin.
     * @return the name of the plugin
     */
    String getPluginName();

    /**
     * Called (only once) when the plugin is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before extracting data (if necessary).
     *
     * @param framework The {@link DataFramework} instance with which the plug-in
     *                  was registered.
     */
    void register(DataFramework framework);

    /**
     * Create charts based on the dimensions users choose
     * @param chartInfo class holding relevant data, and the type of data for x and y axes.
     * @return a String representation for displaying the chart, in Javascript.
     */
    String createChartString(ChartInfo chartInfo);
}
