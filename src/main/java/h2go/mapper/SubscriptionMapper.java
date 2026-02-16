package h2go.mapper;

import h2go.dto.response.SubscriptionRetrievalResponse;
import h2go.model.Subscription;
import h2go.model.enums.SubscriptionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", imports = SubscriptionStatus.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionMapper {
    SubscriptionRetrievalResponse toDto(Subscription subscription);
}
