package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.user.dto.UpdatePasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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

    @PostAuthorize("hasViewAccess(#reviewId)")
    @GetMapping
    public ResponseEntity<Page<UserReview>> getUsersByReviewId(@RequestParam Long reviewId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok().body(userFacade.getUsersByReviewId(reviewId, page, size));
    }

    @GetMapping("/emails")
    public ResponseEntity<Set<String>> getUsersEmails() {
        String username = ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return ResponseEntity.ok().body(userFacade.getEmails(username));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmAccount(@RequestParam String confirmationToken) {
        userFacade.confirmAccount(confirmationToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/update")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
        String username = ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userFacade.updatePassword(username, updatePasswordDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/update")
    public ResponseEntity<String> updateEmail(@RequestBody UserDto userDto) {
        String username = ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
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
        String username = ((UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        userFacade.updateName(username, userDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id,
                                               @RequestParam Long reviewId,
                                               @RequestParam String role) {
        userFacade.changeUserRole(id, reviewId, role);
        return ResponseEntity.ok().build();
    }

}
