package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

/**
 * Class holding information (data) obtained relating to hospitalization.
 * Raw data should have been obtained from the CSV data plugin implementation.
 */
public class HospitalRecord {
    private Groupby g;
    private long hospitalized;
    private long inICU;
    private long onVentilator;
    private long recovered;

    /**
     * Constructor for the HospitalRecord class. Initializes data fields.
     * @param gb the Groupby instance to store, includes date and state information
     * @param h the number of patients hospitalized, for the corresponding Groupby (state, date)
     * @param ICU the number of patients in the ICU, for the corresponding Groupby (state, date)
     * @param vent the number of patients requiring a ventilator, for the corresponding Groupby (state, date)
     * @param recov the number of patients recovered, for the corresponding Groupby (state, date)
     */
    public HospitalRecord(Groupby gb, long h, long ICU, long vent, long recov) {
        this.g = gb;
        hospitalized = h;
        inICU = ICU;
        onVentilator = vent;
        recovered = recov;
    }

    /**
     * Obtains the stored Groupby instance, including state and date information.
     * @return the Groupby instance
     */
    public Groupby getGroupby() {
        return g;
    }

    /**
     * Obtains the stored value for the number of patients hospitalized.
     * @return the number of patients hospitalized
     */
    public long getHospitalized() {
        return hospitalized;
    }

    /**
     * Obtains the stored value for the number of patients in the ICU.
     * @return the number of patients in the ICU
     */
    public long getInICU() {
        return inICU;
    }

    /**
     * Obtains the stored value for the number of patients requiring a ventilator.
     * @return the number of patients requiring a ventilator
     */
    public long getOnVentilator() {
        return onVentilator;
    }

    /**
     * Obtains the stored value for the number of patients recovered.
     * @return the number of patients recovered
     */
    public long getRecovered() {
        return recovered;
    }

    /**
     * Returns the String representation of the HospitalRecord, includes value
     * of each stored field.
     * @return the String representation
     */
    @Override
    public String toString() {
        return "HospitalRecord{" +
                "groupby=" + g +
                ", hospitalized=" + hospitalized +
                ", inICU=" + inICU +
                ", onVentilator=" + onVentilator +
                ", recovered=" + recovered +
                '}';
    }

}
