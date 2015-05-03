package home.journal.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface TweetService
{
    long getTweetCount(LocalDateTime from, LocalDateTime to);

    long getRetweetCount(LocalDateTime from, LocalDateTime to);

    long getTweetCountInState(LocalDateTime from, LocalDateTime to, String state);

    void getTopNTopics(LocalDateTime from, LocalDateTime to);

    void getTopNDevices(LocalDateTime from, LocalDateTime to);

    void getTopNLanguages(LocalDateTime from, LocalDateTime to);
}
