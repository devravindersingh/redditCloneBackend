package com.ravinder.rcbackend.service;

import com.ravinder.rcbackend.dto.CommentsDto;
import com.ravinder.rcbackend.exception.PostNotFoundException;
import com.ravinder.rcbackend.mapper.CommentMapper;
import com.ravinder.rcbackend.model.Comment;
import com.ravinder.rcbackend.model.NotificationEmail;
import com.ravinder.rcbackend.model.Post;
import com.ravinder.rcbackend.model.User;
import com.ravinder.rcbackend.repository.CommentRepository;
import com.ravinder.rcbackend.repository.PostRepository;
import com.ravinder.rcbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
//    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final UserRepository userRepository;

    public void save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(()-> new PostNotFoundException("Post not found "));
//        User user = userRepository.findByUsername(commentsDto.getUsername()).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.map(commentsDto, post, user);
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + "posted a comment on your post. " + POST_URL);
        sendCommentNotification(message, post.getUser());
    }
    private void sendCommentNotification(String message, User user){
        mailService.sendMail(new NotificationEmail(user.getUsername() + "Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}


