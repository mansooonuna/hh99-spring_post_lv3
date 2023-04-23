// Client <-Dto-> Controller <-Dto-> Service <-Dto-> Repository <-Entity-> DB
package com.sparta.spring_post.dto;

import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.entity.Timestamped;
import com.sparta.spring_post.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto extends Timestamped {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long id;
    private Users users;
    private String title;
    private String content;

    public PostResponseDto(Post post) {
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.id = post.getId();
        this.users = post.getUsers();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
