package co.com.hotel.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ExtendWith(MockitoExtension.class)
class DynamoDbConfigTest {
    @InjectMocks
    private DynamoDbConfig dynamoDbConfig;
    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @BeforeEach
    void setUpt(){
        dynamoDbConfig= new DynamoDbConfig();
    }
    @Test
    void validateAsyncClientCreatedSuccessFull(){
        Assertions.assertNotNull(dynamoDbConfig.getDynamoDbAsyncClient("http://localhost:8080"));
    }

    @Test
    void validateDynamoDbEnhancedAsyncClientCreatedSuccessFull(){
        Assertions.assertNotNull(dynamoDbConfig.getDynamoDbEnhancedAsyncClient(dynamoDbAsyncClient));
    }
}