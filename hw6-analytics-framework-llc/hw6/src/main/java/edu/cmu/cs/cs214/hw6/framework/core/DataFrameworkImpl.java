package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of our COVID-19 data analysis framework.
 */
public class DataFrameworkImpl implements DataFramework{
    private List<DataPlugin> dataPlugins;
    private List<DataPlugin> curDataPlugins;
    private List<VisPlugin> visPlugins;
    private VisPlugin curVisPlugin;
    private List<DataRecord> dataRecords;
    private List<String> fieldNames;
    private int earliestDateInt;
    private int latestDateInt;
    // state names
    private static final List<String> states = List.of("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA", "WA", "WV", "WI", "WY", "DC");
    private List<Groupby> keySet;
    private List<String> dates;

    /**
     * Constructor for the framework implementation.
     * Initializes collections to store data as fields.
     * Also initializes list of field names with "state" and "date".
     */
    public DataFrameworkImpl() {
        dataPlugins = new ArrayList<>();
        curDataPlugins = new ArrayList<>();
        visPlugins = new ArrayList<>();
        curVisPlugin = null;
        dataRecords = new ArrayList<>();
        fieldNames = new ArrayList<>();
        fieldNames.add("state");
        fieldNames.add("date");
        keySet = new ArrayList<>();
        dates = new ArrayList<>();
        earliestDateInt = Integer.MIN_VALUE;
        latestDateInt = Integer.MAX_VALUE;
    }

    /**
     * Registers a data plugin in order to link it with the framework implementation.
     * @param dataPlugin data plugin to register
     */
    @Override
    public void registerDataPlugins(DataPlugin dataPlugin) {
        dataPlugin.register(this);
        dataPlugins.add(dataPlugin);
    }

    /**
     * Registers a visualization plugin in order to link it with the framework implementation.
     * @param visPlugin visualization plugin to register
     */
    @Override
    public void registerVisPlugins(VisPlugin visPlugin) {
        visPlugin.register(this);
        visPlugins.add(visPlugin);
    }

    /**
     * Method to add a plugin instance to a stored collection.
     * Also obtains the raw data from the data plugin implementation.
     * @param index index of data plugin
     */
    @Override
    public void addCurrentDataPlugin(int index) {
        DataPlugin dataPlugin = this.dataPlugins.get(index);
        curDataPlugins.add(dataPlugin);
        if (dataPlugin.getKey() != null) {
            dataPlugin.read(dataPlugin.getPathName(), dataPlugin.getKey());
        } else {
            dataPlugin.read(dataPlugin.getPathName());
        }
    }

    @Override
    public void setCurVisPlugin(int index) {
        curVisPlugin = visPlugins.get(index);
    }

    /**
     * Combines all parts of raw data obtained from all data plugins
     * into a single object, holding all data. This is then stored as a field.
     */
    @Override
    public boolean combineData() {
        for (DataPlugin dataPlugin: curDataPlugins) {
            if (dataPlugin.getData() == null || dataPlugin.getData().isEmpty()) {
                continue;
            }
            int earliest = dataPlugin.getEarliestDate();
            int latest = dataPlugin.getLatestDate();
            if (earliest > earliestDateInt) {
                earliestDateInt = earliest;
            }
            if (latest < latestDateInt) {
                latestDateInt = latest;
            }
        }
        if (earliestDateInt == Integer.MIN_VALUE && latestDateInt == Integer.MAX_VALUE) {
            System.out.println("No data");
            return false;
        }
        generateKeySet();
        Map<Groupby, DataRecord> tmpMap = new HashMap<>();
        for (Groupby groupby: keySet) {
            tmpMap.put(groupby, new DataRecord(groupby));
        }
        for (DataPlugin dataPlugin: curDataPlugins) {
            List<String> tmpFieldNames = dataPlugin.getFieldNames();
            tmpFieldNames.remove("state");
            tmpFieldNames.remove("date");
            fieldNames.addAll(dataPlugin.getFieldNames());
            Map<Groupby, DataRecord> pluginData = dataPlugin.getData();
            for (Groupby groupby: tmpMap.keySet()) {
                DataRecord dataRecord = tmpMap.get(groupby);
                if (pluginData.containsKey(groupby)) {
                    dataRecord.setFields(pluginData.get(groupby).getFields());
                } else {
                    int numberFields = dataPlugin.getNumberFields();
                    List<Long> tmpList = new ArrayList<>();
                    for (int i = 0; i < numberFields; i++) {
                        tmpList.add((long)0);
                    }
                    dataRecord.setFields(tmpList);
                }
            }
        }
        dataRecords = tmpMap.values().stream().toList();
        return true;
    }

    /**
     * Method to obtain all the field names for all the fields of data obtained
     * from all the different data plugins.
     * @return list of field names
     */
    @Override
    public List<String> getFieldNames() {
        List<String> returnFieldNames = new ArrayList<>();
        returnFieldNames.addAll(this.fieldNames);
        return returnFieldNames;
    }

    /**
     * Method to obtain a grouping of data, either by state or by date (month).
     * @param xCoordinate the data on x-axis, either state or date
     * @param yCoordinate the data on y-axis
     * @param groupDimension the other dimension
     *                       if x-axis is state, other dimension is date
     *                       if x-axis is date, other dimension is state
     * @return a Map with state/date as key, and its corresponding y-value (data) as value
     */
    @Override
    public Map<String, Long> groupByStateOrMonth(String xCoordinate, String yCoordinate, String groupDimension) {
        Map<String, Long> extractedData = new HashMap<>();
        int index = fieldNames.indexOf(yCoordinate) - 2;
        if (xCoordinate.equals("date")) {
            for (DataRecord dataRecord: dataRecords) {
                if (dataRecord.getGroupBy().getState().equals(groupDimension)) {
                    extractedData.put(dataRecord.getGroupBy().getDate(), dataRecord.getFields().get(index));
                }
            }
        } else if (xCoordinate.equals("state")){
            for (DataRecord dataRecord: dataRecords) {
                if (dataRecord.getGroupBy().getDate().equals(groupDimension)) {
                    extractedData.put(dataRecord.getGroupBy().getState(), dataRecord.getFields().get(index));
                }
            }
        }
        return extractedData;
    }

    /**
     * Method to obtain a grouping of data by state.
     * Generally used to obtain datasets for pie charts.
     * @param types a list of labels/types (analogous to x-axis); categories
     * @param state specified state to obtain data for
     * @return a Map with the type/category as key, and its corresponding data value as value
     */
    @Override
    public Map<String, Long> groupByTypeInState(List<String> types, String state) {
        Map<String, Long> extractedData = new HashMap<>();
        List<DataRecord> stateDataRecords = dataRecords.stream()
                .filter(c -> c.getGroupBy().getState().equals(state))
                .collect(Collectors.toList());
        for (String type: types) {
            int index = fieldNames.indexOf(type);
            long sum = 0;
            for (DataRecord stateDataRecord: stateDataRecords) {
                sum += stateDataRecord.getFields().get(index);
            }
            extractedData.put(type, sum);
        }
        return extractedData;
    }

    /**
     * Method to obtain a general, specified grouping of data (excluding by state/date).
     * @param xCoordinate the data on x-axis
     * @param yCoordinate the data on y-axis
     * @param date the specified date to group data
     * @return a Map with the state as a key, and its corresponding data value, as value
     */
    @Override
    public Map<String, List<Long>> groupByGeneralType(String xCoordinate, String yCoordinate, String date) {
        int xIndex = fieldNames.indexOf(xCoordinate) - 2;
        int yIndex = fieldNames.indexOf(yCoordinate) - 2;
        List<DataRecord> dateDataRecords = dataRecords.stream()
                .filter(c -> c.getGroupBy().getDate().equals(date))
                .collect(Collectors.toList());
        Map<String, List<Long>> extractedData = new HashMap<>();
        for (DataRecord dataRecord: dateDataRecords) {
            List<Long> xyValues = List.of(new Long[]{dataRecord.getFields().get(xIndex), dataRecord.getFields().get(yIndex)});
            extractedData.put(dataRecord.getGroupBy().getState(), xyValues);
        }
        return extractedData;
    }

    private void generateKeySet() {
        int earliestYear = earliestDateInt / 100;
        int earliestMonth = earliestDateInt % 100;
        int latestYear = latestDateInt / 100;
        int latestMonth = latestDateInt % 100;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate earliestDate;
        LocalDate latestDate;
        if (earliestMonth < 10) {
            earliestDate = LocalDate.parse("0" + earliestMonth + "/01/" + earliestYear, formatter);
        } else {
            earliestDate = LocalDate.parse(earliestMonth + "/01/" + earliestYear, formatter);
        }
        if (latestMonth < 10) {
            latestDate = LocalDate.parse("0" + latestMonth + "/01/" + latestYear, formatter);
        } else {
            latestDate = LocalDate.parse(latestMonth + "/01/" + latestYear, formatter);
        }
        while (earliestDate.compareTo(latestDate) <= 0) {
            dates.add(earliestDate.format(formatter));
            earliestDate = earliestDate.plusMonths(1);
        }
        for (String state: states) {
            for (String date: dates) {
                Groupby groupby = new Groupby(date, state);
                keySet.add(groupby);
            }
        }
    }

    /**
     * Method to obtain the plugin names.
     * @param type the type of plugin, either data or visualization
     * @return a list of all plugin names under specified type
     */
    public List<String> getPluginName(String type){
        if (type.equals("data")) {
            return dataPlugins.stream().map(DataPlugin::getPluginName).collect(Collectors.toList());
        } else if (type.equals("vis")) {
            return visPlugins.stream().map(VisPlugin::getPluginName).collect(Collectors.toList());
        } else {
            return curDataPlugins.stream().map(DataPlugin::getPluginName).collect(Collectors.toList());
        }
    }

    /**
     * Method to obtain all dates related to all data processed in framework.
     * @return a list of all dates
     */
    public List<String> getDates() {
        List<String> returnDates = new ArrayList<>();
        returnDates.addAll(dates);
        return returnDates;
    }

    /**
     * Method to obtain all states related to all data processed in framework.
     * @return a list of all states
     */
    public List<String> getStates() {
        List<String> returnStates = new ArrayList<>();
        returnStates.addAll(states);
        return returnStates;
    }

    /**
     * Method to obtain all options for a specified type.
     * @param type the type for options– either general fields, date, or state
     * @return a list of all the options for the specified type
     */
    public List<String> getOptions(String type) {
        if (type.equals("fields")) {
            return getFieldNames();
        }
        if (type.equals("date")) {
            return getDates();
        }
        return getStates();
    }

    /**
     * Method to determine if the data plugin is selected as one of the current data plugins
     * @param index the index of the data plugin
     * @return boolean, if the stored collection contains the data plugin
     */
    public boolean contains(int index) {
        if (curDataPlugins.contains(dataPlugins.get(index))) {
            return true;
        }
        return false;
    }

    /**
     * Method to pass data to the current visualization plugin.
     * @param chartInfo The {@link ChartInfo} that contains information to display on the visualization plugin
     * @return A wrapped String to display on the frontend
     */
    @Override
    public String createChart(ChartInfo chartInfo) {
        return curVisPlugin.createChartString(chartInfo);
    }
}
