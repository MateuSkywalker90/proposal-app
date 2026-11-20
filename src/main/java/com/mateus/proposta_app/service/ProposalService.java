package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.mapper.ProposalMapper;
import com.mateus.proposta_app.repository.ProposalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProposalService {

    private static final Logger logger = LoggerFactory.getLogger(ProposalService.class);

    private final ProposalRepository proposalRepository;

    private final NotificationRabbitService notificationService;

    private final String exchange;

    public ProposalService(ProposalRepository proposalRepository,
                           NotificationRabbitService notificationRabbitService,
                           @Value("${rabbitmq.pendingproposal.exchange}") String exchange) {
        this.proposalRepository = proposalRepository;
        this.notificationService = notificationRabbitService;
        this.exchange = exchange;
    }

    public ProposalResponseDto create(ProposalRequestDto requestDto) {
        // New proposals start as not integrated (see ProposalMapper). They are only
        // marked as integrated after the event is successfully published, so a failed
        // publish is picked up and retried by ProposalWithoutIntegration.
        Proposals proposal = ProposalMapper.INSTANCE.proposalToDto(requestDto);
        Proposals savedProposal = proposalRepository.save(proposal);

        publishPendingProposal(savedProposal);

        return ProposalMapper.INSTANCE.convertEntityToDto(savedProposal);
    }

    private void publishPendingProposal(Proposals proposal) {
        try {
            notificationService.notify(proposal, exchange);
            proposal.setIntegrada(true);
            proposalRepository.save(proposal);
        } catch (RuntimeException e) {
            logger.warn("Could not publish proposal {}; it will be retried by the scheduler: {}",
                    proposal.getId(), e.getMessage());
        }
    }

    public List<ProposalResponseDto> getProposal() {
        return ProposalMapper.INSTANCE.convertListEntityToListDto(proposalRepository.findAll());
    }
}
