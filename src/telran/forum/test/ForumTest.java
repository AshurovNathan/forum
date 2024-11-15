package telran.forum.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.forum.dao.Forum;
import telran.forum.dao.ForumImpl;
import telran.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ForumTest {
    private final LocalDateTime now = LocalDateTime.now();
    private Forum forum;
    private Post[] posts;
    private final Comparator<Post> comparator = (p1,p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res != 0 ? res : Integer.compare(p1.getPostId(),p2.getPostId());
    };

    @BeforeEach
    void setUp(){
        forum = new ForumImpl();
        posts = new Post[6];

        posts[0] = new Post(1, "Title1", "Author1", "Content1");
        posts[1] = new Post(2, "Title2", "Author2", "Content2");
        posts[2] = new Post(3, "Title3", "Author2", "Content3");
        posts[3] = new Post(4, "Title4", "Author1", "Content4");
        posts[4] = new Post(5, "Title5", "Author3", "Content5");
        posts[5] = new Post(6, "Title6", "Author1", "Content6");

        posts[0].setDate(now.minusDays(7));
        posts[1].setDate(now.minusDays(6));
        posts[2].setDate(now.minusDays(5));
        posts[3].setDate(now.minusDays(4));
        posts[4].setDate(now.minusDays(3));
        posts[5].setDate(now.minusDays(2));


        for (int i = 0; i < posts.length; i++) {
            forum.addPost(posts[i]);
        }
    }

    @Test
    void addPost() {
        assertFalse(forum.addPost(null));
        assertFalse(forum.addPost(posts[0]));
        Post post = new Post(7,"title7","Author1","Content7");
        assertTrue(forum.addPost(post));
        assertEquals(7,forum.size());
    }

    @Test
    void removePost() {
        assertTrue(forum.removePost(2));
        assertEquals(5,forum.size());
        assertFalse(forum.removePost(12));
    }

    @Test
    void updatePost() {
        assertTrue(forum.updatePost(1,"asd"));
        assertEquals("asd", forum.getPostById(1).getContent());
        assertFalse(forum.updatePost(13,"zxc"));
    }

    @Test
    void getPostById() {
        assertEquals(posts[0], forum.getPostById(1));
        assertNull(forum.getPostById(9));
    }

    @Test
    void testGetPostsByAuthor() {
        Post[] actual = forum.getPostsByAuthor("Author1");
        Arrays.sort(actual,comparator);
        Post[] expected = {posts[0],posts[3],posts[5]};
        assertArrayEquals(expected,actual);
    }

    @Test
    void testGetPostsByAuthorWithDate() {
        LocalDate localDate = LocalDate.now();
        Post[] actual = forum.getPostsByAuthor("Author1",localDate.minusDays(6),localDate.minusDays(3));
        Arrays.sort(actual,comparator);
        Post[] expected = {posts[3]};
        assertArrayEquals(expected,actual);
    }

    @Test
    void testSize() {
        assertEquals(6, forum.size());
    }
}