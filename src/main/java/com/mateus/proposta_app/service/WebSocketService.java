package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void notify(ProposalResponseDto responseDto) {
        template.convertAndSend("/propostas", responseDto);
    }
}
