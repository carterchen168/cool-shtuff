package ngrams;

import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (Integer entry : ts.keySet()) {
            int year = entry;
            if (year >= startYear && year <= endYear) {
                this.put(year, ts.get(year));
            }
        }

    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        return new ArrayList<>(this.values());
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries thing = new TimeSeries();
        for (Integer thingy : this.keySet()) {
            double tsValue = 0;
            int year = thingy;
            double thisValue = this.get(year);
            
            if (ts.containsKey(year)) {
                tsValue = ts.get(year);
            }
            thing.put(year, thisValue + tsValue);
        }

        for (Integer other : ts.keySet()) {
            int year = other;
            double tsValue = ts.get(year);
            if (!this.containsKey(year)) {
                thing.put(year, tsValue);
            }
        }
        return thing;
    }
    

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries series = new TimeSeries();

        for (Integer thingy : this.keySet()) {
            int year = thingy;
            if (!ts.containsKey(year)) {
                throw new IllegalArgumentException("TS is missing a year!");
            }
            series.put(year, (this.get(year) / ts.get(year)));
        }
        return series;

        /** for (Map.Entry<Integer, Double> this_thingy : this.entrySet()) {
            int year = this_thingy.getKey();
            if (ts.containsKey(year)) {
                series.put(year, (this.get(year) / ts.get(year)));
            } else {
                throw new IllegalArgumentException("TS is missing a year!");
            }
        }
        return series; */
    }

}
