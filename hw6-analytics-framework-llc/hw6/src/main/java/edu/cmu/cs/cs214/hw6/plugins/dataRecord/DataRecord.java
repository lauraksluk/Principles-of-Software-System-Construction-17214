package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to serve as a wrapper for the different records of data (CaseRecord, HospitalRecord, VaccineRecord).
 * Includes different constructors to allow for different wrapping. Also, stores the Groupby instance
 * corresponding to each entry.
 */
public class DataRecord {
    private Groupby gb;
    private List<Long> fields;

    /**
     * Constructor for DataRecord instance where parameter is a Groupby instance.
     * @param groupby the Groupby instance
     */
    public DataRecord(Groupby groupby) {
        this.gb = groupby;
        fields = new ArrayList<>();
    }

    /**
     * Constructor for DataRecord instance where parameter is a CaseRecord.
     * @param c the CaseRecord instance
     */
    public DataRecord(CaseRecord c) {
        gb = c.getGroupby();
        fields = new ArrayList<>();
        fields.add(c.getNewCases());
        fields.add(c.getDeaths());
    }

    /**
     * Constructor for DataRecord instance where parameter is a HospitalRecord.
     * @param h the HospitalRecord instance
     */
    public DataRecord(HospitalRecord h) {
        gb = h.getGroupby();
        fields = new ArrayList<>();
        fields.add(h.getHospitalized());
        fields.add(h.getInICU());
        fields.add(h.getOnVentilator());
        fields.add(h.getRecovered());
    }

    /**
     * Constructor for DataRecord instance where parameter is a VaccineRecord.
     * @param v the VaccineRecord instance
     */
    public DataRecord(VaccineRecord v) {
        gb = v.getGroupby();
        fields = new ArrayList<>();
        fields.add(v.getDistributed());
        fields.add(v.getDistributedJohnson());
        fields.add(v.getDistributedPfizer());
        fields.add(v.getDistributedModerna());
        fields.add(v.getAdministered());
        fields.add(v.getAdministeredJohnson());
        fields.add(v.getAdministeredPfizer());
        fields.add(v.getAdministeredModerna());
    }

    /**
     * Obtains the stored Groupby instance, holding state and date information.
     * @return the Groupby instance
     */
    public Groupby getGroupBy() {
        return gb;
    }

    /**
     * Obtains the list of all data field values, including from all different kinds of record
     * (CaseRecord, HospitalRecord, VaccineRecord). Includes all data field values from all data plugins.
     * @return a list of all data field values
     */
    public List<Long> getFields() {
        return fields;
    }

    /**
     * String representation of DataRecord instance. Returns values of stored fields.
     * @return the String representation
     */
    @Override
    public String toString() {
        return "DataRecord{" +
                "gb=" + gb +
                ", fields=" + Arrays.toString(fields.toArray()) +
                '}';
    }

    /**
     * Sets the Groupby instance, holding state and date information.
     * @param gb the Groupby instance
     */
    public void setGb(Groupby gb) {
        this.gb = gb;
    }

    /**
     * Sets the list of data field values.
     * @param fields the list of data field values
     */
    public void setFields(List<Long> fields) {
        this.fields.addAll(fields);
    }
}
