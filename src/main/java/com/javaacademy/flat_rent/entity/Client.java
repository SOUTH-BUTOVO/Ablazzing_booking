package com.javaacademy.flat_rent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Должно быть имя")
    private String name;

    @Column(columnDefinition = "citext", nullable = false, unique = true)
    @Email(message = "Должен быть email")
    @Size(max = 255, message = "Максимальная длинна email 255 символов")
    private String email;

    @OneToMany(mappedBy = "client", orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    public void addBooking(Booking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
            booking.setClient(this);
        }
    }

    public void removeBooking(Booking booking) {
        if (bookings.contains(booking)) {
            bookings.remove(booking);
            booking.setClient(null);
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return id != null && id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
