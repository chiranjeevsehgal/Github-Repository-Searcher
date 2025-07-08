package com.assessment.githubrepositorysearcher.controller;

import com.assessment.githubrepositorysearcher.dto.*;
import com.assessment.githubrepositorysearcher.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    /**
     * Search repositories on GitHub and save results to database
     * 
     * @param searchRequest - contains search parameters like query, language, etc.
     * @return SearchResponse containing search results and metadata
     */
    @PostMapping("/search")
    public ResponseEntity<SearchResponse> searchRepositories(
            @Valid @RequestBody SearchRequest searchRequest) {
 
        try {
            SearchResponse response = gitHubService.searchAndSaveRepositories(searchRequest);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new SearchResponse("Invalid request: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SearchResponse("Internal server error: " + e.getMessage(), null));
        }
    }

    /**
     * Retrieve stored repositories with optional filtering and sorting
     * Fetches repositories from the local database with support for:
     * - Language filtering
     * - Minimum stars filtering
     * - Sorting by different criteria (stars, name, created date)
     * 
     * @param language - optional filter by programming language
     * @param minStars - optional filter for minimum star count
     * @param sort - optional sorting criteria (default: "stars")
     * @return RepositoryListResponse containing filtered and sorted repositories
     */
    @GetMapping("/repositories")
    public ResponseEntity<RepositoryListResponse> getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false, defaultValue = "stars") String sort) {

        try {

            RepositoryListResponse response = gitHubService.getStoredRepositories(language, minStars, sort);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new RepositoryListResponse(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RepositoryListResponse(null));
        }
    }
}