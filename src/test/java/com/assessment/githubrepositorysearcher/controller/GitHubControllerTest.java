package com.assessment.githubrepositorysearcher.controller;

import com.assessment.githubrepositorysearcher.dto.SearchRequest;
import com.assessment.githubrepositorysearcher.service.GitHubService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for GitHubController
 * Tests REST endpoints for GitHub repository search and retrieval operations
 * Uses MockMvc for web layer testing with mocked service layer
 */
@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test successful repository search with valid request parameters
     * Verifies that a properly formatted request returns HTTP 200 OK
     */
    @Test
    public void testSearchRepositories_ValidRequest() throws Exception {
        SearchRequest request = new SearchRequest("spring boot", "Java", "stars");

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    /**
     * Test validation failure for empty query string
     * Verifies that empty query triggers validation error and returns HTTP 400
     */
    @Test
    public void testSearchRepositories_EmptyQuery() throws Exception {
        SearchRequest request = new SearchRequest("", "Java", "stars");

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test validation failure for null query
     * Verifies that null query triggers validation error and returns HTTP 400
     */
    @Test
    public void testSearchRepositories_NullQuery() throws Exception {
        SearchRequest request = new SearchRequest(null, "Java", "stars");

        mockMvc.perform(post("/api/github/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test repository retrieval without any filter parameters
     * Verifies that endpoint works with default parameters and returns HTTP 200
     */
    @Test
    public void testGetRepositories_NoParameters() throws Exception {
        mockMvc.perform(get("/api/github/repositories"))
                .andExpect(status().isOk());
    }

    /**
     * Test repository retrieval with language filter
     * Verifies that language parameter is properly accepted and processed
     */
    @Test
    public void testGetRepositories_WithLanguage() throws Exception {
        mockMvc.perform(get("/api/github/repositories")
                .param("language", "Java"))
                .andExpect(status().isOk());
    }

     /**
     * Test repository retrieval with minimum stars filter
     * Verifies that minStars parameter is properly accepted and processed
     */
    @Test
    public void testGetRepositories_WithMinStars() throws Exception {
        mockMvc.perform(get("/api/github/repositories")
                .param("minStars", "100"))
                .andExpect(status().isOk());
    }
}