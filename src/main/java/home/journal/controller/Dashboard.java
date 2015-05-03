package home.journal.controller;

import home.journal.service.TweetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

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

    }

    @RequestMapping("/statistic/retweet")
    public void retweet(HttpServletResponse response)
    {

    }

    @RequestMapping("/statistic/device")
    public void device(HttpServletResponse response)
    {

    }

    @RequestMapping("/statistic/language")
    public void language(HttpServletResponse response)
    {

    }

    @RequestMapping("/statistic/topic")
    public void topic(HttpServletResponse response)
    {

    }

    @RequestMapping("/statistic/state/{name}")
    public void state(@PathVariable String name, HttpServletResponse response)
    {

    }
}
