package co.com.hotel.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ModelMapperConfigTest {
    @Test
    void createModelMapper() {
        var modelMapperConfig = new ModelMapperConfig();
        Assertions.assertNotNull(modelMapperConfig.getModelMapper());
    }
}