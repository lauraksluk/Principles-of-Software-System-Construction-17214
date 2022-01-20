package edu.cmu.cs.cs214.hw6.plugins.dataRecord;

/**
 * Class holding state and date information to match with its corresponding data field values.
 */
public class Groupby {
    private String date;
    private String state;

    /**
     * Constructor to create a Groupby instance, with the corresponding date and state to store.
     * @param date the date to store
     * @param state the state to store
     */
    public Groupby(String date, String state) {
        this.date = date;
        this.state = state;
    }

    /**
     * Obtains the date stored.
     * @return the date as a String
     */
    public String getDate() {
        return date;
    }

    /**
     * Obtains the state stored.
     * @return the state as a String
     */
    public String getState() {
        return state;
    }

    /**
     * Overrides the hashCode() method to allow for unique mapping when hashing.
     * @return the hash value
     */
    @Override
    public int hashCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(state);
        stringBuilder.append(date);
        return stringBuilder.toString().hashCode();
    }

    /**
     * Overrides the equals() method to allow for unique mapping when hashing as an object.
     * @param obj the object to compare
     * @return boolean, depending on if parameter is equal
     */
    @Override
    public boolean equals(Object obj) {
        Groupby other = (Groupby) obj;
        if (other.getState().equals(this.state) && other.getDate().equals(this.date)) {
            return true;
        }
        return false;
    }

    /**
     * String representation of Groupby instance. Returns stored values, including state and date.
     * @return the String representation
     */
    @Override
    public String toString() {
        return "Groupby{" +
                "state='" + state + '\'' +
                ", date=" + date +
                '}';
    }
}
