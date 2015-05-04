package home.journal.controller;

import home.journal.service.TweetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Random;

@Controller
public class Dashboard
{
    final static private Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @Autowired
    private TweetService tweetService;

    @RequestMapping("/")
    public String get()
    {
        LOGGER.info("Get dashboard page!");

        return "dashboard/page";
    }

    @RequestMapping("/statistic/tweet")
    public void tweet(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

        try {
            final PrintWriter writer = response.getWriter();

            final Random random = new Random();

            while (true) {
                final LocalDateTime now = LocalDateTime.now();
                final LocalDateTime secondsAgo = now.minusSeconds(15);

                final long tweetCount = tweetService.getTweetCount(secondsAgo, now);

                writer.write("data: " + tweetCount + "\n\n");
                writer.flush();

                Thread.sleep(15000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/statistic/retweet")
    public void retweet(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

        try {
            final PrintWriter writer = response.getWriter();

            while (true) {
                final LocalDateTime now = LocalDateTime.now();
                final LocalDateTime secondsAgo = now.minusSeconds(10);

                final long retweetCount = tweetService.getRetweetCount(secondsAgo, now);

                writer.write("data: " + retweetCount + "\n\n");
                writer.flush();

                Thread.sleep(10000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/statistic/tweet/{state}")
    public void state(@PathVariable String state, HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

        LOGGER.info("Tweet count of " + state.replace("_", " "));

        try {
            final PrintWriter writer = response.getWriter();

            while (true) {
                final LocalDateTime now = LocalDateTime.now();
                final LocalDateTime secondsAgo = now.minusSeconds(15);

                final long count = tweetService.getTweetCountByState(secondsAgo, now, state);

                writer.write("data: " + count + "\n\n");
                writer.flush();

                Thread.sleep(15000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/statistic/device")
    public void device(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");
    }

    @RequestMapping("/statistic/language")
    public void language(HttpServletResponse response)
    {
    }

    @RequestMapping("/statistic/topic")
    public void topic(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");
    }
}
