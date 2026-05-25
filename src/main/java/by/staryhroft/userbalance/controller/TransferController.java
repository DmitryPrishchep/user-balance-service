package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.TransferRequest;
import by.staryhroft.userbalance.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfer")
@Tag(name = "Переводы", description = "Перевод средств между пользователями")
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(summary = "Перевести деньги",
            description = "Отправитель определяется по JWT-токену, " +
                    "получатель и сумма передаются в теле запроса")
    @PostMapping
    public void transfer(Authentication authentication,
                         @Valid @RequestBody TransferRequest request) {
        Long fromUserId = (Long) authentication.getPrincipal();
        log.info("Запрос на перевод: от {} к {} суммы {}", fromUserId, request.getToUserId(), request.getValue());
        transferService.transferMoney(fromUserId, request.getToUserId(), request.getValue());
    }
}