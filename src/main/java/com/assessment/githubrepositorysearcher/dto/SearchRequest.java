package com.assessment.githubrepositorysearcher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for GitHub repository search operations
 * Contains search parameters and validation rules
 */
public class SearchRequest {
    
    @NotNull(message = "Query cannot be null")
    @NotBlank(message = "Query cannot be blank") 
    private String query;
    
    private String language;
    
    private String sort = "stars"; // Default sort by stars
    
    public SearchRequest() {}
    
    public SearchRequest(String query, String language, String sort) {
        this.query = query;
        this.language = language;
        this.sort = sort;
    }
    
    // Getters and Setters
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getSort() {
        return sort;
    }
    
    public void setSort(String sort) {
        this.sort = sort;
    }
}