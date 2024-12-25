package com.mateus.proposta_app.entity;

import jakarta.persistence.*;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String cpf;

    private String phone;

    private Double income;

    @OneToOne(mappedBy = "users")
    private Proposals proposals;
}
