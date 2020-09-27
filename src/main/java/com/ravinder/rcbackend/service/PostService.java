package com.ravinder.rcbackend.service;

import com.ravinder.rcbackend.dto.PostRequest;
import com.ravinder.rcbackend.dto.PostResponse;
import com.ravinder.rcbackend.exception.PostNotFoundException;
import com.ravinder.rcbackend.exception.SpringRedditException;
import com.ravinder.rcbackend.exception.SubredditNotFoundException;
import com.ravinder.rcbackend.mapper.PostMapper;
import com.ravinder.rcbackend.model.Post;
import com.ravinder.rcbackend.model.Subreddit;
import com.ravinder.rcbackend.model.User;
import com.ravinder.rcbackend.repository.PostRepository;
import com.ravinder.rcbackend.repository.SubredditRepository;
import com.ravinder.rcbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository
                .findByName(postRequest.getSubredditName())
                .orElseThrow(()-> new SpringRedditException(postRequest.getSubredditName() + " not found"));
        User user = authService.getCurrentUser();
        postRepository.save(postMapper.map(postRequest,subreddit, user));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }


}
