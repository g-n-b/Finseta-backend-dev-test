package gnb.finseta.backend.rest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.model.Account;
import org.openapitools.model.Payment;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.ErrorResponse;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaymentsIntegrationTest {

    public static final String PAYMENTS_URL = "http://localhost:8080/payments";
    private static final int BAD_REQUEST_CODE = 400;

    ObjectMapper objectMapper = new JsonMapper();

    @BeforeAll
    static void init() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void validPaymentShouldReturn201withPaymentAsBody() throws Exception {
        // Given
        var requestBody = objectMapper.writeValueAsString(defaultPaymentBuilder().build());

        var response = given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(201).extract().as(Payment.class);
        assertEquals(defaultPaymentBuilder().build(), response);
    }

    /**
     *         - in: query
     *           name: minAmount
     *           required: false
     *           schema:
     *             type: number
     *           description: The minimum payment amount (inclusive)
     *         - in: query
     *           name: currencies
     *           required: false
     *           schema:
     *             type: array
     *             items:
     *               type: string
     *           description: A list of three letter ISO 4217 codes to include
     */
    @Test
    void whenAmountLessThanMinimumRequestedForGetPaymentsReturnBadRequest() {
        given()
                .queryParam("minAmount", -1)
                .get(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);
    }

    @Test
    void whenCurrenciesGivenWithInvalidFormatReturnBadRequest() {
        given()
                .queryParam("currencies", new String[]{"NZ"})
                .get(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);
    }

    @Test
    void whenAmountIsNotGreaterThanZeroBadRequestReturned() throws JsonProcessingException {
        var requestPayment = defaultPaymentBuilder().amount(BigDecimal.ZERO).build();

        var requestBody = objectMapper.writeValueAsString(requestPayment);

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);

    }

    @Test
    void whenCurrencyIsEmptyThrowBadRequest() throws JsonProcessingException {

        var requestPayment = defaultPaymentBuilder().currency("").build();
        var requestBody = objectMapper.writeValueAsString(requestPayment);

        var response = given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);

    }

    @Test
    void whenCounterPartyTypeIsEmptyThrowBadRequest() throws JsonProcessingException {
        var requestPayment = defaultPaymentBuilder()
                .counterparty(defaultAccountBuilder().type(null).build())
                .build();

        var requestBody = objectMapper.writeValueAsString(requestPayment);

        var response = given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "ABCDEF"})
    void whenCounterPartySortCodeIsInvalidFormatThrowBadRequest(String invalidSortCode) throws JsonProcessingException {
        var requestPayment =
                defaultPaymentBuilder()
                        .counterparty(defaultAccountBuilder()
                                .sortCode(invalidSortCode)
                                .build())
                        .build();
        var requestBody = objectMapper.writeValueAsString(requestPayment);

        var response = given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);

    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "123456789", "abcdefghi"})
    void thatInvalidCounterPartyAccountNumberFormatThrowsException(String invalidAccNum) throws JsonProcessingException {
        var requestPayment = defaultPaymentBuilder()
                .counterparty(defaultAccountBuilder()
                        .accountNumber(invalidAccNum)
                        .build())
                .build();

        var requestBody = objectMapper.writeValueAsString(requestPayment);

        var response = given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(PAYMENTS_URL)
                .then()
                .statusCode(BAD_REQUEST_CODE);
    }

    private static Payment.PaymentBuilder defaultPaymentBuilder() {
        var counterParty = defaultAccountBuilder().build();

        return Payment.builder()
                .amount(BigDecimal.TEN)
                .currency("NZD")
                .counterparty(counterParty);
    }

    private static Account.AccountBuilder defaultAccountBuilder() {
        return Account.builder()
                .type(Account.TypeEnum.SORT_CODE_ACCOUNT_NUMBER)
                .accountNumber("123")
                .sortCode("123456");
    }
}
