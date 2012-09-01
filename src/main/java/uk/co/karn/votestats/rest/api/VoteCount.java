package uk.co.karn.votestats.rest.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vote-count")
public class VoteCount {

    @XmlAttribute(name = "username", required = true)
    protected String username;
    @XmlAttribute(name = "count", required = true)
    protected int count;

    /**
     * Default no-arg constructor
     * 
     */
    public VoteCount() {
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public VoteCount(final String username, final int count) {
        this.username = username;
        this.count = count;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

    public VoteCount withUsername(String value) {
        setUsername(value);
        return this;
    }

    public VoteCount withCount(int value) {
        setCount(value);
        return this;
    }

}
