package com.mateus.proposta_app.scheduler;

import com.mateus.proposta_app.repository.ProposalRepository;
import com.mateus.proposta_app.service.NotificationRabbitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProposalWithoutIntegration {

    private ProposalRepository proposalRepository;

    private NotificationRabbitService notificationRabbitService;

    private String exchange;

    public ProposalWithoutIntegration(ProposalRepository proposalRepository,
                                      NotificationRabbitService notificationRabbitService,
                                      @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationRabbitService = notificationRabbitService;
        this.exchange = exchange;
    }

    public void findNotIntegratedProposals() {
        proposalRepository.findAllByIntegradaIsFalse().forEach(proposals -> {
            try {
                notificationRabbitService.notify(proposals, exchange);
                proposals.setIntegrada(true);
                proposalRepository.save(proposals);
            } catch (RuntimeException e) {
                System.out.println("Not integrated.");
            }
        });
    }
}
