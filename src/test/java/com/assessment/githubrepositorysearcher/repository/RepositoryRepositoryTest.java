package com.assessment.githubrepositorysearcher.repository;

import com.assessment.githubrepositorysearcher.entity.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepositoryRepositoryTest {

        @Autowired
        private RepositoryRepository repositoryRepository;

        /**
         * Test basic save and find operations
         * Verifies that repository entities can be persisted and retrieved by ID
         */
        @Test
        public void testSaveAndFindById() {
                // Given
                Repository repository = new Repository(
                                1L, "test-repo", "Test description",
                                "testowner", "Java", 100, 50, LocalDateTime.now());

                // When
                repositoryRepository.save(repository);
                Optional<Repository> found = repositoryRepository.findById(1L);

                // Then
                assertTrue(found.isPresent());
                assertEquals("test-repo", found.get().getName());
                assertEquals("Java", found.get().getLanguage());
        }

        /**
         * Test language filtering query
         * Verifies that repositories can be filtered by programming language
         */
        @Test
        public void testFindByLanguage() {
                // Given
                Repository javaRepo = new Repository(
                                1L, "java-repo", "Java description",
                                "owner1", "Java", 100, 50, LocalDateTime.now());
                Repository pythonRepo = new Repository(
                                2L, "python-repo", "Python description",
                                "owner2", "Python", 200, 100, LocalDateTime.now());

                repositoryRepository.save(javaRepo);
                repositoryRepository.save(pythonRepo);

                // When
                List<Repository> javaRepos = repositoryRepository.findByLanguage("Java");

                // Then
                assertEquals(1, javaRepos.size());
                assertEquals("Java", javaRepos.get(0).getLanguage());
        }

        /**
         * Test minimum stars filtering query
         * Verifies that repositories can be filtered by minimum star count
         */
        @Test
        public void testFindByStarsGreaterThanEqual() {
                // Given
                Repository lowStarRepo = new Repository(
                                1L, "low-star", "Low stars",
                                "owner1", "Java", 50, 25, LocalDateTime.now());
                Repository highStarRepo = new Repository(
                                2L, "high-star", "High stars",
                                "owner2", "Java", 500, 250, LocalDateTime.now());

                repositoryRepository.save(lowStarRepo);
                repositoryRepository.save(highStarRepo);

                // When
                List<Repository> highStarRepos = repositoryRepository.findByStarsGreaterThanEqual(100);

                // Then
                assertEquals(1, highStarRepos.size());
                assertTrue(highStarRepos.get(0).getStars() >= 100);
        }

        /**
         * Test complex filtering query with multiple parameters
         * Verifies that repositories can be filtered by language, minimum stars, and
         * sorted
         * Tests the main query used by the service layer
         */
        @Test
        public void testFindRepositoriesWithFilters() {
                // Given
                Repository repo1 = new Repository(
                                1L, "repo1", "Description 1",
                                "owner1", "Java", 100, 50, LocalDateTime.now());
                Repository repo2 = new Repository(
                                2L, "repo2", "Description 2",
                                "owner2", "Python", 200, 100, LocalDateTime.now());

                repositoryRepository.save(repo1);
                repositoryRepository.save(repo2);

                // When
                List<Repository> result = repositoryRepository.findRepositoriesWithFilters("Java", 50, "stars");

                // Then
                assertEquals(1, result.size());
                assertEquals("Java", result.get(0).getLanguage());
                assertTrue(result.get(0).getStars() >= 50);
        }
}