package net.thumbtack.school.mock;

import net.thumbtack.school.concert.base.user.UserErrorCode;
import net.thumbtack.school.concert.base.user.UserException;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoimpl.UserDaoimpl;
import net.thumbtack.school.concert.dto.request.user.LoginUserDtoRequest;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.sevice.UserService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestUserService {

    // Mock tests
    @Test
    public void testRegisterUserWithMocks() throws UserException {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.insert((any(User.class)))).thenReturn(UUID.randomUUID());

        UserService userService = new UserService();
        userService.setUserDao(mockUserDao);

        String jsonResp = userService.registerUser(
                "{\"firstName\":\"UserName\", \"lastName\":\"UserName\",\"login\":\"Vadim5555\",\"password\":\"SwordFish123\"}");
        assertTrue(jsonResp.contains("token"));
    }

    @Test
    public void testLoginUserWithMocks() throws UserException {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.loginUser((any(LoginUserDtoRequest.class)))).thenReturn(UUID.randomUUID());

        UserService userService = new UserService();
        userService.setUserDao(mockUserDao);

        String jsonResp = userService.loginUser("{\"login\":\"abc\",\"password\":\"***\"}");
        assertTrue(jsonResp.contains("token"));
    }

    @Test
    public void leaveFromServerTest() throws Exception {
        UserDao mockUserDao = mock(UserDao.class);
        UserService userService = new UserService();
        userService.setUserDao(mockUserDao);

        doNothing()
                .doThrow(new UserException(UserErrorCode.INVALID_TOKEN))
                .when(mockUserDao).removeUser("c980779a-2992-429d-9b6c-ffbd8c313987");

        String response = userService.removeUser("{\"token\": \"c980779a-2992-429d-9b6c-ffbd8c313987\"}");
        assertEquals("null", response);

        response = userService.removeUser("{\"token\": \"11111111-1111-1111-1111-111111111111\"}");
        assertEquals("null", response);

        response = userService.removeUser("{\"token\": \"c980779a-2992-429d-9b6c-ffbd8c313987\"}");
        assertTrue(response.contains("Invalid token"));
    }

    @Test
    public void logoutFromServerTest() throws Exception {
        UserDao mockUserDao = mock(UserDao.class);
        UserService userService = new UserService();
        userService.setUserDao(mockUserDao);

        doNothing()
                .doThrow(new UserException(UserErrorCode.INVALID_TOKEN))
                .when(mockUserDao).logoutUser("c980779a-2992-429d-9b6c-ffbd8c313987");

        String response = userService.logoutUser("{\"token\": \"c980779a-2992-429d-9b6c-ffbd8c313987\"}");
        assertEquals("null", response);

        response = userService.logoutUser("{\"token\": \"11111111-1111-1111-1111-111111111111\"}");
        assertEquals("null", response);

        response = userService.logoutUser("{\"token\": \"c980779a-2992-429d-9b6c-ffbd8c313987\"}");
        assertTrue(response.contains("Invalid token"));
    }

    @Test
    public void testRealMethod() throws Exception {
        UserService userService = new UserService();
        UserDao mockUserDao = mock(UserDaoimpl.class);
        when(mockUserDao.loginUser(any(LoginUserDtoRequest.class))).thenCallRealMethod();
        String jsonresponse = userService.loginUser("{\"login\":\"defaultLogin1\",\"password\":\"SwordFish1\"}");
        assertTrue(jsonresponse.contains("Wrong login"));
    }


}
