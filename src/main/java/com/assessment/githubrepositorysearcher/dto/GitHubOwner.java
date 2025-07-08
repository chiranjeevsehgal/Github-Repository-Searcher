package com.assessment.githubrepositorysearcher.dto;

/**
 * DTO representing a GitHub repository owner
 */
public class GitHubOwner {
    
    private String login;
    
    public GitHubOwner() {}
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
}