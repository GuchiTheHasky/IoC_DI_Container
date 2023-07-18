package com.husky.container.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bean {
    private String id;
    private Object value;
}



