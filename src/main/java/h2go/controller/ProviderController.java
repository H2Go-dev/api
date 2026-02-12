package h2go.controller;

import h2go.dto.response.ProviderRetrievalResponse;
import h2go.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping
    //TODO add all the filters deleted and others
    public List<ProviderRetrievalResponse> providers() {
        return providerService.getProviders();
    }
}
