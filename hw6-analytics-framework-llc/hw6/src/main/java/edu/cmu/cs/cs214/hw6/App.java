package edu.cmu.cs.cs214.hw6;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import edu.cmu.cs.cs214.hw6.framework.core.DataFrameworkImpl;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.framework.core.VisPlugin;
import edu.cmu.cs.cs214.hw6.framework.gui.Instruction;
import edu.cmu.cs.cs214.hw6.framework.gui.State;
import edu.cmu.cs.cs214.hw6.framework.gui.Status;
import edu.cmu.cs.cs214.hw6.plugins.dataRecord.ChartInfo;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.util.*;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private DataFrameworkImpl data;
    private Status status;
    private Template template;
    private List<DataPlugin> dataPlugins;
    private List<VisPlugin> visPlugins;
    private boolean finishData;
    private boolean finishVis;
    private boolean pieChartUnclickable;
    private Instruction instruction;
    private String chartRecord;
    private int visOption;
    private boolean isMap;

    public App() throws IOException {
        super(8080);

        newData();
        Handlebars handlebars = new Handlebars();
        this.template = handlebars.compile("template");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    private void newData() {
        this.data = new DataFrameworkImpl();
        dataPlugins = loadDataPlugins();
        for (DataPlugin dp: dataPlugins){
            data.registerDataPlugins(dp);
        }
        visPlugins = loadVisPlugins();
        for (VisPlugin vp: visPlugins) {
            data.registerVisPlugins(vp);
        }
        status = new Status(0);
        finishData = false;
        finishVis = true;
        pieChartUnclickable = false;
        instruction = new Instruction("Welcome to covid data platform");
        chartRecord = null;
        visOption = -1;
        isMap = false;
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            Map<String, String> params = session.getParms();
            instruction = new Instruction("Welcome to covid data platform");
            if (uri.equals("/newdata")) {
                newData();
            } else if (uri.equals("/data")) {
                int index = Integer.parseInt(params.get("x"));
                if (!data.contains(index)) {
                    data.addCurrentDataPlugin(index);
                    if (finishVis) {
                        finishVis = false;
                    }
                    if (!data.contains(2)) {
                        pieChartUnclickable = true;
                    } else {
                        pieChartUnclickable = false;
                    }
                }
            } else if (uri.equals("/visualization")) {
                visOption = Integer.parseInt(params.get("x")) + 1;
                status = new Status(visOption);
                chartRecord = null;
                if (!finishData) {
                    boolean ret = data.combineData();
                    if (ret == false) {
                        instruction = new Instruction("no data! Please click on New Data to start over");
                    }
                    finishData = true;
                }
                if (visOption == 2) {
                    isMap = true;
                } else {
                    isMap = false;
                }
            } else if (uri.equals("/parameter")) {
                String option = params.get("option");
                if (status.getPreviousSelected() == null || !status.getPreviousSelected().equals(option)) {
                    status.addCurSelected(option);
                    if (status.isFinish()) {
                        generateChartStr(visOption);
                    }
                }
            }
            // Extract the view-specific data from the game and apply it to the template.
            State state = State.forState(instruction, data, finishData, finishVis, status, chartRecord, pieChartUnclickable, isMap);
            String HTML = this.template.apply(state);
            return newFixedLengthResponse(HTML);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin: plugins) {
            System.out.println("Loaded plugin " + plugin.getPluginName());
            result.add(plugin);
        }
        return result;
    }

    private static List<VisPlugin> loadVisPlugins() {
        ServiceLoader<VisPlugin> plugins = ServiceLoader.load(VisPlugin.class);
        List<VisPlugin> result = new ArrayList<>();
        for (VisPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getPluginName());
            result.add(plugin);
        }
        return result;
    }

    private Map<String, List<Long>> wrapData(Map<String, Long> rawData) {
        Map<String, List<Long>> wrapData = new HashMap<>();
        for (String key: rawData.keySet()) {
            List<Long> list = new ArrayList<>();
            list.add(rawData.get(key));
            wrapData.put(key, list);
        }
        return wrapData;
    }

    private void generateChartStr(int visOption) {
        Map<String, List<Long>> wrapReturnData = new HashMap<>();
        List<String> selecteds = status.getSelecteds();
        data.setCurVisPlugin(visOption - 1);
        String x = "", y = "";
        switch (visOption) {
            case 1:
                List<String> types = List.of("distributedJanssen", "distributedPfizer", "distributedModerna");
                Map<String, Long> returnData = data.groupByTypeInState(types, selecteds.get(0));
                wrapReturnData = wrapData(returnData);
                break;
            case 2:
                x = "state";
                y = selecteds.get(0);
                wrapReturnData = wrapData(data.groupByStateOrMonth(x, selecteds.get(0), selecteds.get(1)));
                break;
            default:
                x = selecteds.get(0);
                y = selecteds.get(1);
                if (x.equals("state") || x.equals("date")) {
                    wrapReturnData = wrapData(data.groupByStateOrMonth(x, y, selecteds.get(2)));
                } else {
                    wrapReturnData = data.groupByGeneralType(x, y, selecteds.get(2));
                }
                break;
        }
        chartRecord = data.createChart(new ChartInfo(wrapReturnData, x, y));
    }
}
