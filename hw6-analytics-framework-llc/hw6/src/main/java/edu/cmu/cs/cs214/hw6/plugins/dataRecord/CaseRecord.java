package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

/**
 * Class holding information (data) obtained relating to new cases or deaths.
 * Raw data should have been obtained from the web-API data plugin implementation.
 */
public class CaseRecord {
    private Groupby groupby;
    private final long newCases;
    private final long deaths;

    /**
     * Constructor for the Case Record class. Initializes data fields.
     * @param groupby instance of Groupby object, holding state & date data
     * @param newCases the number of new cases for the corresponding state, date
     * @param deaths the number of deaths for the corresponding state, date
     */
    public CaseRecord(Groupby groupby, long newCases, long deaths) {
        this.groupby = groupby;
        this.newCases = newCases;
        this.deaths = deaths;
    }

    /**
     * Obtains the value of the number of new cases stored.
     * @return the number of new cases
     */
    public long getNewCases() {
        return newCases;
    }

    /**
     * Obtains the value of the number of deaths stored.
     * @return the number of deaths
     */
    public long getDeaths() {
        return deaths;
    }

    /**
     * Obtains the Groupby instance, holding state and date.
     * @return the Groupby instance
     */
    public Groupby getGroupby() {
        return groupby;
    }

    /**
     * Returns the String representation of the CaseRecord, includes value
     * of each stored field.
     * @return the String representation
     */
    @Override
    public String toString() {
        return "CaseRecord{" +
                "groupby=" + groupby +
                ", newCases=" + newCases +
                ", deaths=" + deaths +
                '}';
    }
}
