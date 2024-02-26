package com.project.shopapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product extends  BaseEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    @Column(name = "price", nullable = false)
    @Min(value = 0)
    @Max(value = 10000)
    private Float price;

    @Column(name = "thumbnail", nullable = true, length = 300)
    private String thumbnail;

    @Column(name = "description", nullable = true)
    private String description;




//  Relationships
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
