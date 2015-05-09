package home.journal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        LOGGER.info("Create thread pool of 5 threads.");

        return Executors.newScheduledThreadPool(5);
    }

    @Bean
    public Gson gson()
    {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                .create();
    }
}
