package h2go.mapper;

import h2go.dto.ProviderCreationDTO;
import h2go.dto.ProviderRetrievalDTO;
import h2go.model.enums.RegistrationStatus;
import h2go.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring", imports = RegistrationStatus.class)
public interface ProviderMapper {

    @Mapping(target = "registrationStatus", expression = "java(RegistrationStatus.PENDING)")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Provider toEntity(ProviderCreationDTO providerCreationDTO);


    ProviderRetrievalDTO toDto(Provider provider);

    List<ProviderRetrievalDTO> toDtoList(List<Provider> providers);

}
