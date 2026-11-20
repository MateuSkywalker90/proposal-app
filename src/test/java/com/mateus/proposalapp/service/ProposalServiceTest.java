package com.mateus.proposalapp.service;

import com.mateus.proposalapp.dto.ProposalRequestDto;
import com.mateus.proposalapp.entity.Proposal;
import com.mateus.proposalapp.repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {

    private static final String EXCHANGE = "pending-proposal.ex";

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private NotificationRabbitService notificationRabbitService;

    private ProposalService proposalService;

    @BeforeEach
    void setUp() {
        proposalService = new ProposalService(proposalRepository, notificationRabbitService, EXCHANGE);
        when(proposalRepository.save(any(Proposal.class))).thenAnswer(invocation -> {
            Proposal proposal = invocation.getArgument(0);
            if (proposal.getId() == null) {
                proposal.setId(1L);
            }
            return proposal;
        });
    }

    @Test
    void marksProposalAsIntegratedWhenPublishSucceeds() {
        proposalService.create(validRequest());

        verify(notificationRabbitService).notify(any(Proposal.class), eq(EXCHANGE));
        assertThat(lastSavedProposal().isIntegrated()).isTrue();
    }

    @Test
    void keepsProposalPendingForRetryWhenPublishFails() {
        doThrow(new AmqpException("broker unavailable"))
                .when(notificationRabbitService).notify(any(Proposal.class), eq(EXCHANGE));

        assertThatCode(() -> proposalService.create(validRequest())).doesNotThrowAnyException();

        Proposal saved = lastSavedProposal();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isIntegrated()).isFalse();
    }

    private Proposal lastSavedProposal() {
        ArgumentCaptor<Proposal> captor = ArgumentCaptor.forClass(Proposal.class);
        verify(proposalRepository, atLeastOnce()).save(captor.capture());
        return captor.getValue();
    }

    private ProposalRequestDto validRequest() {
        return new ProposalRequestDto("Maria", "Silva", "000.000.000-00",
                "+5592999999999", 5000.0, 10000.0, 24);
    }
}
