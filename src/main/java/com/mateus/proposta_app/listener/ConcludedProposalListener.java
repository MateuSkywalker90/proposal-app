package com.mateus.proposta_app.listener;

import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.repository.ProposalRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class ConcludedProposalListener {

    @Autowired
    private ProposalRepository proposalRepository;

    @RabbitListener(queues = "${rabbitmq.queue.concluded.proposal}")
    public void concludedProposal(Proposals proposals) {
        proposalRepository.save(proposals);
    }
}
