package co.com.hotel.api.exception;

import co.com.hotel.TechnicalException;
import co.com.hotel.api.exception.model.ErrorResponse;
import co.com.hotel.api.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Util util;

    public WebExceptionHandler(DefaultErrorAttributes errorAttributes, ApplicationContext applicationContext,
                               ServerCodecConfigurer serverCodecConfigurer, Util util) {

        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.util = util;
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());

    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(BusinessException.class, businessException ->
                        buildErrorResponse(businessException, request))
                .onErrorResume(TechnicalException.class, technicalException ->
                        buildErrorResponse(technicalException, request))
                .onErrorResume(Throwable.class, exception -> buildErrorResponse(request, exception))
                .cast(ErrorResponse.class)
                .flatMap(this::buildResponse)
                .doOnError(error -> logger.error(request.path(), this.getClass().getName(), error));
    }

    private Mono<ErrorResponse> buildErrorResponse(TechnicalException technicalException,
                                                   ServerRequest request) {
        return Mono.just(util.buildErrorResponse(technicalException, this.buildDomain(request)))
                .doOnSuccess(error -> logger.error(request.path(), this.getClass().getName(), technicalException));
    }

    private Mono<ErrorResponse> buildErrorResponse(BusinessException businessException,
                                                   ServerRequest request) {
        return Mono.just(util.buildErrorResponse(businessException, this.buildDomain(request)))
                .doOnSuccess(error -> logger.error(request.path(), this.getClass().getName(), businessException));
    }

    private Mono<ErrorResponse> buildErrorResponse(ServerRequest request, Throwable exception) {
        return Mono.just(util.buildErrorResponse(this.buildDomain(request)))
                .doOnSuccess(error -> logger.error(request.path(), this.getClass().getName(), exception));
    }

    private Mono<ServerResponse> buildResponse(ErrorResponse errorResponse) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(errorResponse), ErrorResponse.class);

    }
    private String buildDomain(ServerRequest request) {
        return request.method().name().concat(":").concat(request.path());
    }
}
