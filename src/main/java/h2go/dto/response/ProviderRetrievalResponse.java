package h2go.dto.response;

import h2go.model.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProviderRetrievalResponse(
        String id,
        UserRetrievalResponse user,
        String businessName,
        String serviceCity,
        RegistrationStatus registrationStatus,
        LocalDateTime deletedAt,
        List<ProductResponse> products
) {
}
