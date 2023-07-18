package com.husky.container.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PaymentService {
    private MailService mailService;
    private String paymentType;
}
