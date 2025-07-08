package com.assessment.githubrepositorysearcher.dto;

import java.util.List;

/**
 * Response wrapper for repository list API endpoint
 * Contains a list of repositories returned to clients
 */
public class RepositoryListResponse {
    
    private List<RepositoryDto> repositories;
    
    public RepositoryListResponse() {}
    
    public RepositoryListResponse(List<RepositoryDto> repositories) {
        this.repositories = repositories;
    }
    
    public List<RepositoryDto> getRepositories() {
        return repositories;
    }
    
    public void setRepositories(List<RepositoryDto> repositories) {
        this.repositories = repositories;
    }
}