package com.mateus.proposta_app.listener;

import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.mapper.ProposalMapper;
import com.mateus.proposta_app.repository.ProposalRepository;
import com.mateus.proposta_app.service.WebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConcludedProposalListener {

    private ProposalRepository proposalRepository;

    private WebSocketService webSocketService;

    @RabbitListener(queues = "${rabbitmq.queue.concluded.proposal}")
    public void concludedProposal(Proposals proposals) {
        proposalUpdate(proposals);
        webSocketService.notify(ProposalMapper.INSTANCE.convertEntityToDto(proposals));
    }

    private void proposalUpdate(Proposals proposals) {
        proposalRepository.proposalUpdate(proposals.getId(), proposals.getAprovada(), proposals.getObservacao());
    }
}
