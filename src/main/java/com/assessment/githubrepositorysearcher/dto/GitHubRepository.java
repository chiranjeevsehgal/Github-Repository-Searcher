package com.assessment.githubrepositorysearcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing a GitHub repository with metadata
 * Maps to GitHub API repository response structure
 */

public class GitHubRepository {
    
    private Long id;
    private String name;
    private String description;
    private String language;
    
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;
    
    @JsonProperty("forks_count")
    private Integer forksCount;
    
    @JsonProperty("updated_at")
    private String updatedAt;
    
    private GitHubOwner owner;
    
    public GitHubRepository() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Integer getStargazersCount() {
        return stargazersCount;
    }
    
    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }
    
    public Integer getForksCount() {
        return forksCount;
    }
    
    public void setForksCount(Integer forksCount) {
        this.forksCount = forksCount;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public GitHubOwner getOwner() {
        return owner;
    }
    
    public void setOwner(GitHubOwner owner) {
        this.owner = owner;
    }
}