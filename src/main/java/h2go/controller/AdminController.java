package h2go.controller;

import h2go.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/approve/provider/{id}")
    public ResponseEntity<String> approveProvider(
            @PathVariable("id")
            String id,
            @RequestParam(defaultValue = "true")
            Boolean approve
    ) {
        adminService.approveProvider(id, approve);
        return ResponseEntity.ok().build();
    }
}
