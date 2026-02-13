package h2go.service;

import h2go.dto.request.ProductCreationalRequest;
import h2go.dto.response.ProductResponse;
import h2go.exception.ApiException;
import h2go.mapper.ProductMapper;
import h2go.model.Product;
import h2go.model.Provider;
import h2go.model.User;
import h2go.repository.ProductRepository;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final UserRepository userRepository;

    private final ProviderRepository providerRepository;

    public List<ProductResponse> findAll() {
        return productMapper.toDtoList(productRepository.findAll());
    }

    public ResponseEntity<ProductResponse> addProduct(ProductCreationalRequest productRequest, String email) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User doesn't exist", HttpStatus.UNAUTHORIZED));

        Provider provider = providerRepository.findByIdAndDeletedAtIsNull(user.getId()).orElseThrow(
                () -> new ApiException("Provider doesn't exist", HttpStatus.UNAUTHORIZED)
        );

        Product product = productMapper.toEntity(productRequest);
        product.setProvider(provider);

        ProductResponse productResponse = productMapper.toDto(productRepository.save(product));
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

}
