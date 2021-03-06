package com.ravinder.rcbackend.mapper;

import com.ravinder.rcbackend.dto.SubredditDto;
import com.ravinder.rcbackend.model.Post;
import com.ravinder.rcbackend.model.Subreddit;
import com.ravinder.rcbackend.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

	default Integer mapPosts(List<Post> numberOfPosts) {
		System.out.println("Mapper insider");
		return numberOfPosts.size();
	}

	@InheritInverseConfiguration
	@Mapping(target = "posts", ignore = true)
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "user", source = "user")
	Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);

}
