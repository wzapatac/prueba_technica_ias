package co.com.hotel;

import co.com.hotel.dao.RoomDAO;
import co.com.hotel.message.TechnicalExceptionMessage;
import co.com.hotel.room.Room;
import co.com.hotel.room.RoomFilter;
import co.com.hotel.room.gateways.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class DynamoDbRoomsAdapter implements RoomRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncTable<RoomDAO> dynamoDbAsyncTable;
    private final GenericModelMapper genericModelMapper;

    public DynamoDbRoomsAdapter(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                                GenericModelMapper genericModelMapper,
                                @Value("${adapters.dynamodb.tableRoomsName}") String tableName) {
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
    public Mono<List<Room>> findRooms(RoomFilter roomFilter, String hotelId) {
        List<Room> listRoom = new ArrayList<>();
        return getRoomsRecursive(getQuery(roomFilter, hotelId), listRoom)
                .doOnSuccess(isOk -> logger.debug("Rooms Listed!!"))
                .onErrorResume(DynamoDbException.class, exception -> Mono.defer(() ->
                        Mono.error(new TechnicalException(TechnicalExceptionMessage.COULD_NOT_LIST_ALL_ROOMS))));
    }

    @Override
    public Mono<Room> findRoomById(String hotelId, String roomId) {
        return Mono.fromFuture(dynamoDbAsyncTable.getItem(Key.builder().partitionValue(hotelId)
                        .sortValue(roomId).build()))
                .map(genericModelMapper::getRoomSinceRoomDAO)
                .doOnSuccess(isOk -> logger.debug("Room Listed!!"))
                .onErrorResume(DynamoDbException.class, exception -> Mono.defer(() ->
                        Mono.error(new TechnicalException(TechnicalExceptionMessage.COULD_NOT_LIST_ROOM))));
    }


    private QueryEnhancedRequest getQuery(RoomFilter roomFilter, String hotelId) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(hotelId).build()))
                .queryConditional(QueryConditional.sortGreaterThan(Key.builder()
                        .partitionValue(hotelId).sortValue("\u0000").build()))
                .filterExpression(getExpression(roomFilter))
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

    private Expression getExpression(RoomFilter roomFilter) {
        Map<String, AttributeValue> values = new HashMap<>();
        Map<String, String> names = new HashMap<>();
        var expression = "";
        if (!roomFilter.getRoomState().isEmpty()) {
            values.put(":roomState", AttributeValue.builder().s(roomFilter.getRoomState()).build());
            names.put("#s", "roomState");
            expression = getExpression(expression, "#s = :roomState");
        }
        if (!roomFilter.getRoomType().isEmpty()) {
            values.put(":roomType", AttributeValue.builder().s(roomFilter.getRoomType()).build());
            names.put("#t", "roomType");
            expression = getExpression(expression, "#t = :roomType");
        }
        if (roomFilter.getNumberGuests() > 0) {
            values.put(":numberGuests", AttributeValue.builder().n(roomFilter.getNumberGuests().toString()).build());
            names.put("#n", "numberGuests");
            expression = getExpression(expression, "#n = :numberGuests");
        }
        if (!roomFilter.getReservationDate().isEmpty()) {
            values.put(":reservationStartDate", AttributeValue.builder().s(roomFilter.getReservationDate()).build());
            values.put(":reservationEndDate", AttributeValue.builder().s(roomFilter.getReservationDate()).build());
            names.put("#ds", "reservationStartDate");
            names.put("#de", "reservationEndDate");
            expression = getExpression(expression, "not(#ds <= :reservationStartDate and #de >= :reservationEndDate)");
        }
        return (!expression.isEmpty()) ? Expression.builder().expression(expression).expressionNames(names)
                .expressionValues(values).build() : Expression.builder().build();
    }

    private String getExpression(String generalExpression, String localExpression) {
        generalExpression = generalExpression.concat((generalExpression.isEmpty()) ? "" : " and ");
        generalExpression = generalExpression.concat(localExpression);
        return generalExpression;
    }
}
