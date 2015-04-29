package home.journal.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface TweetService
{
    long getTweetCount(LocalDateTime start, LocalDateTime end);
}
