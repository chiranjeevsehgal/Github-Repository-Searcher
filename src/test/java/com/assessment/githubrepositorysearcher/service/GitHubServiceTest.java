package com.assessment.githubrepositorysearcher.service;

import com.assessment.githubrepositorysearcher.dto.RepositoryListResponse;
import com.assessment.githubrepositorysearcher.entity.Repository;
import com.assessment.githubrepositorysearcher.repository.RepositoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

    @Mock
    private RepositoryRepository repositoryRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private GitHubService gitHubService;

    private Repository testRepository;

    /**
     * Set up test data before each test
     * Creates a sample repository entity for use in test scenarios
     */
    @BeforeEach
    void setUp() {
        testRepository = new Repository(
                123456L,
                "test-repo",
                "Test repository",
                "testowner",
                "Java",
                100,
                50,
                LocalDateTime.now());
    }

    /**
     * Test repository retrieval without any filters
     * Verifies that service correctly handles default parameters and returns all
     * repositories
     */
    @Test
    public void testGetStoredRepositories_WithoutFilters() {
        // Given
        List<Repository> repositories = Arrays.asList(testRepository);
        when(repositoryRepository.findRepositoriesWithFilters(null, null, "stars"))
                .thenReturn(repositories);

        // When
        RepositoryListResponse result = gitHubService.getStoredRepositories(null, null, "stars");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRepositories().size());
        assertEquals("test-repo", result.getRepositories().get(0).getName());
    }

    /**
     * Test repository retrieval with language filter
     * Verifies that service correctly applies language filtering and returns
     * matching repositories
     */
    @Test
    public void testGetStoredRepositories_WithLanguageFilter() {
        // Given
        List<Repository> repositories = Arrays.asList(testRepository);
        when(repositoryRepository.findRepositoriesWithFilters("Java", null, "stars"))
                .thenReturn(repositories);

        // When
        RepositoryListResponse result = gitHubService.getStoredRepositories("Java", null, "stars");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRepositories().size());
        assertEquals("Java", result.getRepositories().get(0).getLanguage());
    }

    /**
     * Test repository retrieval with minimum stars filter
     * Verifies that service correctly applies star count filtering and returns
     * qualifying repositories
     */

    @Test
    public void testGetStoredRepositories_WithMinStarsFilter() {
        // Given
        List<Repository> repositories = Arrays.asList(testRepository);
        when(repositoryRepository.findRepositoriesWithFilters(null, 50, "stars"))
                .thenReturn(repositories);

        // When
        RepositoryListResponse result = gitHubService.getStoredRepositories(null, 50, "stars");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRepositories().size());
        assertTrue(result.getRepositories().get(0).getStars() >= 50);
    }

    /**
     * Test validation of invalid sort parameter
     * Verifies that service throws exception when invalid sort criteria is provided
     */
    @Test
    public void testGetStoredRepositories_InvalidSort() {
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            gitHubService.getStoredRepositories(null, null, "invalid");
        });
    }

    /**
     * Test default sort parameter handling
     * Verifies that service uses default "stars" sorting when null sort parameter
     * is provided
     */
    @Test
    public void testGetStoredRepositories_NullSort() {
        // Given
        List<Repository> repositories = Arrays.asList(testRepository);
        when(repositoryRepository.findRepositoriesWithFilters(null, null, "stars"))
                .thenReturn(repositories);

        // When
        RepositoryListResponse result = gitHubService.getStoredRepositories(null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRepositories().size());
    }
}