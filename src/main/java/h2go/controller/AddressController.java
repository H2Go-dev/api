package h2go.controller;

import h2go.dto.request.AddressRequest;
import h2go.dto.response.AddressRetrievalResponse;
import h2go.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressRetrievalResponse> getMyAddresses(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return addressService.getMyAddresses(userDetails.getUsername());
    }

    @PostMapping
    public ResponseEntity<AddressRetrievalResponse> createAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddressRequest addressRequest
    ) {
        AddressRetrievalResponse createdAddress = addressService.addAddress(userDetails.getUsername(), addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressRetrievalResponse> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest addressDetails
    ) {
        AddressRetrievalResponse updatedAddress = addressService.updateAddress(
                userDetails.getUsername(),
                addressId,
                addressDetails
        );
        return ResponseEntity.ok(updatedAddress);
    }

}
