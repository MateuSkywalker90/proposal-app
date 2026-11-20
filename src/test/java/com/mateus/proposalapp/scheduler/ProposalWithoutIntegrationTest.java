package com.mateus.proposalapp.scheduler;

import com.mateus.proposalapp.entity.Proposal;
import com.mateus.proposalapp.repository.ProposalRepository;
import com.mateus.proposalapp.service.NotificationRabbitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProposalWithoutIntegrationTest {

    private static final String EXCHANGE = "pending-proposal.ex";

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private NotificationRabbitService notificationRabbitService;

    @Test
    void republishesPendingProposalAndMarksItAsIntegrated() {
        Proposal pending = pendingProposal();
        when(proposalRepository.findAllByIntegratedIsFalse()).thenReturn(List.of(pending));

        scheduler().findNotIntegratedProposals();

        verify(notificationRabbitService).notify(pending, EXCHANGE);
        verify(proposalRepository).save(pending);
        assertThat(pending.isIntegrated()).isTrue();
    }

    @Test
    void leavesProposalPendingWhenBrokerIsStillDown() {
        Proposal pending = pendingProposal();
        when(proposalRepository.findAllByIntegratedIsFalse()).thenReturn(List.of(pending));
        doThrow(new AmqpException("broker unavailable"))
                .when(notificationRabbitService).notify(any(Proposal.class), eq(EXCHANGE));

        scheduler().findNotIntegratedProposals();

        verify(proposalRepository, never()).save(any(Proposal.class));
        assertThat(pending.isIntegrated()).isFalse();
    }

    private ProposalWithoutIntegration scheduler() {
        return new ProposalWithoutIntegration(proposalRepository, notificationRabbitService, EXCHANGE);
    }

    private Proposal pendingProposal() {
        Proposal proposal = new Proposal();
        proposal.setId(1L);
        proposal.setRequestedAmount(10000.0);
        proposal.setPaymentTerm(24);
        proposal.setIntegrated(false);
        return proposal;
    }
}
