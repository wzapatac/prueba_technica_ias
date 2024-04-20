package co.com.hotel;

import co.com.hotel.dao.RoomDAO;
import co.com.hotel.message.TechnicalExceptionMessage;
import co.com.hotel.room.Room;
import co.com.hotel.room.gateways.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class DynamoDbRoomsAdapter implements RoomRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncTable<RoomDAO> dynamoDbAsyncTable;
    private final GenericModelMapper genericModelMapper;


    public DynamoDbRoomsAdapter(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                                GenericModelMapper genericModelMapper,
                                @Value("adapters.dynamodb.tableUsersName") String tableName) {
        this.dynamoDbEnhancedAsyncClient = dynamoDbEnhancedAsyncClient;
        this.dynamoDbAsyncTable = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(RoomDAO.class));
        this.genericModelMapper = genericModelMapper;
    }


    @Override
    public Mono<Room> registerRoom(Room room) {
        return Mono.fromFuture(dynamoDbAsyncTable.putItem(genericModelMapper.getRoomDAOSinceRoom(room)))
                .thenReturn(room)
                .doOnSuccess(isOk -> logger.debug("Room Registered"))
                .onErrorResume(DynamoDbException.class, exception -> Mono.defer(() ->
                        Mono.error(new TechnicalException(TechnicalExceptionMessage.COULD_NOT_REGISTER_ROOM))));

    }

    @Override
    public Mono<List<Room>> findRooms(String hotelId) {
        List<Room> listRoom = new ArrayList<>();
        return getRoomsRecursive(getQuery(hotelId), listRoom);
    }

    @Override
    public Mono<Room> deleteRoom(Room room) {
        return Mono.fromFuture(dynamoDbAsyncTable.deleteItem(genericModelMapper.getRoomDAOSinceRoom(room)))
                .map(genericModelMapper::getRoomSinceRoomDAO);
    }


    private QueryEnhancedRequest getQuery(String hotelId) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(hotelId).build()))
                .queryConditional(QueryConditional.sortGreaterThan(Key.builder()
                        .partitionValue(hotelId).sortValue("\u0000").build()))
                .build();
    }

    private Mono<List<Room>> getRoomsRecursive(QueryEnhancedRequest queryEnhancedRequest, List<Room> roomList) {
        PagePublisher<RoomDAO> pagePublisher = dynamoDbAsyncTable.query(queryEnhancedRequest);

        return Mono.from(pagePublisher).map(roomDAOPage -> {
                    roomList.addAll(roomDAOPage.items().stream().map(genericModelMapper::getRoomSinceRoomDAO).toList());
                    return roomDAOPage;
                }).filter(page -> Objects.nonNull(page.lastEvaluatedKey()))
                .flatMap(pageResponse -> getRoomsRecursive(queryEnhancedRequest.toBuilder()
                        .exclusiveStartKey(pageResponse.lastEvaluatedKey())
                        .build(), roomList))
                .switchIfEmpty(Mono.just(roomList));
    }
}
