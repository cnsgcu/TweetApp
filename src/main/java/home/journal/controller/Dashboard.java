package home.journal.controller;

import home.journal.service.TweetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
public class Dashboard
{
    final static private Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @Autowired
    private TweetService tweetService;

    @RequestMapping("/")
    public String get()
    {
        tweetService.getTweetCount(LocalDateTime.now(), LocalDateTime.now());
        LOGGER.info("Get dashboard page!");

        return "dashboard/page";
    }

    @RequestMapping("/")
    public void frequency(HttpServletResponse response)
    {

    }
}
