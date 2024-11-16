package telran.forum.dao;

import telran.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class ForumImpl implements Forum {
    private Post[] posts;
    private int size;
    private final Comparator<Post> comparator = (p1, p2) -> {
        int res = p1.getAuthor().compareTo(p2.getAuthor());


        return res != 0 ? res : p1.getDate().compareTo(p2.getDate());
    };

    public ForumImpl() {
        posts = new Post[6];
    }

    @Override
    public boolean addPost(Post post) {
        if (post == null || getPostById(post.getPostId()) != null) {
            return false;
        }

        if (size == posts.length) {
            posts = Arrays.copyOf(posts, posts.length * 2);
        }

        int index = Arrays.binarySearch(posts, 0, size, post, comparator);
        index = index >= 0 ? index : -index - 1;

        System.arraycopy(posts, index, posts, index + 1, size - index);

        posts[index] = post;

        size++;
        return true;
    }

    @Override
    public boolean removePost(int postId) {
        for (int i = 0; i < size; i++) {
            if(posts[i].getPostId() == postId){
                System.arraycopy(posts,i + 1, posts, i, size - i - 1);
                posts[--size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePost(int postId, String content) {
        if(getPostById(postId) != null){
            getPostById(postId).setContent(content);
            return true;
        }
        return false;
    }

    @Override
    public Post getPostById(int postId) {
        for (int i = 0; i < size; i++) {
            if(posts[i].getPostId() == postId){
                return posts[i];
            }
        }
        return null;
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
    LocalDate dateFrom = LocalDate.MIN;
    LocalDate dateTo = LocalDate.MAX;
    return getPostsByAuthorByIsai(author,dateFrom,dateTo);
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
        return getPostsByAuthorByIsai(author,dateFrom,dateTo);
    }

    private Post[] getPostsByAuthorByIsai(String author, LocalDate dateFrom, LocalDate dateTo) {
        Post pattern = new Post(Integer.MIN_VALUE, "", author, "");
        pattern.setDate(dateFrom.atStartOfDay());
        int from = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        from = from >= 0 ? from : -from - 1;

        pattern.setDate(dateTo.atStartOfDay());
        int to = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        to = to >= 0 ? to : -to - 1;

        return Arrays.copyOfRange(posts, from, to);
    }

    @Override
    public int size() {
        return size;
    }
}
