package home.journal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
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

    @Bean
    public Map<String, String> stateCodes()
    {
        final Map<String, String> stateCodes = new HashMap<>();

        stateCodes.put("AL", "Alabama");
        stateCodes.put("AK", "Alaska");
        stateCodes.put("AZ", "Arizona");
        stateCodes.put("AR", "Arkansas");
        stateCodes.put("CA", "California");
        stateCodes.put("CO", "Colorado");
        stateCodes.put("CT", "Connecticut");
        stateCodes.put("DE", "Delaware");
        stateCodes.put("FL", "Florida");
        stateCodes.put("GA", "Georgia");
        stateCodes.put("HI", "Hawaii");
        stateCodes.put("ID", "Idaho");
        stateCodes.put("IL", "Illinois");
        stateCodes.put("IN", "Indiana");
        stateCodes.put("IA", "Iowa");
        stateCodes.put("KS", "Kansas");
        stateCodes.put("KY", "Kentucky");
        stateCodes.put("LA", "Louisiana");
        stateCodes.put("ME", "Maine");
        stateCodes.put("MD", "Maryland");
        stateCodes.put("MA", "Massachusetts");
        stateCodes.put("MI", "Michigan");
        stateCodes.put("MN", "Minnesota");
        stateCodes.put("MS", "Mississippi");
        stateCodes.put("MO", "Missouri");
        stateCodes.put("MT", "Montana");
        stateCodes.put("NE", "Nebraska");
        stateCodes.put("NV", "Nevada");
        stateCodes.put("NH", "New Hampshire");
        stateCodes.put("NJ", "New Jersey");
        stateCodes.put("NM", "New Mexico");
        stateCodes.put("NY", "New York");
        stateCodes.put("NC", "North Carolina");
        stateCodes.put("ND", "North Dakota");
        stateCodes.put("OH", "Ohio");
        stateCodes.put("OK", "Oklahoma");
        stateCodes.put("OR", "Oregon");
        stateCodes.put("PA", "Pennsylvania");
        stateCodes.put("RI", "Rhode Island");
        stateCodes.put("SC", "South Carolina");
        stateCodes.put("SD", "South Dakota");
        stateCodes.put("TN", "Tennessee");
        stateCodes.put("TX", "Texas");
        stateCodes.put("UT", "Utah");
        stateCodes.put("VT", "Vermont");
        stateCodes.put("VA", "Virginia");
        stateCodes.put("WA", "Washington");
        stateCodes.put("WV", "West Virginia");
        stateCodes.put("WI", "Wisconsin");
        stateCodes.put("WY", "Wyoming");

        return stateCodes;
    }
}
