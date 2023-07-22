package com.husky.container.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserService {
    private MailService mailService;
}
