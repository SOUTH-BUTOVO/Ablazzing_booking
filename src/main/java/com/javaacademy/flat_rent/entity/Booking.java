package com.javaacademy.flat_rent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @JoinColumn(name = "client_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Client client;

    @JoinColumn(name = "advert_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Advert advert;

    @Column(name = "total_price", nullable = false)
    @PositiveOrZero
    private BigDecimal totalPrice;

    @AssertTrue(message = "Дата окончания должна быть после даты начала или равна")
    private boolean isEndValidStart() {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking booking)) return false;
        return id != null && id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
