package com.blog.blogapi.service;

import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.repository.BlogPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testGetPostById(){
        //Arrange
        BlogPost mockPost = new BlogPost();
        mockPost.setId(1L);
        mockPost.setTitle("Test post");

        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        //Act
        BlogPost result = blogService.getPostById(1L);

        //Assert
        assertNotNull(result);
        assertEquals("Test post", result.getTitle());

        verify(blogPostRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetPostById_NotFound(){
        // Arrange
        when(blogPostRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            blogService.getPostById(2L);
        });

        verify(blogPostRepository, times(1)).findById(2L);
    }
}
