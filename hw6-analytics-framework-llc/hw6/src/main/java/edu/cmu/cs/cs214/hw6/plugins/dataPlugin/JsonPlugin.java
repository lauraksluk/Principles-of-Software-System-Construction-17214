package edu.cmu.cs.cs214.hw6.plugins.dataPlugin;
import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.io.FileReader;
import java.util.Map;

import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.VaccineRecord;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.stream.Collectors;

/**
 * Implementation of the data plugin for obtaining data from a local *.json data file.
 */
public class JsonPlugin implements DataPlugin{
    private String pluginName = "JsonPlugin";
    private List<String> fieldNames;
    private Map<Groupby, DataRecord> data;
    private int earliestDate;
    private int latestDate;
    private static final int numberFields = 8;
    private static final String pathName = "vaccination.json";

    /**
     * Constructor for the implementation of the JSON plugin.
     * Initializes collections for fields, and sets up variables.
     */
    public JsonPlugin(){
        fieldNames = new ArrayList<>();
        fieldNames.add("distributed");
        fieldNames.add("distributedJanssen");
        fieldNames.add("distributedPfizer");
        fieldNames.add("distributedModerna");
        fieldNames.add("administered");
        fieldNames.add("administeredJanssen");
        fieldNames.add("administeredPfizer");
        fieldNames.add("administeredModerna");
        earliestDate = Integer.MAX_VALUE;
        latestDate = Integer.MIN_VALUE;
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
        return pluginName;
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
     * Read data field names and data values from given path to obtain raw data.
     * @param filePath The path of the data being extracted, ie. file path
     */
    @Override
    public void read(String filePath){
        JSONParser parser = new JSONParser();
        try {
            // parse file
            JSONArray array = (JSONArray) parser.parse(new FileReader(filePath));
            List<VaccineRecord> list = new ArrayList<>();

            for(Object o: array){
                JSONObject object  = (JSONObject) o;
                long Distributed = (Long) object.get("Distributed");
                long Distributed_Janssen = (Long) object.get("Distributed_Janssen");
                long Distributed_Pfizer = (Long) object.get("Distributed_Pfizer");
                long Distributed_Moderna = (Long) object.get("Distributed_Moderna");
                long Administered = (Long) object.get("Administered");
                long Administered_Janssen = (Long) object.get("Administered_Janssen");
                long Administered_Pfizer = (Long) object.get("Administered_Pfizer");
                long Administered_Moderna = (Long) object.get("Administered_Moderna");
                String state = (String) object.get("Location");
                String Date = (String) object.get("Date");

                int index1 = Date.indexOf('/',0);
                int index2 = Date.indexOf('/', 3);
                String month = Date.substring(0, index1);
                String date = Date.substring(index1+1, index2);
                String year = Date.substring(index2+1);
                StringBuilder sb = new StringBuilder();

                if(month.length() == 1){
                    sb.append("0");
                }
                sb.append(month);
                sb.append("/");

                if(date.length() == 1) {
                    sb.append("0");
                }
                sb.append("01");
                sb.append("/");
                sb.append("20");
                sb.append(year);
                String dateFormatted = sb.toString();

                StringBuilder sb1 = new StringBuilder();
                sb1.append("20");
                sb1.append(year);
                if(month.length() == 1){
                    sb1.append("0");
                }
                sb1.append(month);
                String time = sb1.toString();
                int time1 = Integer.parseInt(time);
                if(time1 < earliestDate){
                    earliestDate = time1;
                }
                if(time1 > latestDate){
                    latestDate = time1;
                }

                Groupby groupby = new Groupby(dateFormatted, state);
                VaccineRecord vaccineRecord = new VaccineRecord(groupby, Distributed, Distributed_Janssen, Distributed_Pfizer, Distributed_Moderna, Administered, Administered_Janssen, Administered_Pfizer, Administered_Moderna);
                list.add(vaccineRecord);
            }

            data = list.stream().collect(Collectors.groupingBy(VaccineRecord::getGroupby, Collectors.reducing((sum, s)->
                            new VaccineRecord(s.getGroupby(),sum.getDistributed() + s.getDistributed(),
                                    sum.getAdministeredJohnson() + s.getDistributedJohnson(),
                                    sum.getDistributedPfizer() + s.getDistributedPfizer(),
                                    sum.getDistributedModerna() + s.getDistributedModerna(),
                                    sum.getAdministered() + s.getAdministered(),
                                    sum.getAdministeredJohnson() + s.getAdministeredJohnson(),
                                    sum.getDistributedPfizer() + s.getAdministeredPfizer(),
                                    sum.getAdministeredModerna() + s.getDistributedModerna()))))
                    .entrySet().stream().collect(Collectors.toMap(
                            entry->entry.getKey(),
                            entry->new DataRecord(entry.getValue().get())
                    ));
        } catch(ParseException | FileNotFoundException e) {
            System.out.println("error in JsonPlugin's read");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the data extracted from specific data source that was saved into a collection as a field.
     * @return Map of <key, value> pairs of data extracted from the data source
     */
    @Override
    public Map<Groupby, DataRecord> getData() {
        return data;
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
    public int getEarliestDate(){
        return earliestDate;
    }

    /**
     * Obtains the latest date that has data from the data plugin.
     * @return the latest date in integer form (YYYYMM)
     */
    @Override
    public int getLatestDate(){
        return latestDate;
    }

    @Override
    public void read(String filePath, String key) {

    }

    @Override
    public String getKey() {
        return null;
    }
}

