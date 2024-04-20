package co.com.hotel.user;

import co.com.hotel.user.gateways.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserUserCase {
    private final UserRepository userRepository;

    public UserUserCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> registerUser(User user) {
        return userRepository.registerUser(user);
    }
}
