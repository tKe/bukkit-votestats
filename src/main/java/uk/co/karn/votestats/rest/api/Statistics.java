package uk.co.karn.votestats.rest.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "statistics")
public class Statistics {

    @XmlElementWrapper
    @XmlElement(name = "user")
    protected List<VoteCount> total;
    @XmlElementWrapper
    @XmlElement(name = "user")
    protected List<VoteCount> weekly;

    /**
     * Default no-arg constructor
     *
     */
    public Statistics() {
    }

    /**
     * Fully-initialising value constructor
     *
     */
    public Statistics(final List<VoteCount> total, final List<VoteCount> weekly) {
        this.total = total;
        this.weekly = weekly;
    }

    /**
     * Gets the value of the total property.
     *
     * <p> This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the total property.
     *
     * <p> For example, to add a new item, do as follows:
     * <pre>
     *    getTotal().add(newItem);
     * </pre>
     *
     *
     * <p> Objects of the following type(s) are allowed in the list
     * {@link VoteCount }
     *
     *
     */
    public List<VoteCount> getTotal() {
        if (total == null) {
            total = new ArrayList<VoteCount>();
        }
        return this.total;
    }

    /**
     * Gets the value of the weekly property.
     *
     * <p> This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the weekly property.
     *
     * <p> For example, to add a new item, do as follows:
     * <pre>
     *    getWeekly().add(newItem);
     * </pre>
     *
     *
     * <p> Objects of the following type(s) are allowed in the list
     * {@link VoteCount }
     *
     *
     */
    public List<VoteCount> getWeekly() {
        if (weekly == null) {
            weekly = new ArrayList<VoteCount>();
        }
        return this.weekly;
    }

    public Statistics withTotal(VoteCount... values) {
        return withTotal(Arrays.asList(values));
    }

    public Statistics withTotal(Collection<VoteCount> values) {
        if (values != null) {
            getTotal().addAll(values);
        }
        return this;
    }

    public Statistics withWeekly(VoteCount... values) {
        return withWeekly(Arrays.asList(values));
    }

    public Statistics withWeekly(Collection<VoteCount> values) {
        if (values != null) {
            getWeekly().addAll(values);
        }
        return this;
    }
}
