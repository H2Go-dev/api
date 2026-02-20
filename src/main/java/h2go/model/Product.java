package h2go.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "name can't be null")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @DecimalMin(value = "0.1", message = "price can't be zero or negative")
    private Double price;

    @DecimalMin(value = "0.1", message = "price can't be zero or negative")
    private Double volume;

    @Min(value = 0, message = "stock can't be negative")
    private Integer stock;

    private String nutritionalFacts;

    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

}
