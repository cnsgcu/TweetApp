package home.journal.util;

import home.journal.model.Tweet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TweetExtractor
{
    final static private Logger LOGGER = LoggerFactory.getLogger(TweetExtractor.class);

    final private Status source;

    final private List<Consumer<Tweet>> extractings;

    final private Map<String, String> stateCodes;

    final private Pattern hashTagMatcher = Pattern.compile("#(.+?)\\b");

    private TweetExtractor(Status source)
    {
        this.source = source;
        this.extractings = new LinkedList<>();

        stateCodes = new HashMap<>();
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
    }

    static public TweetExtractor from(Status status)
    {
        return new TweetExtractor(status);
    }

    public TweetExtractor extractLanguage()
    {
        final Consumer<Tweet> languageExtracting = tweet -> tweet.setLanguage(source.getLang());

        extractings.add(languageExtracting);

        return this;
    }

    public TweetExtractor extractDate()
    {
        final Consumer<Tweet> dateExtracting = tweet -> tweet.setTimestamp(source.getCreatedAt());

        extractings.add(dateExtracting);

        return this;
    }

    public TweetExtractor extractRetweetCount()
    {
        final Consumer<Tweet> retweetCountExtracting = tweet -> tweet.setRetweetCount(source.getRetweetCount());

        extractings.add(retweetCountExtracting);

        return this;
    }

    public TweetExtractor extractState()
    {
        final Consumer<Tweet> stateExtracting = tweet -> {
            if (source.getPlace() == null) {
                tweet.setState("");
            } else {
                switch (source.getPlace().getPlaceType()) {
                    case "city": {
                        final String state =stateCodes.get(source.getPlace().getFullName().split(",")[1].trim().toUpperCase());

                        tweet.setState(state);
                        break;
                    }
                    case "admin": {
                        final String state = source.getPlace().getFullName().split(",")[0].trim();

                        tweet.setState(state);
                        break;
                    }
                    default: {
                        tweet.setState(source.getPlace().getFullName());

                        break;
                    }
                }
            }
        };

        extractings.add(stateExtracting);

        return this;
    }

    public TweetExtractor extractTopic()
    {
        final Consumer<Tweet> topicExtracting = tweet -> {
            final Matcher matcher = hashTagMatcher.matcher(source.getText());

            if (matcher.find()) {
                tweet.setTopic(matcher.group(1));
            } else {
                extractings.clear();
            }
        };

        extractings.add(0, topicExtracting);

        return this;
    }

    public TweetExtractor extractDevice()
    {
        final Document doc = Jsoup.parse(source.getSource());
        final Consumer<Tweet> deviceExtracting = tweet -> {
            final String txt = doc.select("a").text();

            if (txt.contains("Web")) {
                tweet.setDevice("Web Client");
            } else {
                tweet.setDevice(Stream.of(txt.split(" ")).reduce((p, c) -> c).get());
            }
        };

        extractings.add(deviceExtracting);

        return this;
    }

    public TweetExtractor extractLatLon()
    {
        final Consumer<Tweet> latLongExtracting = tweet -> {
            if (source.getGeoLocation() != null) {
                tweet.setLat(source.getGeoLocation().getLatitude());
                tweet.setLon(source.getGeoLocation().getLongitude());
            }
        };

        extractings.add(latLongExtracting);

        return this;
    }

    public Tweet to(Tweet tweet)
    {
        extractings.forEach(e -> e.accept(tweet));

        return tweet;
    }
}
