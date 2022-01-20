package edu.cmu.cs.cs214.hw6.framework.core;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.HospitalRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPluginStub2 implements DataPlugin{
    private static final String pluginName = "DataPluginStub2";
    private List<String> fieldNames = new ArrayList<>();
    private Map<Groupby, DataRecord> dataSet= new HashMap<Groupby, DataRecord>();
    private DataFramework framework;
    private int earliest;
    private int latest;
    private static final int numberFields = 4;
    private static final String pathName = "hospital.csv";

    public DataPluginStub2() {
        fieldNames.add("hospitalized");
        fieldNames.add("inICU");
        fieldNames.add("onVentilator");
        fieldNames.add("recovered");
        earliest = 202105;
        latest = 202112;
        Groupby groupBy1 = new Groupby("05/05/2021", "PA");
        HospitalRecord hospitalRecord1 = new HospitalRecord(groupBy1, 100L, 20L, 3L, 5L);
        DataRecord dataRecord1 = new DataRecord(hospitalRecord1);
        Groupby groupBy2 = new Groupby("06/10/2021", "PA");
        HospitalRecord hospitalRecord2 = new HospitalRecord(groupBy2, 2000L, 200L, 30L, 50L);
        DataRecord dataRecord2 = new DataRecord(hospitalRecord2);
        dataSet.put(groupBy1, dataRecord1);
        dataSet.put(groupBy2, dataRecord2);
    }

    @Override
    public String getPluginName() {
        return this.pluginName;
    }

    @Override
    public void read(String filePath) {

    }

    @Override
    public void read(String filePath, String key) {

    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public Map<Groupby, DataRecord> getData() {
        return this.dataSet;
    }

    @Override
    public List<String> getFieldNames() {
        return this.fieldNames;
    }

    @Override
    public void register(DataFramework framework) {
        this.framework = framework;

    }

    @Override
    public int getEarliestDate() {
        return this.earliest;
    }

    @Override
    public int getLatestDate() {
        return this.latest;
    }

    @Override
    public int getNumberFields() {
        return this.numberFields;
    }

    @Override
    public String getPathName() {
        return pathName;
    }
}


