package edu.cmu.cs.cs214.hw6.framework.core;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class testFrameworkImplWithStub {
    private DataFrameworkImpl implementation = new DataFrameworkImpl();
    private List<DataPlugin> curDataPlugins;
    private List<DataRecord> dataRecords;
    int earliestDate;
    int latestDate;


    @Before
    public void setUp() throws IOException, InterruptedException {
        DataPluginStub1 dataPluginStub1 = new DataPluginStub1();
        DataPluginStub2 dataPluginStub2 = new DataPluginStub2();
        implementation.registerDataPlugins(dataPluginStub1);
        implementation.registerDataPlugins(dataPluginStub2);
        implementation.addCurrentDataPlugin(0);
        implementation.addCurrentDataPlugin(1);
    }

    @Test
    public void testGetFieldNames(){
        implementation.combineData();
        List<String> fieldNames = implementation.getFieldNames();
        assertEquals(fieldNames.size(), 8);
    }


}
