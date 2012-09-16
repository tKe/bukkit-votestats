package uk.co.karn.votestats.model;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

/**
 *
 * @author tKe
 */
@Entity
@Sql
public class VoterStats {
    public static RawSql QUERY = RawSqlBuilder.parse(
              "select min(username) as username, "
            + "       count(*) as count "
            + "  from " + Vote.TABLE + " "
            + " group by upper(username) "
            + " order by 2 desc, max(vote_time) desc")
            .create();
    
    public static Query<VoterStats> findIn(EbeanServer db) {
        return db.find(VoterStats.class)
            .setRawSql(QUERY)
            .orderBy().desc("count");
    }
    
    private String username;
    private Integer count;

    public String getUsername() {
        return username;
    }

    public Integer getCount() {
        return count;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
