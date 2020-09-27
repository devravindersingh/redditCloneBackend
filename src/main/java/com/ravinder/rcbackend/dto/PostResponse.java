package com.ravinder.rcbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String userName;
    private String subredditName;
    private String postName;
    private String url;
    private String description;
    private String duration;
    private boolean upVote;
    private boolean downVote;

}
