package com.javaacademy.flat_rent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Должно быть название города")
    private String city;

    @Column(nullable = false)
    @NotBlank(message = "Должно быть название улицы")
    private String street;

    @Column(nullable = false)
    @NotBlank(message = "Должен быть номер дома")
    private String house;

    @Column(nullable = false)
    @Size(max = 10, message = "Максимальная длинна корпуса 10")
    private String corpus;

    @Column(name = "room_count", columnDefinition = "apartment_room_count", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomCount roomCount;

    @OneToMany(mappedBy = "apartment", orphanRemoval = true)
    private Set<Advert> adverts = new HashSet<>();

    public void addAdvert(Advert advert) {
        if (!adverts.contains(advert)) {
            adverts.add(advert);
            advert.setApartment(this);
        }
    }

    public void removeAdvert(Advert advert) {
        if (adverts.contains(advert)) {
            adverts.remove(advert);
            advert.setApartment(null);
        }
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", corpus='" + corpus + '\'' +
                ", roomCount=" + roomCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apartment apartment)) return false;
        return id != null && id.equals(apartment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
