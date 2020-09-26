package com.ravinder.rcbackend.service;

import com.ravinder.rcbackend.dto.SubredditDto;
import com.ravinder.rcbackend.exception.SpringRedditException;
import com.ravinder.rcbackend.mapper.SubredditMapper;
import com.ravinder.rcbackend.model.Subreddit;
import com.ravinder.rcbackend.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service 
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}
	
	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll().stream()
			.map(subredditMapper::mapSubredditToDto)
			.collect(Collectors.toList());
	}


	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository
				.findById(id).orElseThrow(()-> new SpringRedditException("No Subreddit found with " + id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
}
