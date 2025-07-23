package com.blog.blogapi.service;

import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testAddPost(){
        //Arrange
        BlogPost newPost = new BlogPost();
        newPost.setTitle("New Post");

        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(newPost);

        //Act
        blogService.addPost(newPost);

        //Assert
        verify(blogPostRepository, times(1)).save(newPost);
    }

    @Test
    public void testDeletePost(){
        //Arrange
        BlogPost mockPost = new BlogPost();
        mockPost.setId(1L);
        mockPost.setTitle("Post to delete");

        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        //Act
        blogService.deletePost(1L);

        //Assert
        verify(blogPostRepository, times(1)).findById(1L);
        verify(blogPostRepository, times(1)).delete(mockPost);
    }

    @Test
    public void testSearchPostByTitle(){
        //Arrange
        String keyword = "Java";
        BlogPost post1 = new BlogPost();
        post1.setId(1L);
        post1.setTitle("Java Basics");

        BlogPost post2 = new BlogPost();
        post2.setId(2L);
        post2.setTitle("Advanced Java");

        List<BlogPost> mockPosts = Arrays.asList(post1, post2);

        when(blogPostRepository.findByTitleContainingIgnoreCase(keyword)).thenReturn(mockPosts);

        //Act
        List<BlogPost> results = blogService.searchPostByTitle(keyword);

        //Assert
        assertEquals(2, results.size());
        assertTrue(results.get(0).getTitle().contains("Java"));
        assertTrue(results.get(1).getTitle().contains("Java"));
        verify(blogPostRepository, times(1)).findByTitleContainingIgnoreCase(keyword);
    }

    @Test
    public void testAssignAuthor(){
        //Arrange
        Long postId = 1L;
        Long authorId = 2L;

        BlogPost post = new BlogPost();
        post.setId(postId);

        Author author = new Author();
        author.setId(authorId);
        author.setName("Joe Doe");

        when(blogPostRepository.findById(postId)).thenReturn(Optional.of(post));
        when(authorService.getAuthorById(authorId)).thenReturn(author);
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(post);

        //Act
        BlogPost updatedPost = blogService.assignAuthor(postId, authorId);

        //Assert
        assertNotNull(updatedPost);
        assertEquals(author, updatedPost.getAuthor());
        verify(blogPostRepository, times(1)).findById(postId);
        verify(authorService, times(1)).getAuthorById(authorId);
        verify(blogPostRepository, times(1)).save(post);
    }

    @Test
    public void testAssignCategoryToPost(){
        //Arrange
        Long postId = 1L;
        Long categoryId = 10L;

        BlogPost post = new BlogPost();
        post.setId(postId);
        post.setCategories(new ArrayList<>());

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Technology");

        when(blogPostRepository.findById(postId)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(blogPostRepository.save(any(BlogPost.class))).thenReturn(post);

        //Act
        BlogPost updatedPost = blogService.assignCategoryToPost(postId, categoryId);

        //Assert
        assertNotNull(updatedPost);
        assertTrue(updatedPost.getCategories().contains(category));
        verify(blogPostRepository, times(1)).findById(postId);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(blogPostRepository, times(1)).save(post);
    }

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
