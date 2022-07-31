package ru.netology.domain.entities;

import lombok.Data;

@Data
public class User {
    private final String login;
    private final String password;
    private final String status;
}
