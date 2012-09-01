package uk.co.karn.votestats.model;

import com.google.common.base.Strings;
import com.vexsoftware.votifier.model.VotifierEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Simple Vote entity pojo
 *
 * @author tKe
 */
@Entity
@Table(name = Vote.TABLE)
public class Vote {

    public static final String TABLE = "vote_log";
    public static final SimpleDateFormat MINESTATUS_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");

    private static long parseTimestamp(String timeStamp) {
        timeStamp = trim(timeStamp);
        try {
            return Long.parseLong(timeStamp) * 1000;
        } catch (NumberFormatException e) {
        }
        try {
            return MINESTATUS_TIMESTAMP.parse(timeStamp).getTime();
        } catch (ParseException e) {
        }
        return new Date().getTime();
    }

    public static Vote fromVotifierEvent(VotifierEvent event) {
        final com.vexsoftware.votifier.model.Vote evote = event.getVote();
        final Vote vote = new Vote();
        vote.setAddress(trim(evote.getAddress()));
        vote.setServiceName(trim(evote.getServiceName()));
        vote.setUsername(trim(evote.getUsername()));
        vote.setVoteTime(parseTimestamp(trim(evote.getTimeStamp())));
        return vote;
    }
    @Id @GeneratedValue
    private Long id;
    @Column
    private String serviceName;
    @Column
    private String username;
    @Column
    private String address;
    @Column
    private Long voteTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Long voteTime) {
        this.voteTime = voteTime;
    }
    
    private static String trim(String s) {
        return s != null ? s.trim() : null;
    }
}
