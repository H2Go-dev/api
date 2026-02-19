package h2go.service;

import h2go.dto.request.AddressRequest;
import h2go.dto.response.AddressRetrievalResponse;
import h2go.exception.ApiException;
import h2go.model.Address;
import h2go.model.User;
import h2go.repository.AddressRepository;
import h2go.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> addAddress(String email, AddressRequest address) {

        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("user not found", HttpStatus.NOT_FOUND));

        Address addressEntity = new Address();
        addressEntity.setUser(user);
        addressEntity.setAddressDetails(address.addressDetails());
        addressRepository.save(addressEntity);

        return new ResponseEntity<>("address added successfully", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AddressRetrievalResponse> getMyAddresses(String email) {
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("user not found", HttpStatus.UNAUTHORIZED));

        return user.getAddresses().stream().map(
                (e) -> {
                    return new AddressRetrievalResponse(e.getId(), e.getAddressDetails());
                }
        ).toList();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> updateAddress(String email, String addressId, AddressRequest addressDetails) {
        Address address = addressRepository.findByIdAndDeletedAtIsNull(addressId)
                .orElseThrow( () -> new ApiException("address not found", HttpStatus.NOT_FOUND));

        if (!address.getUser().getEmail().equals(email)) {
            throw new ApiException("", HttpStatus.UNAUTHORIZED);
        }

        address.setAddressDetails(addressDetails.addressDetails());
        addressRepository.save(address);

        return new ResponseEntity<>("address Modified successfully",HttpStatus.OK);
    }


}
