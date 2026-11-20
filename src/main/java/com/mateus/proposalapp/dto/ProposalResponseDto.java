package com.mateus.proposalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String cpf;

    private String phone;

    private Double income;

    private String requestedAmountFormatted;

    private int paymentTerm;

    private Boolean approved;

    private String observation;
}
