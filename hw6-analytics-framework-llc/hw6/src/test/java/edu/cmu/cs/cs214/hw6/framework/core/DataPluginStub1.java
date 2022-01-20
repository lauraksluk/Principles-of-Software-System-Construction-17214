package edu.cmu.cs.cs214.hw6.framework.core;

import edu.cmu.cs.cs214.hw6.plugins.dataRecord.CaseRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPluginStub1 implements DataPlugin{
    private static final String pluginName = "DataPluginStub1";
    private List<String> fieldNames = new ArrayList<>();
    private Map<Groupby, DataRecord> dataSet= new HashMap<Groupby, DataRecord>();
    private DataFramework framework;
    private int earliest;
    private int latest;
    private static final int numberFields = 2;
    private static final String pathName = "https://api.covidactnow.org/v2/";

    public DataPluginStub1() {
        fieldNames.add("newCases");
        fieldNames.add("death");
        earliest = 202105;
        latest = 202112;
        Groupby groupBy1 = new Groupby("05/05/2021", "PA");
        CaseRecord caseRecord1 = new CaseRecord(groupBy1, 1000L, 20L);
        DataRecord dataRecord1 = new DataRecord(caseRecord1);
        Groupby groupBy2 = new Groupby("06/10/2021", "PA");
        CaseRecord caseRecord2 = new CaseRecord(groupBy2, 1500L, 50L);
        DataRecord dataRecord2 = new DataRecord(caseRecord2);
        dataSet.put(groupBy1, dataRecord1);
        dataSet.put(groupBy2, dataRecord2);
    }

    @Override
    public String getPluginName() {
        return this.pluginName;
    }

    @Override
    public void read(String filePath){

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
