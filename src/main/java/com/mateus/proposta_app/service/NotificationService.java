package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private RabbitTemplate rabbitTemplate;

    public void notify(Proposals proposals, String exchange) {
        rabbitTemplate.convertAndSend(exchange, "", proposals);
    }
}
