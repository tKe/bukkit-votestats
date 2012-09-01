package uk.co.karn.votestats;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.vexsoftware.votifier.model.VotifierEvent;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.PersistenceException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.karn.votestats.model.Vote;
import uk.co.karn.votestats.model.VoterStats;
import uk.co.karn.votestats.rest.StatisticsSource;
import uk.co.karn.votestats.rest.WebServer;

/**
 *
 * @author tKe
 */
public class VoteStatsPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger(VoteStatsPlugin.class.getName());
    private final ConfHelper conf = new ConfHelper(this);
    private final VotifierEventListener listener = new VotifierEventListener();
    private Closeable webserver;
    
    static {
        LOGGER.setFilter(new Filter() {
            @Override
            public boolean isLoggable(LogRecord record) {
                record.setMessage("[VoteStats] " + record.getMessage());
                if(record.getParameters() != null) {
                    record.setMessage(MessageFormat.format(record.getMessage(), record.getParameters()));
                }
                return true;
            }
        });
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        return ImmutableList.<Class<?>>of(Vote.class, VoterStats.class);
    }

    @Override
    public void onEnable() {
        initDatabase();
        initConfig();
        replayLog();
        getServer().getPluginManager().registerEvents(listener, this);
        startWebServer();
    }

    @Override
    public void onDisable() {
        Closeables.closeQuietly(webserver);
        webserver = null;
    }

    private void initConfig() {
        getConfig().addDefault("webserver.host", getServer().getIp());
    }

    private StatisticsSource createStatsSource() {
        final EbeanServerStatisticsSource statsSource = new EbeanServerStatisticsSource(getDatabase());
        return conf.cacheEnabled() ? new MemoizedStatisticsSource(statsSource, conf.cacheDuration(), conf.cacheDurationTimeUnit())
                : statsSource;
    }

    private void replayLog() {
        File votesLog = new File(getDataFolder(), "votes.log");
        if (votesLog.isFile()) {
            LOGGER.log(Level.INFO, "Importing {0}", votesLog);
            try {
                Integer processedCount = CharStreams.readLines(Files.newReaderSupplier(votesLog, Charset.defaultCharset()), new VoteLogProcessor(listener));
                LOGGER.log(Level.INFO, "Imported {0} legacy votes", processedCount);
            } catch (IOException ex) {
                LOGGER.warning("Failed to import votes.log");
            }
            votesLog.delete();
        }
    }

    protected void initDatabase() {
        try {
            int votes = getDatabase().find(Vote.class).findRowCount();
            LOGGER.log(Level.INFO, "Database already initialised with {0} votes", votes);
        } catch (PersistenceException e) {
            LOGGER.info("Initialising database");
            installDDL();
            copyVotifierLog();
        }
    }

    protected Closeable callWithClassloader(final ClassLoader classLoader, final Callable<Closeable> callable) throws Exception {
        final ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return callable.call();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    protected void copyVotifierLog(){
        File file = new File(getDataFolder(), "../Votifier/votes.log");
        if(file.isFile()) {
            LOGGER.info("Copying votifier logs for import");
            try {
                Files.copy(file, new File(getDataFolder(), "votes.log"));
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Failed to copy votifier logs.", ex);
            }
        }
    }

    protected void startWebServer() {
        try {
            webserver = callWithClassloader(getClassLoader(), new Callable<Closeable>() {
                @Override
                public Closeable call() throws Exception {
                    return new WebServer(createStatsSource(), conf.webserverIp(), conf.webserverPort());
                }
            });
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to start votestats web server", ex);
        }
    }

    private static class VoteLogProcessor implements LineProcessor<Integer> {

        private static final Pattern LINE_PATTERN = Pattern.compile("^Vote \\(from:(.*) username:(.*) address:(.*) timeStamp:(.*)\\)$");
        private final VotifierEventListener listener;
        private int processed = 0;

        public VoteLogProcessor(VotifierEventListener pl) {
            this.listener = pl;
        }

        @Override
        public boolean processLine(String string) throws IOException {
            final Matcher matcher = LINE_PATTERN.matcher(string);
            if (matcher.matches()) {
                com.vexsoftware.votifier.model.Vote vote = new com.vexsoftware.votifier.model.Vote();
                vote.setServiceName(matcher.group(1));
                vote.setUsername(matcher.group(2));
                vote.setAddress(matcher.group(3));
                vote.setTimeStamp(matcher.group(4));
                listener.onVote(new VotifierEvent(vote));
                processed++;
            }
            return true;
        }

        @Override
        public Integer getResult() {
            return processed;
        }
    }

    public class VotifierEventListener implements Listener {

        @EventHandler(priority = EventPriority.NORMAL)
        public void onVote(final VotifierEvent voteEvent) {
            getDatabase().insert(Vote.fromVotifierEvent(voteEvent));
        }
    }
}
