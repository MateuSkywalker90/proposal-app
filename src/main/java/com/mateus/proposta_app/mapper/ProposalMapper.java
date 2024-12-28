package com.mateus.proposta_app.mapper;

import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.entity.Proposals;
import org.mapstruct.Mapper;

@Mapper
public interface ProposalMapper {

    Proposals proposalToDto(ProposalRequestDto proposalRequestDto);
}
