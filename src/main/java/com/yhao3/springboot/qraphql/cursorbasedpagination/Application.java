package com.yhao3.springboot.qraphql.cursorbasedpagination;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Author;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Post;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.AuthorRepository;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.PostRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PostRepository postRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// for (int i = 0; i < 20000; i++) {
		// 	Author author = new Author();
		// 	author.setFirstName("FirstName" + i);
		// 	author.setLastName("LastName" + i);
		// 	author.setEmail(i + "@gmail.com");
		// 	List<Post> posts = new LinkedList<>();
		// 	for (int j = 0; j < 3; j++) {
		// 		Post post = new Post();
		// 		post.setTitle(author.getFirstName() + author.getLastName() + " Post" + j);
		// 		post.setCreatedAt(Instant.now());
		// 		post.setAuthor(author);
		// 		posts.add(post);
		// 	}
		// 	author.setPosts(posts);
		// 	authorRepository.save(author);
		// }
		
	}

}
