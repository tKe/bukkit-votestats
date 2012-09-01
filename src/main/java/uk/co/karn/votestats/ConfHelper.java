package uk.co.karn.votestats;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.configuration.Configuration;

/**
 * encapsulates configuration values
 * @author tKe
 */
final class ConfHelper {
    
    private final VoteStatsPlugin conf;

    public ConfHelper(VoteStatsPlugin plugin) {
        this.conf = plugin;
    }
    
    public String webserverIp() {
        return getConf().getString("webserver.ip");
    }
    
    public int webserverPort() {
        return getConf().getInt("webserver.port");
    }
    
    public long cacheDuration() {
        return getConf().getLong("memoization.duration");
    }
    
    public TimeUnit cacheDurationTimeUnit() {
        try {
            return TimeUnit.valueOf(getConf().getString("memoization.unit").toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return cacheDuration() < 600 ? TimeUnit.SECONDS : TimeUnit.MILLISECONDS;
        }
    }

    public boolean cacheEnabled() {
        return getConf().getBoolean("memoization.enabled");
    }

    protected Configuration getConf() {
        return conf.getConfig();
    }
}
