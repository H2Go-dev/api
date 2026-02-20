package h2go.controller;

import h2go.dto.response.ProviderRetrievalResponse;
import h2go.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping
    public Page<ProviderRetrievalResponse> providers(
            @RequestParam(defaultValue = "0")
            Integer page,
            @RequestParam(defaultValue = "20")
            Integer size
    ) {
        return providerService.getApprovedProviders(page, size);
    }
}
