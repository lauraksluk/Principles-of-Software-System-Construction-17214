package edu.cmu.cs.cs214.hw6.plugins.dataPlugin;

import edu.cmu.cs.cs214.hw6.framework.core.DataFramework;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.CaseRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.DataRecord;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.Groupby;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the data plugin for (web-scrapping) obtaining data from a web API.
 */
public class ApiPlugin implements DataPlugin {
    private static final String pluginName = "ApiPlugin";
    private Map<Groupby, DataRecord> data;
    private List<String> fieldNames;
    private static HttpClient client = HttpClient.newHttpClient();
    private int earliestDate;
    private int latestDate;
    private static final int numberFields = 2;
    private static final String pathName = "states.timeseries.csv";

    // TODO: Fill in API key here
    private static final String API_KEY = "";

    /**
     * Constructor for implementation of web-API data plugin.
     * Initializes collections for fields, and sets up variables.
     */
    public ApiPlugin() {
        data = new HashMap<>();
        fieldNames = new ArrayList<>();
        fieldNames.add("newCases");
        fieldNames.add("deaths");
        earliestDate = Integer.MAX_VALUE;
        latestDate = Integer.MIN_VALUE;
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
     * @return the path name, ie. URL
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

    @Override
    public void read(String filePath) {
    }

    /**
     * Read data field names and data values from given path to obtain raw data.
     * @param filePath The path of the data being extracted, the URL
     */
    @Override
    public void read(String filePath, String key) {
        String rawData = runWebAPIRequest(filePath, key);
        parseData(rawData);
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
        return this.fieldNames;
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

    private String runWebAPIRequest(String filePath, String key) {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.covidactnow.org/v2/" + filePath + "?apiKey=" + key))
                .header("Authorization", "Key " + key)
                .header("Content-Type", "application/csv")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    private void parseData(String rawData) {
        int i = 0;
        Scanner scanner = new Scanner(rawData);
        List<CaseRecord> caseRecords = new ArrayList<>();
        while (scanner.hasNext()) {
            if (i == 0) {
                i++;
                scanner.nextLine();
                continue;
            }
            String[] tmp = scanner.nextLine().split(",");
            String date = parseDateStr(tmp[0]);
            String state = tmp[2];
            int newCases = Integer.parseInt(tmp[8] == "" ? "0": tmp[8]);
            int deaths = Integer.parseInt(tmp[9] == "" ? "0": tmp[9]);
            CaseRecord caseRecord = new CaseRecord(new Groupby(date, state), newCases, deaths);
            caseRecords.add(caseRecord);
        }
        scanner.close();

        data = caseRecords.stream().collect(Collectors.groupingBy(CaseRecord::getGroupby, Collectors.reducing((sum, s)->
                new CaseRecord(s.getGroupby(), sum.getNewCases() + s.getNewCases(),
                        sum.getDeaths() + s.getDeaths()))))
                .entrySet().stream().collect(Collectors.toMap(
                        entry->entry.getKey(),
                        entry->new DataRecord(entry.getValue().get())
                ));
    }

    private String parseDateStr(String dateStr) {
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        int dateInt = Integer.parseInt(year) * 100 + Integer.parseInt(month);
        if (dateInt < earliestDate) {
            earliestDate = dateInt;
        }
        if (dateInt > latestDate) {
            latestDate = dateInt;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(month);
        stringBuilder.append("/");
        stringBuilder.append("01");
        stringBuilder.append("/");
        stringBuilder.append(year);
        return stringBuilder.toString();
    }

    /**
     * Obtains the earliest date that has data from the data plugin.
     * @return the earliest date in integer form (YYYYMM)
     */
    @Override
    public int getEarliestDate() {
        return earliestDate;
    }

    /**
     * Obtains the latest date that has data from the data plugin.
     * @return the latest date in integer form (YYYYMM)
     */
    @Override
    public int getLatestDate() {
        return latestDate;
    }

    @Override
    public String getKey() {
        return this.API_KEY;
    }
}
