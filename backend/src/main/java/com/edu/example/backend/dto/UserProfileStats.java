package com.edu.example.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileStats {
    private Integer articleCount;
    private Integer fansCount;
    private Integer followingCount;
}