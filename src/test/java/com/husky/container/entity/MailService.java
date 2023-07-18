package com.husky.container.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MailService {
    private String protocol;
    private int timeout;
}
