package home.journal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Dashboard
{
    final static private Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    @RequestMapping("/")
    public String get()
    {
        LOGGER.info("Get dashboard page!");

        return "dashboard/page";
    }
}
