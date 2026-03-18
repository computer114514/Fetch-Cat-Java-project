package com.github.computer114514.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * userDTo，不敏感的。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    /**
     * 不敏感的id
     */
    private int id;
    /**
     * 不敏感的username
     */
    private String username;
}
