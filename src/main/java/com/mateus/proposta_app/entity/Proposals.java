package com.mateus.proposta_app.entity;

import jakarta.persistence.*;

@Entity
public class Proposals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double requestedAmount;

    private int paymentTerm;

    private Boolean approved;

    private boolean integrated;

    private String observation;

    @OneToOne
    @JoinColumn(name = "id_users")
    private Users users;
}
