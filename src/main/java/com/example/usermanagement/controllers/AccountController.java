package com.example.usermanagement.controllers;

import com.example.usermanagement.dto.accounts.*;
import com.example.usermanagement.entities.Account;
import com.example.usermanagement.events.publishers.emails.EmailVerificationTokenGeneratedEvent;
import com.example.usermanagement.events.publishers.emails.PasswordResetGeneratedEvent;
import com.example.usermanagement.interfaces.services.IAccountService;
import com.example.usermanagement.interfaces.services.IEmailService;
import com.example.usermanagement.interfaces.services.IEmailVerificationTokenService;
import com.example.usermanagement.interfaces.services.IPasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService accountService;
    private final IEmailVerificationTokenService emailVerificationTokenService;
    private final IPasswordResetTokenService passwordResetTokenService;
    private final ApplicationEventPublisher eventPublisher;

    // account management related
    @PostMapping
    public ResponseEntity<UUID> createAccount(@RequestBody CreateAccountDTO requestBody) {

        Account userAccount = requestBody.toEntity(null);
        accountService.encodeAndSaveAccount(userAccount);

        // generate email verification token
        String token = emailVerificationTokenService.generateEmailVerificationToken(userAccount);

        var event = new EmailVerificationTokenGeneratedEvent(this, token, userAccount.getEmail());
        eventPublisher.publishEvent(event);

        return new ResponseEntity<>(userAccount.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GeneralAccountDTO>> getAccounts(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Account> accounts = accountService.searchAndSortAccounts(email, sort, page, size, direction);
        return new ResponseEntity<>(accounts.map(GeneralAccountDTO::new), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestParam(defaultValue = "false") boolean isDetailed) {
        Account account = accountService.getMyAccount();
        return new ResponseEntity<>(isDetailed ? new DetailedAccountDTO(account) : new GeneralAccountDTO(account), HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<GeneralAccountDTO> updateMe(@RequestBody UpdateAccountDTO requestBody) {
        Account account = accountService.updateMyAccount(requestBody);
        return new ResponseEntity<>(new GeneralAccountDTO(account), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<DetailedAccountDTO> getAccount(@PathVariable UUID accountId) {
        var account = new DetailedAccountDTO(accountService.getAccountById(accountId));
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("/authorities")
    public ResponseEntity<List<AccountAuthoritiesEditResponse>> editAuthorities(@RequestBody List<EditAuthoritiesRequest> requestBody) {
        List<AccountAuthoritiesEditResponse> res = accountService.editAuthorities(requestBody);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    // email verification related
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String email = emailVerificationTokenService.consumeEmailVerificationToken(token);
        accountService.verifyAccountEmail(email);
        return new ResponseEntity<>("Email verified", HttpStatus.OK);
    }

    @PostMapping("/verify-email/resend")
    public ResponseEntity<String> resendEmailVerificationToken(@RequestParam String email) {
        Account account = accountService.getAccountByEmail(email);
        String token = emailVerificationTokenService.generateEmailVerificationToken(account);

        eventPublisher.publishEvent(new EmailVerificationTokenGeneratedEvent(this, token, email));

        return new ResponseEntity<>("Email verification token sent", HttpStatus.OK);
    }


    // password related
    @PostMapping("/reset-password/resend")
    public ResponseEntity<String> requestResetPassword(@RequestParam String email) {
        Account account = accountService.getAccountByEmail(email);
        String token = passwordResetTokenService.generatePasswordResetToken(account);

        eventPublisher.publishEvent(new PasswordResetGeneratedEvent(this, email, token));

        return new ResponseEntity<>("Password reset token sent", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> confirmResetPassword(@RequestBody ResetPasswordRequest requestBody) {
        accountService.resetPassword(requestBody.getToken(), requestBody.getNewPassword());
        return new ResponseEntity<>("Password reset", HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest requestBody) {
        accountService.changeMyPassword(requestBody.getOldPassword(), requestBody.getNewPassword());
        return new ResponseEntity<>("Password changed", HttpStatus.OK);
    }


    // special info management related
    @PostMapping("/{accountId}/identity-verification")
    public ResponseEntity<String> verifyIdentity(@RequestParam boolean verify, @PathVariable UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        accountService.verifyIdentity(verify, account);
        return new ResponseEntity<>("Identity verified", HttpStatus.OK);
    }

    @PostMapping("/{accountId}/lock-account")
    public ResponseEntity<String> lockAccount(@RequestParam boolean lock, @PathVariable UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        accountService.lockAccount(lock, account);
        return new ResponseEntity<>("Account lock status updated", HttpStatus.OK);
    }

    @PostMapping("/{accountId}/membership")
    public ResponseEntity<String> changeMembership(@RequestParam boolean member, @PathVariable UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        accountService.changeMembership(member, account);
        return new ResponseEntity<>("Membership status updated", HttpStatus.OK);
    }

}
