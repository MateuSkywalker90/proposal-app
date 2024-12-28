package com.mateus.proposta_app.controller;


import com.mateus.proposta_app.dto.ProposalRequestDto;
import com.mateus.proposta_app.dto.ProposalResponseDto;
import com.mateus.proposta_app.service.ProposalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/proposal")
public class ProposalController {

    private ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalResponseDto> create(@RequestBody ProposalRequestDto requestDto){
        ProposalResponseDto response = proposalService.create(requestDto);
        return ResponseEntity.ok(response);
    }
}
