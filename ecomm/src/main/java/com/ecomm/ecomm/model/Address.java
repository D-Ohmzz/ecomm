package com.ecomm.ecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "addresses")
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, max = 30)
    private String address;

    @NotBlank
    @Size(min = 5, max = 30)
    private String buildingName;

    @NotBlank
    @Size(min = 5, max = 30)
    private String city;

    @NotBlank
    @Size(max = 30)
    private String country;

    public Address(String address, String buildingName, String city, String country) {
        this.address = address;
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
    }

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
