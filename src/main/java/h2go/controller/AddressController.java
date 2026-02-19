package h2go.controller;

import h2go.dto.request.AddressRequest;
import h2go.dto.response.AddressRetrievalResponse;
import h2go.service.AddressService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> createAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddressRequest addressRequest
    ) {
        return addressService.addAddress(userDetails.getUsername(), addressRequest);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<String> updateAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String addressId,
            @RequestBody AddressRequest addressDetails
    ) {
        return addressService.updateAddress(userDetails.getUsername(), addressId, addressDetails);
    }

}
