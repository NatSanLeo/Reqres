import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private static boolean started = false;
    private static ReentrantLock lock = new ReentrantLock();

    @BeforeAll
    public static void setUp() {
        lock.lock();
        try {
            if (!started) {
                logger.info("Iniciando configuración");
                RestAssured.requestSpecification = defaultRequestEspecification();
                logger.info("Finalizando configuración");
                started = true;
            }

        } finally {
            lock.unlock();
        }


    }

    private static RequestSpecification defaultRequestEspecification() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());
        filters.add(new AllureRestAssured());

        return new RequestSpecBuilder()
                .setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
                .addFilters(filters)
                .setContentType(ContentType.JSON)
                .build();

    }

    private ResponseSpecification defaultResponseSpecificationSuccesfulOperation() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();

    }


}
