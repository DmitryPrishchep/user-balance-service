package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.exception.InvalidTransferException;
import by.staryhroft.userbalance.exception.UserNotFoundException;
import by.staryhroft.userbalance.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(new BigDecimal("1000.00"));
        // user не обязателен для теста логики, но может быть null

        toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("500.00"));
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        // given
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("200.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.of(toAccount));

        // when
        transferService.transferMoney(fromUserId, toUserId, amount);

        // then
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());
        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        // given
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("1500.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.of(toAccount));

        // when & then
        InvalidTransferException exception = assertThrows(InvalidTransferException.class, () ->
                transferService.transferMoney(fromUserId, toUserId, amount));

        assertEquals("Недостаточно средств", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTransferToSelf() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        InvalidTransferException exception = assertThrows(InvalidTransferException.class, () ->
                transferService.transferMoney(userId, userId, amount));

        assertEquals("Нельзя перевести деньги самому себе", exception.getMessage());
        verify(accountRepository, never()).findByUserIdWithLock(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZeroOrNegative() {
        Long fromUserId = 1L;
        Long toUserId = 2L;

        assertThrows(InvalidTransferException.class, () ->
                transferService.transferMoney(fromUserId, toUserId, BigDecimal.ZERO));
        assertThrows(InvalidTransferException.class, () ->
                transferService.transferMoney(fromUserId, toUserId, new BigDecimal("-100.00")));

        verify(accountRepository, never()).findByUserIdWithLock(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenSenderAccountNotFound() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                transferService.transferMoney(fromUserId, toUserId, amount));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenReceiverAccountNotFound() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        when(accountRepository.findByUserIdWithLock(fromUserId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByUserIdWithLock(toUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                transferService.transferMoney(fromUserId, toUserId, amount));

        verify(accountRepository, never()).save(any());
    }
}
