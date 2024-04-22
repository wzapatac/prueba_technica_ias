package co.com.hotel.user.gateways;

import co.com.hotel.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> registerUser(User user);
}
