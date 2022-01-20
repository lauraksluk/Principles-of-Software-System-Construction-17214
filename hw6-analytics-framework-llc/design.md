# Design

## Domain
The idea is to provide an analytics framework for users to analyze stock prices through data retrieval, processing and visualization.

By providing data plugins and visualization plugins interfaces, we allow users to retrieve stock quotes from different sources (provided by data plugins) and shows different results in different ways (provided by the visualization plugins).

In addition, the framework performs data processing that includes merging stock quotes from different data sources and aggregating them by different time period.

To merge the stock prices, stock prices that share the same symbol and date will be considered as duplication. Duplicated stock price will be removed from the system (according to the order of plugins registered).

To aggregate the stock price by different time period, the open price of the first day of a time period will be selected,
as well as the close price for the last date of a time period.
The highest high price of the time period and the lowest low price of the time period will also be selected to represent the aggregated data.

Data plugins could provide *a list of stock prices with symbol, open price, close price, high price, low price, date*.

Possible data plugins:
- Web APIs. Free time series stock API like [Alpha vantage](https://www.alphavantage.co/).
- Local file (e.g. csv)
- Database

Possible visualization plugins:
- Candlestick chart displaying the open, close, high, low price for a specific symbol during a time period.
- Line chart displaying stock comparison between 2+ stocks.
- Bubble chart displaying changes to different stocks within a specific time period.
- Bar chart displaying changes to different stocks in a single day



## Generality vs Specificity
### Generality
To provide generality, the proposed framework supports stock quotes of multiple companies from multiple data sources, to be visualized in multiple ways.

Users can provide a list of stock symbols to each of the data plugins such that they can analyze the stocks that they are interested in.
This allows the framework to be general in the sense that it is not fixated to some specific stock symbols, and it also allows users to select which data sources for which symbols (since data availability could vary for different symbols).

The framework accepts multiple data sources, as there are often multiple data sources of stock prices due to various reasons:
- different coverage (e.g. stock exchanges, companies)
- some APIs may only provide the latest price data but others only provide end-of-day data with a delay
- different data formats (e.g. csv, database, APIs)

With multiple data plugins, the way of retrieving data is abstracted away from the framework itself. The framework only needs to utilize the data returned by the data plugins, but not how they are retrieved.
Should there be duplicated quotes, users can specify the order of preference of the data plugins.

Since some data sources may only come with the closing price, but not opening / high / low. We allow those fields to be nullable to provide a maximum flexibility.
Therefore, visualization plugins should handle nullable prices appropriately.

The framework also aggregates the stock quotes in pre-specified periods (e.g. weekly, monthly, quarterly, yearly).
This allows visualization plugins to choose which granularity to display.
For example, a visualization plugin could gear towards displaying monthly prices only.

Since the framework performs aggregation itself, this provides reusability for data plugins as they only need to provide the raw daily stock quotes.
Similarly, visualization plugins don't have to implement their own aggregation logic by reusing the aggregated stock quotes.

We understand that some visualization plugins may only support one stock symbol at a time (e.g. candlestick chart),
where other may support multiple stock symbols (e.g. line chart for comparison between 2+ stocks).

To allow generality of visualization plugins with different numbers of stock symbol requirement, each visualization plugin should implement a method `isSupported(symbolCount)` to indicate its symbol number requirement.
This allows us to display only the relevant plugins to the users in the GUI.

### Specificity
The framework only supports the stock quotes of stocks, but not other metrics such as market capitalization, P/E ratio.

This allows visualization plugins to be specifically developed for stock quotes, not just a generic line chart showing time series data.
For example, one can develop a candlestick chart plugin that visualizes the open, close, high, low prices, but such plugin would not make sense for metrics such as P/E ratio.

The stocks quotes are specific that they include the opening, closing, high and low prices, not just the end-of-day closing price.
When they are aggregated they represent the opening, closing, high and low prices of the time period.
This allows visualizations plugins to visualize how price changes within a time period.

## Project structure

The organization of our project looks like this:
- src
  - main
    - java/deadlinefighters/analyticsframework
      - framework
        - core
          - Framework
          - FrameworkImpl
          - DataPlugin
          - VisualizationPlugin
        - gui
      - plugin
        - data
          - LocalDataPlugin
          - WebDataPlugin
          - DatabaseDataPlugin
        - visualization
          - CandlestickVisualizationPlugin
          - LineVisualizationPlugin
          - BubbleVisualizationPlugin
          - BarVisualizationPlugin
    - resources/META-INT/services
      - deadlinefighters.analyticsframework.framework.core.DataPlugin
      - deadlinefighters.analyticsframework.framework.core.VisualizationPlugin

The key data structure in our project is the format of the stock quote. Here is the raw data object that originally read from the data source.
```java
public class StockQuote {
    String symbol;
    LocalDate date;
    // nullable as some data sources may not come with particular prices
    Double open;
    Double close;
    Double high;
    Double low;
}

```

Here is the data structure for the aggregated stock quotes
```java
class AggregatedStockQuotes {
    String symbol;
    List<StockQuote> daily;
    List<StockQuote> weekly;
    List<StockQuote> monthly;
    List<StockQuote> quarterly;
    List<StockQuote> yearly;
}
```


The data plugins and the visualization plugins will be both loaded during the startup of the system. The class names for both plugins will be listed in two files under `/resource/META-INF/services/...` and `ServiceLoader` will be called to load the plugins. The framework will register all the listed plugins.

## Plugin interfaces

### Data Plugin
Data plugins will implement the following interface.

`getStockQuotes` is a method that returns a `CompletableFuture<List<StockQuote>>` because some plugin operations can be asynchronous or concurrent (like firing multiple requests to the API at the same time).

Data plugins can optionally parallelize its `getStockQuotes` operations, whereas the framework should parallelize calling `getStockQuotes` of different data plugins.

```java
import java.io.IOException;

class StockQuotesResult {
    List<StockQuote> stockQuotes;
    boolean hasError;
    String errorMessage;
}

public interface DataPlugin {

    /**
     * Get the name of the data plugin to show on the GUI
     *
     * @return name of the data plugin
     */
    String getName();

    /**
     * Called (only once) when the plugin is first registered with the
     * framework, giving the plugin a chance to perform any set-up during
     * plugin registration.
     *
     * @param framework The {@link Framework} instance with which the plugin
     *                  was registered.
     */
    void onRegister(Framework framework);

    /**
     * Establish a connection with the data source, this is called by the
     * framework before getStockQuotes is called.
     *
     * @param arg    argument required to initialize the data plugin
     *               (e.g. local file path, web url or database url)
     * @throws IllegalArgumentException when fail to parse the arg
     *           or IOException when fail to establish a connection
     */
    void openConnection(String arg) throws Exception;

    /**
     * Read from the data source with only selected symbols
     * and parse the data into stock object.
     *
     * If exception was thrown during the execution, the framework should catch
     * ExecutionException and handle it.
     *
     * If symbols are missing in the data source, error message will be written
     * in the result.
     *
     * @param symbols   a list of selected symbols
     * @return a promise of stock quotes result
     */
    CompletableFuture<StockQuotesResult> getStockQuotes(List<String> symbols);

    /**
     * Close the connection with the data source, this is called by the
     * framework after getStockQuotes is called.
     *
     * @throws  IOException when fail to close a connection
     */
    void closeConnection() throws IOException;
}

```

### Visualization Plugin
Visualization plugins will implement the following interface.

The HTML string returned by the `render` method will be embedded in the GUI by the framework.

```java
public interface VisualizationPlugin {

    /**
     * Get the name of the visualization plugin to show on the GUI
     *
     * @return name of the visualization plugin
     */
    String getName();

    /**
     * Called (only once) when the plugin is first registered with the
     * framework, giving the plugin a chance to perform any set-up during
     * plugin registration
     *
     * @param framework The {@link Framework} instance with which the plugin
     *                  was registered.
     */
    void onRegister(Framework framework);

    /**
     * Requirement of the plugin on the data
     *
     * @param symbolCount   number of unique symbols in the data
     * @return boolean      indicating whether the number of symbols is
     *                      supported by the plugin
     */
    boolean isSupported(int symbolCount);

    /**
     * Render the corresponding char using Plotly
     *
     * @param data   a list of aggregated stock quotes
     * @return       HTML that will be embedded in the web-based GUI
     */
    String render(List<AggregatedStockQuotes> data);

}
```

