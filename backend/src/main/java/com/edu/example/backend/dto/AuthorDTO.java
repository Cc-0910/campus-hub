package com.edu.example.backend.dto;

/**
 * 作者信息DTO - 符合API公约，只包含 id, nickname, avatar
 */
public class AuthorDTO {
    private Long id;
    private String nickname;
    private String avatar;

    public AuthorDTO() {
    }

    public AuthorDTO(Long id, String nickname, String avatar) {
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

