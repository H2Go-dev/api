package h2go.mapper;

import h2go.dto.request.ProductCreationalRequest;
import h2go.dto.response.ProductResponse;
import h2go.model.Product;
import h2go.model.enums.RegistrationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = RegistrationStatus.class)
public interface ProductMapper {

    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "id", ignore = true)

    Product toEntity(ProductCreationalRequest productCreationalRequest);

    ProductResponse toDto(Product product);

    List<ProductResponse> toDtoList(List<Product> products);

}
