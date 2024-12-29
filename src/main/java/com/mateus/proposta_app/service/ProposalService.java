package com.mateus.proposta_app.service;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.entity.Proposals;
import com.mateus.proposta_app.mapper.ProposalMapper;
import com.mateus.proposta_app.repository.ProposalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProposalService {

    private ProposalRepository proposalRepository;

    public ProposalResponseDto create(ProposalRequestDto requestDto) {
        Proposals proposals = ProposalMapper.INSTANCE.proposalToDto(requestDto);
        proposalRepository.save(proposals);
        return ProposalMapper.INSTANCE.convertEntityToDto(proposals);
    }
}
