package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface for data plugins. Defines methods for obtaining data from a data plugin.
 */
public interface DataPlugin {

    /**
     * Retrieves the name of the plugin.
     * @return the name of the plugin
     */
    String getPluginName();

    /**
     * Read data field names and data values from given path to obtain raw data.
     * @param filePath The path of the data being extracted.
     */
    void read(String filePath);

    /**
     * Read data field names and data values from given path with a key to obtain raw data.
     * @param filePath The path of the data being extracted.
     * @param key The key of retrieve data, like an API key.
     */
    void read(String filePath, String key);

    /**
     * Retrieves the data extracted from specific data source that was saved into a collection as a field.
     * @return Map of <key, value> pairs of data extracted from the data source
     */
    Map<Groupby, DataRecord> getData();

    /**
     * Retrieves the field names of the extracted data.
     * @return a list of field names
     */
    List<String> getFieldNames();

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
     * Obtains the earliest date that has data from the data plugin.
     * @return the earliest date in integer form (YYYYMM)
     */
    int getEarliestDate();

    /**
     * Obtains the latest date that has data from the data plugin.
     * @return the latest date in integer form (YYYYMM)
     */
    int getLatestDate();

    /**
     * Obtains the number of fields obtained from the data plugin.
     * @return the number of fields
     */
    int getNumberFields();

    /**
     * Obtains the path where the data was extracted from the data plugin.
     * @return the path name, can be URL or local filepath
     */
    String getPathName();

    /**
     * Obtains the key to identify a data source.
     * @return The key to access the data
     */
    String getKey();
}
