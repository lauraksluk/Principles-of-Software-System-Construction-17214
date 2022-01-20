package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

/**
 * Class holding information (data) obtained relating to COVID vaccines.
 * Raw data should have been obtained from the JSON data plugin implementation.
 */
public class VaccineRecord {
    private Groupby groupby;
    private long distributed;
    private long distributedJohnson;
    private long distributedPfizer;
    private long distributedModerna;
    private long administered;
    private long administeredJohnson;
    private long administeredPfizer;
    private long administeredModerna;

    /**
     * Constructor for the VaccineRecord class. Initializes data fields.
     * @param groupby the Groupby instance to store, includes date and state information
     * @param distributed the number of vaccines distributed, for the corresponding Groupby (state, date)
     * @param distributedJohnson the number of Johnson & Johnson vaccines distributed, for the corresponding Groupby (state, date)
     * @param distributedPfizer the number of Pfizer vaccines distributed, for the corresponding Groupby (state, date)
     * @param distributedModerna the number of Moderna vaccines distributed, for the corresponding Groupby (state, date)
     * @param administered the number of vaccines administered, for the corresponding Groupby (state, date)
     * @param administeredJohnson the number of Johnson & Johnson vaccines administered, for the corresponding Groupby (state, date)
     * @param administeredPfizer the number of Pfizer vaccines administered, for the corresponding Groupby (state, date)
     * @param administeredModerna the number of Moderna vaccines administered, for the corresponding Groupby (state, date)
     */
    public VaccineRecord(Groupby groupby, long distributed, long distributedJohnson, long distributedPfizer, long distributedModerna, long administered, long administeredJohnson, long administeredPfizer, long administeredModerna){
        this.groupby = groupby;
        this.distributed = distributed;
        this.distributedJohnson = distributedJohnson;
        this.distributedPfizer = distributedPfizer;
        this.distributedModerna = distributedModerna;
        this.administered = administered;
        this.administeredJohnson = administeredJohnson;
        this.administeredPfizer = administeredPfizer;
        this.administeredModerna = administeredModerna;
    }

    /**
     * Obtains the stored Groupby instance, including state and date information.
     * @return the Groupby instance
     */
    public Groupby getGroupby(){
        return this.groupby;
    }

    /**
     * Obtains the stored value for the number of vaccines distributed.
     * @return the number of vaccines distributed
     */
    public long getDistributed(){
        return this.distributed;
    }

    /**
     * Obtains the stored value for the number of Johnson & Johnson vaccines distributed.
     * @return the number of Johnson & Johnson vaccines distributed
     */
    public long getDistributedJohnson(){
        return this.distributedJohnson;
    }

    /**
     * Obtains the stored value for the number of Pfizer vaccines distributed.
     * @return the number of Pfizer vaccines distributed
     */
    public long getDistributedPfizer(){
        return this.distributedPfizer;
    }

    /**
     * Obtains the stored value for the number of Moderna vaccines distributed.
     * @return the number of Moderna vaccines distributed
     */
    public long getDistributedModerna(){
        return this.distributedModerna;
    }

    /**
     * Obtains the stored value for the number of vaccines administered.
     * @return the number of vaccines administered
     */
    public long getAdministered(){
        return this.administered;
    }

    /**
     * Obtains the stored value for the number of Johnson & Johnson vaccines administered.
     * @return the number of Johnson & Johnson vaccines administered
     */
    public long getAdministeredJohnson(){
        return this.administeredJohnson;
    }

    /**
     * Obtains the stored value for the number of Pfizer vaccines administered.
     * @return the number of Pfizer vaccines administered
     */
    public long getAdministeredPfizer(){
        return this.administeredPfizer;
    }

    /**
     * Obtains the stored value for the number of Moderna vaccines administered.
     * @return the number of Moderna vaccines administered
     */
    public long getAdministeredModerna(){
        return this.administeredModerna;
    }

    /**
     * Returns the String representation of the HospitalRecord, includes value
     * of each stored field.
     * @return the String representation
     */
    @Override
    public String toString(){
        return "VaccineRecord{" +
                "grouby=" + groupby +
                ", distributed=" + distributed +
                ", distributedJohnson=" + distributedJohnson +
                ", distributedPfizer=" + distributedPfizer +
                ", distributedModerna=" + distributedModerna +
                ", administered=" + administered +
                ", administeredJohnson=" + administeredJohnson +
                ", administeredPfizer=" + administeredPfizer +
                ", administeredModerna=" + administeredModerna +
                "}";

    }

}
