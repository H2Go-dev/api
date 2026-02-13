package h2go.dto.response;

import java.time.LocalDateTime;

public record ProductResponse(
        String id,
        String name,
        Double price,
        Double volume,
        Integer stock,
        String nutritionalFacts,
        LocalDateTime deletedAt
) {
}
