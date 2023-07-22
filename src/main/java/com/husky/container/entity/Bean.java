package com.husky.container.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bean {
    private String id;
    private Object value;
}



