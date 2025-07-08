package com.assessment.githubrepositorysearcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    /**
     * Configuring with GitHub API specific headers
     * Sets up default headers required by GitHub API for all HTTP requests
     * 
     * @return WebClient.Builder configured with GitHub API headers
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultHeader("User-Agent", "GitHub-Repository-Searcher");
    }
}