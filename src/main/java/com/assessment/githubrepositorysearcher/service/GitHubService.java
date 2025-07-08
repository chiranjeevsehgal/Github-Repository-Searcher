package com.assessment.githubrepositorysearcher.service;

import com.assessment.githubrepositorysearcher.dto.*;
import com.assessment.githubrepositorysearcher.entity.Repository;
import com.assessment.githubrepositorysearcher.repository.RepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    @Value("${github.api.base-url}")
    private String gitHubApiBaseUrl;

    @Value("${github.api.search-endpoint}")
    private String searchEndpoint;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * Search GitHub repositories and save results to database
     * Calls GitHub API, processes response, and stores repositories with
     * deduplication
     * 
     */
    public SearchResponse searchAndSaveRepositories(SearchRequest searchRequest) {
        try {
            // Building GitHub API URL
            String url = buildGitHubApiUrl(searchRequest);

            // Calling GitHub API
            GitHubSearchResponse gitHubResponse = callGitHubApi(url);

            // Converting and save repositories
            List<Repository> repositories = convertAndSaveRepositories(gitHubResponse);

            // Converting to DTOs
            List<RepositoryDto> repositoryDtos = repositories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return new SearchResponse(
                    "Repositories fetched and saved successfully",
                    repositoryDtos);

        } catch (Exception e) {
            throw new RuntimeException("Failed to search GitHub repositories: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieve stored repositories with optional filtering and sorting
     * Fetches repositories from database with support for language and star
     * filtering
     */
    public RepositoryListResponse getStoredRepositories(String language, Integer minStars, String sort) {
        try {

            // Setting default as sort if not provided
            if (sort == null || sort.trim().isEmpty()) {
                sort = "stars";
            }

            // Validating sort parameter
            if (!isValidSortParameter(sort)) {
                throw new IllegalArgumentException("Invalid sort parameter. Must be 'stars', 'forks', or 'updated'");
            }

            // Getting repositories from database
            List<Repository> repositories = repositoryRepository.findRepositoriesWithFilters(
                    language, minStars, sort);

            // Converting to DTOs
            List<RepositoryDto> repositoryDtos = repositories.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return new RepositoryListResponse(repositoryDtos);

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve stored repositories: " + e.getMessage(), e);
        }
    }

    /**
     * Build GitHub API search URL with query parameters
     * Constructs URL with search query, language filter, sort criteria, and
     * pagination
     */
    private String buildGitHubApiUrl(SearchRequest searchRequest) {
        StringBuilder urlBuilder = new StringBuilder(gitHubApiBaseUrl + searchEndpoint);
        urlBuilder.append("?q=").append(searchRequest.getQuery());

        if (searchRequest.getLanguage() != null && !searchRequest.getLanguage().trim().isEmpty()) {
            urlBuilder.append("+language:").append(searchRequest.getLanguage());
        }

        if (searchRequest.getSort() != null && !searchRequest.getSort().trim().isEmpty()) {
            urlBuilder.append("&sort=").append(searchRequest.getSort());
        }

        urlBuilder.append("&per_page=30"); // Limit results

        return urlBuilder.toString();
    }

    /**
     * Call GitHub API and handle common error scenarios
     * Makes HTTP request to GitHub search API with proper error handling for rate
     * limits
     */
    private GitHubSearchResponse callGitHubApi(String url) {
        try {

            WebClient webClient = webClientBuilder.build();

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(GitHubSearchResponse.class)
                    .block();

        } catch (WebClientResponseException e) {

            if (e.getStatusCode().value() == 403) {
                throw new RuntimeException("GitHub API rate limit exceeded. Please try again later.");
            } else if (e.getStatusCode().value() == 422) {
                throw new RuntimeException("Invalid search query. Please check your search parameters.");
            } else {
                throw new RuntimeException("GitHub API call failed: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to call GitHub API: " + e.getMessage(), e);
        }
    }

    /**
     * Convert GitHub API response to entities and save to database
     * Processes each repository from the API response and handles save/update logic
     */
    private List<Repository> convertAndSaveRepositories(GitHubSearchResponse gitHubResponse) {
        if (gitHubResponse == null || gitHubResponse.getItems() == null) {
            return List.of();
        }

        return gitHubResponse.getItems().stream()
                .map(this::convertToEntity)
                .map(this::saveOrUpdateRepository)
                .collect(Collectors.toList());
    }

    /**
     * Convert GitHub API repository object to database entity
     * Maps GitHub response fields to internal entity with null safety and timestamp
     * parsing
     */
    private Repository convertToEntity(GitHubRepository gitHubRepo) {
        Repository repository = new Repository();
        repository.setId(gitHubRepo.getId());
        repository.setName(gitHubRepo.getName());
        repository.setDescription(gitHubRepo.getDescription());
        repository.setOwner(gitHubRepo.getOwner() != null ? gitHubRepo.getOwner().getLogin() : "Unknown");
        repository.setLanguage(gitHubRepo.getLanguage());
        repository.setStars(gitHubRepo.getStargazersCount() != null ? gitHubRepo.getStargazersCount() : 0);
        repository.setForks(gitHubRepo.getForksCount() != null ? gitHubRepo.getForksCount() : 0);

        // Parse updated_at timestamp
        if (gitHubRepo.getUpdatedAt() != null) {
            try {
                LocalDateTime lastUpdated = LocalDateTime.parse(
                        gitHubRepo.getUpdatedAt(),
                        DateTimeFormatter.ISO_DATE_TIME);
                repository.setLastUpdated(lastUpdated);
            } catch (Exception e) {
                repository.setLastUpdated(LocalDateTime.now());
            }
        } else {
            repository.setLastUpdated(LocalDateTime.now());
        }

        return repository;
    }

    /**
     * Save new repository or update existing one in database
     * Implements upsert logic to handle duplicate repositories from GitHub API
     * Updates existing repositories with latest data while preserving database
     * timestamps
     */
    private Repository saveOrUpdateRepository(Repository repository) {
        try {
            // Checking if repository already exists
            if (repositoryRepository.existsById(repository.getId())) {
                Repository existingRepo = repositoryRepository.findById(repository.getId()).orElse(null);
                if (existingRepo != null) {
                    // Update existing repository
                    existingRepo.setName(repository.getName());
                    existingRepo.setDescription(repository.getDescription());
                    existingRepo.setOwner(repository.getOwner());
                    existingRepo.setLanguage(repository.getLanguage());
                    existingRepo.setStars(repository.getStars());
                    existingRepo.setForks(repository.getForks());
                    existingRepo.setLastUpdated(repository.getLastUpdated());
                    existingRepo.setUpdatedAt(LocalDateTime.now());
                    return repositoryRepository.save(existingRepo);
                }
            } else {
            }

            return repositoryRepository.save(repository);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save repository: " + e.getMessage(), e);
        }
    }

    /**
     * Convert repository entity to DTO for API response
     */
    private RepositoryDto convertToDto(Repository repository) {
        return new RepositoryDto(
                repository.getId(),
                repository.getName(),
                repository.getDescription(),
                repository.getOwner(),
                repository.getLanguage(),
                repository.getStars(),
                repository.getForks(),
                repository.getLastUpdated());
    }

    /**
     * Validate sort parameter against allowed values
     */
    private boolean isValidSortParameter(String sort) {
        return "stars".equals(sort) || "forks".equals(sort) || "updated".equals(sort);
    }
}