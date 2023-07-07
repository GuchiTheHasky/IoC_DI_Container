package com.husky.container.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bean {
    private Object value;
    private String id;

    public void f(){
        System.out.println("fffffffffffffffff");
    }
}



