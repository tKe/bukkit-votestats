package uk.co.karn.votestats.rest;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.core.ScanningResourceConfig;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.core.spi.component.ioc.IoCInstantiatedComponentProvider;
import com.sun.jersey.core.spi.scanning.PackageNamesScanner;
import com.sun.jersey.server.impl.provider.RuntimeDelegateImpl;
import com.sun.jersey.simple.container.SimpleServerFactory;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.ext.RuntimeDelegate;

/**
 *
 * @author tKe
 */
public class WebServer implements Closeable {
    private static final String PROVIDER_PACKAGE = "uk.co.karn.votestats.rest";
    private final Closeable server;

    public WebServer(final StatisticsSource stats, String host, int port) throws IOException {
            RuntimeDelegate.setInstance(new RuntimeDelegateImpl());
            ScanningResourceConfig resConf = new PackagesResourceConfig(PROVIDER_PACKAGE);
            this.server = SimpleServerFactory.create(hostUri("http", host, port), resConf, new SingletonStatsIOCProvider(stats));
    }

    @Override
    public void close() throws IOException {
        server.close();
    }
    
    private static URI hostUri(String scheme, String host, int port) {
        try {
            return new URI(scheme, null, host, port, null, null, null);
        } catch (URISyntaxException ex) {
            return URI.create(String.format("%s://%s:%s", scheme, host, port));
        }
    }

    private static class SingletonStatsIOCProvider implements IoCComponentProviderFactory {

        private final StatisticsSource stats;

        public SingletonStatsIOCProvider(StatisticsSource stats) {
            this.stats = stats;
        }

        @Override
        public IoCComponentProvider getComponentProvider(Class<?> c) {
            if(c == StatisticsSource.class) {
                return new IoCInstantiatedComponentProvider() {
                    @Override
                    public Object getInstance() {
                        return stats;
                    }

                    @Override
                    public Object getInjectableInstance(Object o) {
                        return stats;
                    }
                };
            } else {
                return null;
            }
        }

        @Override
        public IoCComponentProvider getComponentProvider(ComponentContext cc, Class<?> c) {
            return getComponentProvider(c);
        }
    }
    
}
