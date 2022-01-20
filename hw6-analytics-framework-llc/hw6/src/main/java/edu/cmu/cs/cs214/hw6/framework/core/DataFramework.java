package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;

import java.util.List;
import java.util.Map;

/**
 * Interface for the COVID-19 data analyzing framework, that incorporates
 * the usage of plugins– data plugins to obtain data, and visualization plugins
 * to visualize the processed data.
 * Defines registering methods, data processing/analysis methods to implement.
 */
public interface DataFramework {

    /**
     * Method to register any data plugins in order to link it
     * with the framework implementation.
     * @param dataPlugin data plugin to register
     */
    void registerDataPlugins(DataPlugin dataPlugin);

    /**
     * Method to register any visualization plugins in order to link
     * it with the framework implementation.
     * @param visPlugin visualization plugin to register
     */
    void registerVisPlugins(VisPlugin visPlugin);

    /**
     * Method to add a plugin instance to a stored collection.
     * Also obtains the raw data from the data plugin implementation.
     * @param index index of data plugin
     */
    void addCurrentDataPlugin(int index);

    /**
     * Method to set current visualization plugin.
     * @param index The index of the visualization plugin in the visualization plugin list.
     */
    void setCurVisPlugin(int index);

    /**
     * Method to combine all parts of raw data obtained from all data plugins
     * into a single object, holding all data.
     * @return {@code true} if successfully combine the data, {@code false} if no data can be combined
     */
    boolean combineData();

    /**
     * Method to obtain all the field names for all the fields of data obtained
     * from all the different data plugins.
     * @return list of field names
     */
    List<String> getFieldNames();

    /**
     * Method to obtain a grouping of data, either by state or by date (month).
     * @param xCoordinate the data on x-axis, either state or date
     * @param yCoordinate the data on y-axis
     * @param groupDimension the other dimension
     *                       if x-axis is state, other dimension is date
     *                       if x-axis is date, other dimension is state
     * @return a Map with state/date as key, and its corresponding y-value (data) as value
     */
    Map<String, Long> groupByStateOrMonth(String xCoordinate, String yCoordinate, String groupDimension);

    /**
     * Method to obtain a grouping of data by state.
     * Generally used to obtain datasets for pie charts.
     * @param types a list of labels/types (analogous to x-axis); categories
     * @param groupDimension specified state to obtain data for
     * @return a Map with the type/category as key, and its corresponding data value as value
     */
    Map<String, Long> groupByTypeInState(List<String> types, String groupDimension);

    /**
     * Method to obtain a general, specified grouping of data (excluding by state/date).
     * @param xCoordinate the data on x-axis
     * @param yCoordinate the data on y-axis
     * @param date the specified date to group data
     * @return a Map with the state as a key, and its corresponding data value, as value
     */
    Map<String, List<Long>> groupByGeneralType(String xCoordinate, String yCoordinate, String date);

    /**
     * Method to pass data to visualization plugins.
     * @param chartInfo The {@link ChartInfo} that contains information to display on the visualization plugin
     * @return A wrapped String to display on the frontend
     */
    String createChart(ChartInfo chartInfo);
}
