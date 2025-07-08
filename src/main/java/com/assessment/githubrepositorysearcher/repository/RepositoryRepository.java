package com.assessment.githubrepositorysearcher.repository;

import com.assessment.githubrepositorysearcher.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    
     /**
     * Find repositories with optional filtering and sorting
     * Supports filtering by language and minimum stars with dynamic sorting
     */
    @Query("SELECT r FROM Repository r WHERE " +
           "(:language IS NULL OR r.language = :language) AND " +
           "(:minStars IS NULL OR r.stars >= :minStars) " +
           "ORDER BY " +
           "CASE WHEN :sort = 'stars' THEN r.stars END DESC, " +
           "CASE WHEN :sort = 'forks' THEN r.forks END DESC, " +
           "CASE WHEN :sort = 'updated' THEN r.lastUpdated END DESC")
    List<Repository> findRepositoriesWithFilters(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            @Param("sort") String sort
    );
    
     /**
     * Find repositories by programming language
     */
    @Query("SELECT r FROM Repository r WHERE r.language = :language")
    List<Repository> findByLanguage(@Param("language") String language);
    
    /**
     * Find repositories with star count greater than or equal to specified value
     */
    @Query("SELECT r FROM Repository r WHERE r.stars >= :minStars")
    List<Repository> findByStarsGreaterThanEqual(@Param("minStars") Integer minStars);
}