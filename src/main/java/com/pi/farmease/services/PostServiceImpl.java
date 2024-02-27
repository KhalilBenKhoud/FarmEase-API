package com.pi.farmease.services;

import com.pi.farmease.dao.PostRepository;
import com.pi.farmease.entities.Post;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

     private final UserService userService ;
     private final PostRepository postRepository ;

    @Override
    public void addPost(Post requestBody, Principal connected) {

        User connectedUser = userService.getCurrentUser(connected) ;
          Post post = Post.builder().user(connectedUser).date_forum(new Date())
                          .title_forum(requestBody.getTitle_forum())
                                  .description_forum(requestBody.getDescription_forum())
                  .build();


        postRepository.save(post) ;
    }

    @Override
    public void updatePost(Post post, Long id) {
       post.setId_forum(id) ;
       postRepository.save(post) ;
    }

    @Override
    public void deletePost(Long id) {

    }
}
