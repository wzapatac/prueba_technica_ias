package co.com.hotel;

import co.com.hotel.dao.UserDAO;
import co.com.hotel.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamoDbUsersAdapterTest {

    @InjectMocks
    private DynamoDbUsersAdapter dynamoDbUsersAdapter;
    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    @Mock
    private DynamoDbAsyncTable<UserDAO> dynamoDbAsyncTable;
    @Mock
    private GenericModelMapper genericModelMapper;

    private UserDAO userDAO;
    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId("user_id_test");
        user.setDocumentType("cc");
        user.setEndDate("25/04/2024");
        user.setName("Walther test");
        user.setStartDate("22/04/2024");
        user.setIdentification(5555555);
        user.setRoomId("101");

        userDAO = new UserDAO();
        userDAO.setId("user_id_test");
        userDAO.setDocumentType("cc");
        userDAO.setEndDate("25/04/2024");
        userDAO.setName("Walther test");
        userDAO.setStartDate("22/04/2024");
        userDAO.setIdentification(5555555);
        userDAO.setRoomId("101");

        when(dynamoDbEnhancedAsyncClient.table(any(), any(TableSchema.class)))
                .thenReturn(dynamoDbAsyncTable);

        dynamoDbUsersAdapter = new DynamoDbUsersAdapter(dynamoDbEnhancedAsyncClient, "table_test",
                genericModelMapper);
    }

    @Test
    void couldRegisterUserSuccessFull() {
        when(dynamoDbAsyncTable.putItem(any(UserDAO.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        when(genericModelMapper.getUserDAOSinceUser(any(User.class))).thenReturn(userDAO);

        StepVerifier.create(dynamoDbUsersAdapter.registerUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void couldRegisterUserFail() {
        when(dynamoDbAsyncTable.putItem(any(UserDAO.class))).thenReturn(CompletableFuture
                .failedFuture(DynamoDbException.builder().message("Error").build()));

        when(genericModelMapper.getUserDAOSinceUser(any(User.class))).thenReturn(userDAO);

        StepVerifier.create(dynamoDbUsersAdapter.registerUser(user))
                .expectError(TechnicalException.class)
                .verify();
    }
}