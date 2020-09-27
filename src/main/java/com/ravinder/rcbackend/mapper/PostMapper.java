package com.ravinder.rcbackend.mapper;

import com.ravinder.rcbackend.dto.PostRequest;
import com.ravinder.rcbackend.dto.PostResponse;
import com.ravinder.rcbackend.model.Post;
import com.ravinder.rcbackend.model.Subreddit;
import com.ravinder.rcbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createDate" , expression = "java(java.time.Instant.now())")
//    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "description", expression = "java(postRequest.getDescription())")
    @Mapping(target = "subreddit",  source = "subreddit")
    @Mapping(target = "user", source = "user")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source="postId")
    @Mapping(target = "subredditName", expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "userName", expression = "java(post.getUser().getUsername())")
    PostResponse mapToDto(Post post);

}
