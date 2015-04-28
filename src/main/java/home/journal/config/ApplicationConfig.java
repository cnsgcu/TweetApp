package home.journal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ApplicationConfig
{
    final static private Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Bean
    public ScheduledExecutorService scheduledExecutorService()
    {
        LOGGER.info("Create thread pool of 4 threads.");

        return Executors.newScheduledThreadPool(4);
    }
}
