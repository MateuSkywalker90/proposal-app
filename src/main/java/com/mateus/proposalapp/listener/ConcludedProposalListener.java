package com.mateus.proposalapp.listener;

import com.mateus.proposalapp.entity.Proposal;
import com.mateus.proposalapp.mapper.ProposalMapper;
import com.mateus.proposalapp.repository.ProposalRepository;
import com.mateus.proposalapp.service.WebSocketService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConcludedProposalListener {

    private ProposalRepository proposalRepository;

    private WebSocketService webSocketService;

    @RabbitListener(queues = "${rabbitmq.queue.concluded.proposal}")
    public void concludedProposal(Proposal proposal) {
        updateProposal(proposal);
        webSocketService.notify(ProposalMapper.INSTANCE.toDto(proposal));
    }

    private void updateProposal(Proposal proposal) {
        proposalRepository.updateResult(proposal.getId(), proposal.getApproved(), proposal.getObservation());
    }
}
