package com.assessment.githubrepositorysearcher.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representing GitHub search API response
 * Maps to the structure returned by GitHub's repository search endpoint
 */
public class GitHubSearchResponse {
    
    @JsonProperty("total_count")
    private Integer totalCount;
    
    @JsonProperty("incomplete_results")
    private Boolean incompleteResults;
    
    private List<GitHubRepository> items;
    
    public GitHubSearchResponse() {}
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public Boolean getIncompleteResults() {
        return incompleteResults;
    }
    
    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }
    
    public List<GitHubRepository> getItems() {
        return items;
    }
    
    public void setItems(List<GitHubRepository> items) {
        this.items = items;
    }
}


