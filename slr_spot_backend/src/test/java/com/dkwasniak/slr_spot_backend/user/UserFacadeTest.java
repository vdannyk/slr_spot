package com.dkwasniak.slr_spot_backend.user;

import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationToken;
import com.dkwasniak.slr_spot_backend.confirmationToken.ConfirmationTokenService;
import com.dkwasniak.slr_spot_backend.email.EmailService;
import com.dkwasniak.slr_spot_backend.reviewRole.ReviewRoleService;
import com.dkwasniak.slr_spot_backend.userReview.UserReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserFacadeTest {

    @InjectMocks
    private UserFacade userFacade;

    @Mock
    private UserService userService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;

    @Test
    public void createUser_shouldReturnNewUserId_whenCreated() {
        var user = new User("test", "test", "test@gmail.com", "123");
        user.setId(1L);
        var token = new ConfirmationToken();
        token.setToken("test");

        when(userService.saveUser(user)).thenReturn(user);
        when(confirmationTokenService.createConfirmationToken(user)).thenReturn(token);
        doNothing().when(confirmationTokenService).saveConfirmationToken(token);
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        var newUserId = userFacade.createUser(user);

        assertEquals(1L, newUserId);
    }

    @Test
    public void getEmails_shouldReturnEmails_whenFound() {
        when(userService.getAllEmails()).thenReturn(new HashSet<>(){{ add("test"); add("test2"); }});

        var resultEmails = userFacade.getEmails("test");

        assertEquals(1, resultEmails.size());
    }
}
