package co.com.hotel;

import co.com.hotel.dao.UserDAO;
import co.com.hotel.message.TechnicalExceptionMessage;
import co.com.hotel.user.User;
import co.com.hotel.user.gateways.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Repository
public class DynamoDbUsersAdapter implements UserRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncTable<UserDAO> dynamoDbAsyncTable;

    private final GenericModelMapper genericModelMapper;

    public DynamoDbUsersAdapter(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                                @Value("${adapters.dynamodb.tableUsersName}") String tableName,
                                GenericModelMapper genericModelMapper) {
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
        this.genericModelMapper = genericModelMapper;
        this.dynamoDbAsyncTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(UserDAO.class));
    }


    @Override
    public Mono<User> registerUser(User user) {
        return Mono.fromFuture(dynamoDbAsyncTable.putItem(genericModelMapper.getUserDAOSinceUser(user)))
                .thenReturn(user)
                .doOnSuccess(isOk -> logger.debug("User Registered"))
                .onErrorResume(DynamoDbException.class, exception -> Mono.defer(() ->
                        Mono.error(new TechnicalException(TechnicalExceptionMessage.COULD_NOT_REGISTER_USER))));
    }
}
