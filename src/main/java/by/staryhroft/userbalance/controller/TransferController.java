package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.TransferRequest;
import by.staryhroft.userbalance.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public void transfer(Authentication authentication,
                         @Valid @RequestBody TransferRequest request) {
        Long fromUserId = (Long) authentication.getPrincipal();
        transferService.transferMoney(fromUserId, request.getToUserId(), request.getValue());
    }
}
