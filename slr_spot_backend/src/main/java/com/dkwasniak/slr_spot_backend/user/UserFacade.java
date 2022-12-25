package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationToken;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationTokenService;
import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.review.Review;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRole;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
import com.dkwasniak.slr_spot_backend.user.dto.UpdatePasswordDto;
import com.dkwasniak.slr_spot_backend.user.dto.UserDto;
import com.dkwasniak.slr_spot_backend.userReview.UserReview;
import com.dkwasniak.slr_spot_backend.userReview.UserReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final UserReviewService userReviewService;
    private final ReviewRoleService reviewRoleService;

    public long createUser(User user) {
        User savedUser = userService.saveUser(user);

        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(savedUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink = String.format("http://localhost:3000/activate/%s", confirmationToken.getToken());
        emailService.sendVerificationEmail(user.getEmail(), activationLink);
        return savedUser.getId();
    }

    public List<UserDto> getUsersByReviewId(Long reviewId) {
        List<UserReview> users = userReviewService.getUserReviewByReviewId(reviewId);
        return users.stream().map(userService::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public void confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);
        confirmationTokenService.confirmToken(confirmationToken);
        userService.activateUser(confirmationToken.getUser().getEmail());
    }

    public void updateEmail(String oldEmail, String newEmail) {
        User user = userService.getUserByEmail(oldEmail);
        ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(user);
        confirmationToken.setNewEmail(newEmail);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String activationLink = String.format("http://localhost:3000/email/confirm/%s", confirmationToken.getToken());
        emailService.sendVerificationEmail(newEmail, activationLink);
    }

    public void saveEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);
        if (isNull(confirmationToken.getNewEmail())) {
            throw new IllegalStateException("Cant confirm new email");
        }
        confirmationTokenService.confirmToken(confirmationToken);
        userService.updateEmail(confirmationToken.getUser(), confirmationToken.getNewEmail());
    }

    public void updatePassword(String username, UpdatePasswordDto updatePasswordDto) {
        userService.updatePassword(username,
                updatePasswordDto.getOldPassword(),
                updatePasswordDto.getNewPassword(),
                updatePasswordDto.getConfirmPassword());
    }

    public void updateName(String username, UserDto userDto) {
        userService.updateName(username, userDto.getFirstName(), userDto.getLastName());
    }

    public Set<String> getEmails(String currentUserEmail) {
        Set<String> allUsersEmails = userService.getAllEmails();
        allUsersEmails.remove(currentUserEmail);
        return allUsersEmails;
    }


    public void changeUserRole(Long userId, Long reviewId, String role) {
        UserReview userReview = userReviewService.getUserReviewByReviewIdAndUserId(reviewId, userId);
        userReviewService.changeUserReviewRole(userReview, reviewRoleService.getRoleByName(role));
    }
}
