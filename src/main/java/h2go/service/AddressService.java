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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public AddressRetrievalResponse addAddress(String email, AddressRequest address) {

        User user =  userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new ApiException("user not found", HttpStatus.NOT_FOUND));

        Address addressEntity = new Address();
        addressEntity.setUser(user);
        addressEntity.setAddressDetails(address.addressDetails());
        Address savedAddress = addressRepository.save(addressEntity);

        return new AddressRetrievalResponse(savedAddress.getId(), savedAddress.getAddressDetails());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AddressRetrievalResponse> getMyAddresses(String email) {
        return addressRepository.findAllByUserEmailAndDeletedAtIsNull(email).stream()
                .map(address -> new AddressRetrievalResponse(address.getId(), address.getAddressDetails()))
                .toList();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    public AddressRetrievalResponse updateAddress(String email, String addressId, AddressRequest addressDetails) {
        Address address = addressRepository.findByIdAndDeletedAtIsNull(addressId)
                .orElseThrow( () -> new ApiException("address not found", HttpStatus.NOT_FOUND));

        if (!address.getUser().getEmail().equals(email)) {
            throw new ApiException("can't update this address", HttpStatus.FORBIDDEN);
        }

        address.setAddressDetails(addressDetails.addressDetails());
        Address updatedAddress = addressRepository.save(address);

        return new AddressRetrievalResponse(updatedAddress.getId(), updatedAddress.getAddressDetails());
    }


}
