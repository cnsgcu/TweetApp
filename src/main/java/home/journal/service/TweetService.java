package home.journal.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface TweetService
{
    long getTweetCount(LocalDateTime start, LocalDateTime end);

    long getRetweetCount(LocalDateTime start, LocalDateTime end);

    long getTweetCountInState(LocalDateTime start, LocalDateTime end, String state);

    void getTopNTopics(LocalDateTime start, LocalDateTime end);

    void getTopNDevices(LocalDateTime start, LocalDateTime end);

    void getTopNLanguages(LocalDateTime start, LocalDateTime end);
}
