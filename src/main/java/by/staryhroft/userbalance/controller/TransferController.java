package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.TransferRequest;
import by.staryhroft.userbalance.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public void transfer(Authentication authentication,
                         @Valid @RequestBody TransferRequest request) {
        Long fromUserId = (Long) authentication.getPrincipal();
        log.info("Запрос на перевод: от {} к {} суммы {}", fromUserId, request.getToUserId(), request.getValue());
        transferService.transferMoney(fromUserId, request.getToUserId(), request.getValue());
    }
}