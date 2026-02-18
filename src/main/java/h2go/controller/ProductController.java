package h2go.controller;

import h2go.dto.request.ProductCreationalRequest;
import h2go.dto.response.ProductResponse;
import h2go.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductResponse> findAll(
            @RequestParam(defaultValue = "0")
            Integer page,
            @RequestParam(defaultValue = "20")
            Integer size
    ) {
        return productService.findAll(page, size);
    }

    @GetMapping("/{providerId}")
    public Page<ProductResponse> findByProviderIdAndDeletedAtIsNull(@PathVariable("providerId") String providerId, @RequestParam int page, @RequestParam int size) {
        return productService.findAllByProviderId(providerId, page, size);

    }

    @GetMapping("/my")
    public Page<ProductResponse> findByProviderId(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @RequestParam
            int page,
            @RequestParam
            int size
    ) {

        return  productService.findMyProducts(userDetails.getUsername(), page, size);

    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @AuthenticationPrincipal
            UserDetails userDetails,
            @RequestBody
            @Valid
            ProductCreationalRequest productCreationalRequest) {

        return productService.addProduct(productCreationalRequest, userDetails.getUsername());
    }

}
