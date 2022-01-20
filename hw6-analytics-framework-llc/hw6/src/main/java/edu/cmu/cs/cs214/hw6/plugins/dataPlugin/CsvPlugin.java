package edu.cmu.cs.cs214.hw6.plugins.dataPlugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.HospitalRecord;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the data plugin for obtaining data from a local *.csv data file.
 */
public class CsvPlugin implements DataPlugin {
    private static final String pluginName = "CSVPlugin";
    private String path = "hospital.csv";
    private List<String> fieldNames;
    private Map<Groupby, DataRecord> dataSet;
    private int earliest;
    private int latest;
    private static final int numberFields = 4;
    private static final String pathName = "hospital.csv";

    /**
     * Constructor for implementation of CSV plugin.
     * Initializes collections for fields, and sets up variables.
     */
    public CsvPlugin() {
        fieldNames = new ArrayList<>();
        fieldNames.add("hospitalized");
        fieldNames.add("inICU");
        fieldNames.add("onVentilator");
        fieldNames.add("recovered");
        dataSet = new HashMap<Groupby, DataRecord>();
    }

    /**
     * Obtains the number of fields obtained from the data plugin.
     * @return the number of fields
     */
    @Override
    public int getNumberFields() {
        return numberFields;
    }

    /**
     * Obtains the path where the data was extracted from the data plugin.
     * @return the path name, ie. local filepath
     */
    @Override
    public String getPathName() {
        return pathName;
    }

    /**
     * Retrieves the name of the plugin.
     * @return the name of the plugin
     */
    @Override
    public String getPluginName() {
        return this.pluginName;
    }

    private String[] parseCSV(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            return null;
        }
        return line.split(",");
    }

    private String formatDate(String dd) {
        String d = "";
        if (dd.length() == 6) {
            d = "0" + dd.substring(0, 2) + "0"
                           + dd.substring(2, 4) + "20" + dd.substring(4, 6);
        }
        else if (dd.length() == 7) {
            if (dd.indexOf('/') == 1) {
                d = "0" + dd.substring(0, 5) + "20" + dd.substring(5, 7);
            }
            if (dd.indexOf('/') == 2)  {
                d = dd.substring(0, 3) + "0" + dd.substring(3, 5) + "20"
                                       + dd.substring(5, 7);
            }
        }
        else if (dd.length() == 8) {
            d = dd.substring(0, 6) + "20" + dd.substring(6, 8);
        }
        char[] ddChars = d.toCharArray();
        ddChars[3] = '0';
        ddChars[4] = '1';
        return String.valueOf(ddChars);
    }

    /**
     * Read data field names and data values from given path to obtain raw data.
     * @param filePath The path of the data being extracted, the local filepath
     */
    @Override
    public void read(String filePath) {
        List<HospitalRecord> tempRecord = new ArrayList<>();
        try {
            Reader r = new FileReader(filePath);
            BufferedReader br = new BufferedReader(r);
            boolean end = false;
            while (!end) {
                String[] vals = parseCSV(br);
                if (vals != null) {
                    int hosp = Integer.parseInt(vals[2]);
                    int icu = Integer.parseInt(vals[3]);
                    int vent = Integer.parseInt(vals[4]);
                    int re = Integer.parseInt(vals[5]);
                    String d = formatDate(vals[0]);
                    String state = vals[1];
                    Groupby gb = new Groupby(d, state);
                    HospitalRecord h = new HospitalRecord(gb, hosp, icu, vent, re);
                    tempRecord.add(h);
                } else {
                    end = true;
                }
            }
            // set latest date
            HospitalRecord lastRecord = tempRecord.get(0);
            String lastDate = lastRecord.getGroupby().getDate();
            String buildLate = lastDate.substring(6, 10) + lastDate.substring(0, 2);
            latest = Integer.parseInt(buildLate);
            // set earliest date
            HospitalRecord firstRecord = tempRecord.get(tempRecord.size() - 1);
            String firstDate = firstRecord.getGroupby().getDate();
            String buildEarly = firstDate.substring(6, 10) + firstDate.substring(0, 2);
            earliest = Integer.parseInt(buildEarly);
            // build data set
            dataSet = tempRecord.stream().collect(Collectors.groupingBy(HospitalRecord::getGroupby, Collectors.reducing((sum, s)->
                            new HospitalRecord(s.getGroupby(), sum.getHospitalized() + s.getHospitalized(),
                                    sum.getInICU() + s.getInICU(), sum.getOnVentilator() + s.getOnVentilator(),
                                    sum.getRecovered() + s.getRecovered()))))
                    .entrySet().stream().collect(Collectors.toMap(
                            entry->entry.getKey(),
                            entry->new DataRecord(entry.getValue().get())));
        } catch (IOException e) {
            System.out.println("File doesn't exist.");
            return;
        }
    }

    /**
     * Retrieves the data extracted from specific data source that was saved into a collection as a field.
     * @return Map of <key, value> pairs of data extracted from the data source
     */
    @Override
    public Map<Groupby, DataRecord> getData() {
        return dataSet;
    }

    /**
     * Retrieves the field names of the extracted data.
     * @return a list of field names
     */
    @Override
    public List<String> getFieldNames() {
        return fieldNames;
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
     * Obtains the earliest date that has data from the data plugin.
     * @return the earliest date in integer form (YYYYMM)
     */
    @Override
    public int getEarliestDate() {
        return earliest;
    }

    /**
     * Obtains the latest date that has data from the data plugin.
     * @return the latest date in integer form (YYYYMM)
     */
    @Override
    public int getLatestDate() {
        return latest;
    }

    @Override
    public void read(String filePath, String key) {
    }

    @Override
    public String getKey() {
        return null;
    }
}
