package h2go.controller;

import h2go.dto.request.ProductCreationalRequest;
import h2go.dto.response.ProductResponse;
import h2go.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
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
