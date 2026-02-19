package h2go.service;

import h2go.dto.request.ProductCreationalRequest;
import h2go.dto.response.ProductResponse;
import h2go.exception.ApiException;
import h2go.mapper.ProductMapper;
import h2go.model.Product;
import h2go.model.Provider;
import h2go.model.User;
import h2go.model.enums.RegistrationStatus;
import h2go.repository.ProductRepository;
import h2go.repository.ProviderRepository;
import h2go.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final UserRepository userRepository;

    private final ProviderRepository providerRepository;

    public Page<ProductResponse> findAll(Integer page, Integer size) {
        if (page == null || size == null || size <= 0 || page < 0 ) {
            throw new ApiException("page and size must be greater than zero", HttpStatus.BAD_REQUEST);
        }
        return productRepository.findAll(PageRequest.of(page, size)).map(productMapper::toDto);
    }

    public Page<ProductResponse> findAllByProviderId(String providerId, Integer page, Integer size) {
        if (page == null || size == null || size <= 0 || page < 0 ) {
            throw new ApiException("page and size must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        return productRepository.findByProviderIdAndDeletedAtIsNull(providerId, PageRequest.of(page, size))
                .map(productMapper::toDto);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    public Page<ProductResponse> findMyProducts(String email, Integer page, Integer size) {
        if (page == null || size == null || size <= 0 || page < 0 ) {
            throw new ApiException("page and size must be greater than zero", HttpStatus.BAD_REQUEST);
        }

        return productRepository.findByProviderUserEmailAndDeletedAtIsNull(email, PageRequest.of(page, size))
                .map(productMapper::toDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROVIDER')")
    public ResponseEntity<ProductResponse> addProduct(ProductCreationalRequest productRequest, String email) {

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("User doesn't exist", HttpStatus.UNAUTHORIZED));

        Provider provider = providerRepository.findByIdAndDeletedAtIsNull(user.getId()).orElseThrow(
                () -> new ApiException("Provider doesn't exist", HttpStatus.UNAUTHORIZED)
        );

        if (provider.getRegistrationStatus() == RegistrationStatus.PENDING) {
            throw new ApiException("Provider hasn't been approved yet.", HttpStatus.UNAUTHORIZED);
        } else if (provider.getRegistrationStatus() == RegistrationStatus.REJECTED) {
            throw new ApiException("Provider is not approved", HttpStatus.UNAUTHORIZED);
        }

        Product product = productMapper.toEntity(productRequest);
        product.setProvider(provider);

        ProductResponse productResponse = productMapper.toDto(productRepository.save(product));
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

}
