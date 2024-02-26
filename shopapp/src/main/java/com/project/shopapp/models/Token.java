package com.project.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "token", nullable = false, length = 255)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;


    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    @Column(name = "exppired", nullable = false)
    private Boolean expired;


    @Column(name = "revoked", nullable = false)
    private Boolean revoked;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
