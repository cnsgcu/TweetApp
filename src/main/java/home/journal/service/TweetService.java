package home.journal.service;

import java.time.LocalDateTime;

public interface TweetService
{
    public long getTweetCount(LocalDateTime start, LocalDateTime end);
}
