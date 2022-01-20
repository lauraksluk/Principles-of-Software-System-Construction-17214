package edu.cmu.cs.cs214.hw6.framework.core;
import edu.cmu.cs.cs214.hw6.plugins.dataPlugin.CsvPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataPlugin.JsonPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class testDataFrameworkImpl {
    DataFrameworkImpl implementation = new DataFrameworkImpl();

    private List<DataPlugin> curDataPlugins;
    private List<DataRecord> dataRecords;
    int csvEarliestDate;
    int csvLatestDate;
    int jsonEarliestDate;
    int jsonLatestDate;

    @Before
    public void setUp(){
        implementation = new DataFrameworkImpl();
        CsvPlugin csvPlugin = new CsvPlugin();
        csvPlugin.read("hospital.csv");
        csvEarliestDate = csvPlugin.getEarliestDate();
        csvLatestDate = csvPlugin.getLatestDate();

        JsonPlugin jsonPlugin = new JsonPlugin();
        jsonPlugin.read("vaccination.json");
        jsonEarliestDate = jsonPlugin.getEarliestDate();
        jsonLatestDate = jsonPlugin.getLatestDate();

        implementation.registerDataPlugins(csvPlugin);
        implementation.registerDataPlugins(jsonPlugin);
        implementation.addCurrentDataPlugin(0);
        implementation.addCurrentDataPlugin(1);
    }


    @Test
    public void testEarliestDateInCombineData(){
        implementation.combineData();
        int earliestDateAfterCombine;
        if(jsonEarliestDate > csvEarliestDate){
            earliestDateAfterCombine = jsonEarliestDate;
        }else{
            earliestDateAfterCombine = csvEarliestDate;
        }
        assertEquals(earliestDateAfterCombine, 202012);
    }

    @Test
    public void testGetFieldNames(){
        implementation.combineData();
        List<String> fieldNames = implementation.getFieldNames();
        assertEquals(fieldNames.size(), 14);
    }

    @Test
    public void testGroupByStateOrMonth(){
        implementation.combineData();
        Map<String, Long> PADailyDistributedVaccine= implementation.groupByStateOrMonth("date", "distributed", "DC");
        assertEquals(PADailyDistributedVaccine.size(), 4);
    }

    @Test
    public void testGroupByTypeInState(){
        implementation.combineData();
        List<String> vaccineType = List.of("distributedJanssen", "distributedPfizer", "distributedModerna");
        Map<String, Long> vaccineMap = implementation.groupByTypeInState(vaccineType, "PA");
        assertEquals(vaccineMap.size(), 3);
    }

    @Test
    public void testGroupBbyGeneralType(){
        implementation.combineData();
        List<Long> icuAndRecovered = new ArrayList<>();
        Map<String, List<Long>> twoTypes = implementation.groupByGeneralType("inICU", "recovered", "01/01/2021");
        for(Map.Entry<String, List<Long>> entry: twoTypes.entrySet()){
            if(entry.getKey().equals("PA")){
                icuAndRecovered = entry.getValue();
            }
        }
        assertEquals(icuAndRecovered.size(), 2);
    }

}
