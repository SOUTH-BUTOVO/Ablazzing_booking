package com.javaacademy.flat_rent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @PositiveOrZero
    private BigDecimal price;

    @Column(name = "status", columnDefinition = "advert_status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @JoinColumn(name = "apartment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Apartment apartment;

    @Column(nullable = false)
    @NotBlank(message = "Должно быть описание")
    private String description;

    @OneToMany(mappedBy = "advert", orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    public void addBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
            booking.setAdvert(this);
        }
    }

    public void removeBooking(Booking booking) {
        if (bookings.contains(booking)) {
            bookings.remove(booking);
            booking.setAdvert(null);
        }
    }

    @Override
    public String toString() {
        return "Advert{" +
                "id=" + id +
                ", price=" + price +
                ", status=" + status +
                ", apartment=" + apartment +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Advert advert)) return false;
        return id != null && id.equals(advert.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
