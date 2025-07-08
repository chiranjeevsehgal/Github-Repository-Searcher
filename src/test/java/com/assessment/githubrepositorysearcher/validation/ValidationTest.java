package com.assessment.githubrepositorysearcher.validation;

import com.assessment.githubrepositorysearcher.dto.SearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    private Validator validator;

    /**
     * Set up Bean Validation validator before each test
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test that valid SearchRequest passes all validation constraints
     * Verifies that properly formatted request with all required fields is accepted
     */
    @Test
    public void testValidSearchRequest() {
        SearchRequest request = new SearchRequest("spring boot", "Java", "stars");

        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    /**
     * Test that null query field triggers validation error
     * Verifies @NotNull constraint on query field works correctly
     */
    @Test
    public void testNullQuery() {
        SearchRequest request = new SearchRequest(null, "Java", "stars");

        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    /**
     * Test that empty query field triggers validation error
     * Verifies @NotBlank constraint on query field works correctly
     */
    @Test
    public void testEmptyQuery() {
        SearchRequest request = new SearchRequest("", "Java", "stars");

        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}