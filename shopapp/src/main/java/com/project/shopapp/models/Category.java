package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "categories")
@Data // toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  long id;

    @Column(name = "name", nullable = false)
    private String name;
}
