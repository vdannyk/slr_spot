package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.user.dto.UpdatePasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;


@RestController
@RequestMapping(EndpointConstants.API_PATH + "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/save")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        long id = userFacade.createUser(user);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/users/{id}").buildAndExpand(id).toUriString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAccount(@RequestParam String confirmationToken) {
        userFacade.confirmAccount(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/update")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updatePassword(username, updatePasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/update")
    public ResponseEntity<String> updateEmail(@RequestBody UserDto userDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updateEmail(username, userDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/email/update/confirm")
    public ResponseEntity<String> saveEmail(@RequestParam String confirmationToken, @PathVariable String id) {
        userFacade.saveEmail(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/name/update")
    public ResponseEntity<String> updateName(@RequestBody UserDto userDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userFacade.updateName(username, userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Set<Review>> getReviewsByUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userFacade.getReviewsByUser(id));
    }

    @GetMapping("/emails")
    public ResponseEntity<Set<String>> getUsersEmails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(userFacade.getEmails(username));
    }

    @GetMapping("/emails/allowed")
    public ResponseEntity<Set<String>> getAllowedUsersEmails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok().body(userFacade.getAllowedEmails(username, 2L));
    }
}
