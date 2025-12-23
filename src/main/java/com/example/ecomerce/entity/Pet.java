package com.example.ecomerce.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table (name ="pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name ="image_url")
    private String imageUrl;

    @Column (name ="name")
    private String name;

    @Column (name ="breed")
    private String breed;

    @Column(name ="color")
    private String color;

    @Column(name ="age")
    private int age;

    @Column (name ="gender")
    private String gender;

    @Column (name ="location")
    private String location;

    @Column(name ="price")
    private BigDecimal price;

    @Column (name ="description")
    private String description;



}
