package home.journal.service;

import home.journal.model.DeviceCount;
import home.journal.model.LanguageCount;
import home.journal.model.TopicCount;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TweetService
{
    long getTweetCount(LocalDateTime from, LocalDateTime to);

    long getRetweetCount(LocalDateTime from, LocalDateTime to);

    long getTweetCountByState(LocalDateTime from, LocalDateTime to, String state);

    List<TopicCount> getTopNTopics(LocalDateTime from, LocalDateTime to);

    List<DeviceCount> getTopNDevices(LocalDateTime from, LocalDateTime to);

    List<LanguageCount> getTopNLanguages(LocalDateTime from, LocalDateTime to);
}
