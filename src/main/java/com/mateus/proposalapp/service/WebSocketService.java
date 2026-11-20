package com.mateus.proposalapp.service;

import com.mateus.proposalapp.dto.ProposalResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    public void notify(ProposalResponseDto responseDto) {
        template.convertAndSend("/proposals", responseDto);
    }
}
