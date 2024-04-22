package co.com.hotel;

import co.com.hotel.dao.RoomDAO;
import co.com.hotel.room.Room;
import co.com.hotel.room.RoomFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Subscription;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DynamoDbRoomsAdapterTest {

    @InjectMocks
    private DynamoDbRoomsAdapter dynamoDbRoomsAdapter;
    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    @Mock
    private DynamoDbAsyncTable<RoomDAO> dynamoDbAsyncTable;
    @Mock
    private GenericModelMapper genericModelMapper;

    private RoomDAO roomDAO;
    private Room room;

    @BeforeEach
    void setUp() {

        roomDAO = new RoomDAO();
        roomDAO.setRoomType("Sencilla");
        roomDAO.setRoomState("Disponible");
        roomDAO.setNightValue(100000);
        roomDAO.setNumberGuests(2);

        room = new Room();
        room.setRoomType("Sencilla");
        room.setRoomState("Disponible");
        room.setNightValue(100000);
        room.setNumberGuests(2);

        when(dynamoDbEnhancedAsyncClient.table(any(), any(TableSchema.class)))
                .thenReturn(dynamoDbAsyncTable);

        dynamoDbRoomsAdapter = new DynamoDbRoomsAdapter(dynamoDbEnhancedAsyncClient,
                genericModelMapper, "table_test");

    }

    @Test
    void couldRegisterRoomSuccessFull() {

        when(dynamoDbAsyncTable.putItem(any(RoomDAO.class)))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(genericModelMapper.getRoomDAOSinceRoom(any(Room.class))).thenReturn(roomDAO);

        StepVerifier.create(dynamoDbRoomsAdapter.registerRoom(room))
                .expectNext(room)
                .verifyComplete();
    }

    @Test
    void couldRegisterRoomFail() {

        when(dynamoDbAsyncTable.putItem(any(RoomDAO.class)))
                .thenReturn(CompletableFuture
                        .failedFuture(DynamoDbException.builder().message("Error").build()));
        when(genericModelMapper.getRoomDAOSinceRoom(any(Room.class))).thenReturn(roomDAO);

        StepVerifier.create(dynamoDbRoomsAdapter.registerRoom(room))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void couldFindsRoomsSuccessFull() {

        var roomFilter = new RoomFilter();
        roomFilter.setRoomState("stateTest");
        roomFilter.setReservationDate("ReservationTest");
        roomFilter.setNumberGuests(2);
        roomFilter.setRoomType("typeTest");

        Subscription subscription = new Subscription() {
            @Override
            public void request(long n) {

            }

            @Override
            public void cancel() {

            }
        };
        PagePublisher<RoomDAO> pagePublisher = PagePublisher.create(s -> {
            s.onSubscribe(subscription);
            s.onNext(Page.builder(RoomDAO.class).items(List.of(roomDAO)).build());
            s.onComplete();
        });

        when(dynamoDbAsyncTable.query(any(QueryEnhancedRequest.class))).thenReturn(pagePublisher);
        when(genericModelMapper.getRoomSinceRoomDAO(any(RoomDAO.class))).thenReturn(room);

        StepVerifier.create(dynamoDbRoomsAdapter.findRooms(roomFilter, "hotel_test"))
                .expectNext(List.of(room))
                .verifyComplete();
    }

    @Test
    void couldFindsRoomsFail() {

        var roomFilter = new RoomFilter();
        roomFilter.setRoomState("stateTest");
        roomFilter.setReservationDate("ReservationTest");
        roomFilter.setNumberGuests(2);
        roomFilter.setRoomType("typeTest");

        PagePublisher<RoomDAO> pagePublisher = PagePublisher.create(s -> {
            s.onError(DynamoDbException.builder().message("Error").build());
        });

        when(dynamoDbAsyncTable.query(any(QueryEnhancedRequest.class))).thenReturn(pagePublisher);

        StepVerifier.create(dynamoDbRoomsAdapter.findRooms(roomFilter, "hotel_test"))
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void couldFindRoomByIdSuccessFull() {

        when(dynamoDbAsyncTable.getItem(any(Key.class))).thenReturn(CompletableFuture.completedFuture(roomDAO));
        when(genericModelMapper.getRoomSinceRoomDAO(any(RoomDAO.class))).thenReturn(room);

        StepVerifier.create(dynamoDbRoomsAdapter.findRoomById("hotel_test", "room_test"))
                .expectNext(room)
                .verifyComplete();
    }

    @Test
    void couldFindRoomByIdFail() {

        when(dynamoDbAsyncTable.getItem(any(Key.class)))
                .thenReturn(CompletableFuture
                        .failedFuture(DynamoDbException.builder().message("Error").build()));

        StepVerifier.create(dynamoDbRoomsAdapter.findRoomById("hotel_test", "room_test"))
                .expectError(TechnicalException.class)
                .verify();
    }

}