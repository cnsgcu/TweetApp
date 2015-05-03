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

@Controller
public class Dashboard
{
    final static private Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @Autowired
    private TweetService tweetService;

    @RequestMapping("/")
    public String get()
    {
        //tweetService.getTweetCount(LocalDateTime.now(), LocalDateTime.now());
        LOGGER.info("Get dashboard page!");

        return "dashboard/page";
    }

    @RequestMapping("/statistic/tweet")
    public void tweet(HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

        try {
            final PrintWriter writer = response.getWriter();

            int frq = 0;

            while (true) {
                writer.write("data: " + frq + "\n\n");
                writer.flush();

                Thread.sleep(1000);
                frq += 1;
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

            int frq = 0;

            while (true) {
                writer.write("data: " + frq + "\n\n");
                writer.flush();

                Thread.sleep(1000);
                frq += 1;
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

    @RequestMapping("/statistic/tweet/{state}")
    public void state(@PathVariable String state, HttpServletResponse response)
    {
        response.setContentType("text/event-stream");

    }
}
