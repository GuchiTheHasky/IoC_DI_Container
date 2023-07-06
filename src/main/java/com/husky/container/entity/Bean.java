package com.husky.container.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bean {
    private Object value;
    private String id;

    public void yes() {
        System.out.println("yes " + this.id);
    }
}



