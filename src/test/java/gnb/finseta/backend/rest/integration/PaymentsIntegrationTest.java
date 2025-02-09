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
import org.openapitools.model.BadRequest;
import org.openapitools.model.Payment;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static gnb.finseta.backend.TestUtils.defaultAccountBuilder;
import static gnb.finseta.backend.TestUtils.defaultPaymentBuilder;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	void whenAmountLessThanMinimumRequestedForGetPaymentsReturnBadRequest() {
		given()
				.queryParam("minAmount", -1)
				.get(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE);
	}

	@Test
	@DirtiesContext
	void whenPaymentIsCreatedItIsCanBeRetrievedOnGetPayments() throws JsonProcessingException {
		// Given
		var requestBody = objectMapper.writeValueAsString(defaultPaymentBuilder().build());

		given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(201);

		Payment[] res = given()
				.get(PAYMENTS_URL)
				.then()
				.statusCode(200)
				.extract()
				.body().as(Payment[].class);

		assertEquals(defaultPaymentBuilder().build(), res[0]);
	}

	@Test
	@DirtiesContext
	void whenGetPaymentWithoutQueryParamsWillReturnAllPayments() throws JsonProcessingException {
		// create 10 payments
		for (int i = 1; i <= 10; i++) {
			var requestBody = objectMapper.writeValueAsString(defaultPaymentBuilder()
					.amount(BigDecimal.valueOf(i))
					.build());

			given().contentType(ContentType.JSON)
					.body(requestBody)
					.when()
					.post(PAYMENTS_URL)
					.then()
					.statusCode(201);
		}

		// Filter for val > 5
		Payment[] res = given()
				.queryParam("minAmount", 5)
				.queryParam("currencies", new String[]{"NZD", "GBP"})
				.get(PAYMENTS_URL)
				.then()
				.statusCode(200)
				.extract()
				.body().as(Payment[].class);

		// get 5 payments
		assertEquals(5, res.length);
	}

	@Test
	void whenCurrenciesGivenWithInvalidFormatReturnBadRequest() {
		var res = given()
				.queryParam("currencies", new String[]{"NZ"})
				.get(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);
		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertEquals("Currency code: NZ", err.getMessage());
	}

	@Test
	void whenAmountIsNotGreaterThanZeroBadRequestReturned() throws JsonProcessingException {
		var requestPayment = defaultPaymentBuilder().amount(BigDecimal.ZERO).build();

		var requestBody = objectMapper.writeValueAsString(requestPayment);

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);
		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertEquals("Create Payment amount: 0", err.getMessage());
	}

	@Test
	void whenCurrencyIsEmptyThrowBadRequest() throws JsonProcessingException {

		var requestPayment = defaultPaymentBuilder()
				.currency("")
				.build();
		var requestBody = objectMapper.writeValueAsString(requestPayment);

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);
		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertEquals("Field error in object 'payment' on field 'currency': rejected value []; " +
				"codes [Size.payment.currency...", err.getMessage());
	}

	@Test
	void whenCounterPartyTypeIsEmptyThrowBadRequest() throws JsonProcessingException {
		var requestPayment = defaultPaymentBuilder()
				.counterparty(defaultAccountBuilder().type(null).build())
				.build();

		var requestBody = objectMapper.writeValueAsString(requestPayment);

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);

		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertEquals("Field error in object 'payment' on field 'counterparty.type': rejected value [null]; " +
				"codes [NotNull....", err.getMessage());
	}

	@ParameterizedTest
	@ValueSource(strings = {"12345", "1234567"})
	void whenCounterPartySortCodeIsInvalidFormatThrowBadRequest(String invalidSortCode) throws JsonProcessingException {
		var requestPayment = defaultPaymentBuilder()
				.counterparty(defaultAccountBuilder()
						.sortCode(invalidSortCode)
						.build())
				.build();
		var requestBody = objectMapper.writeValueAsString(requestPayment);

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);

		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertTrue(err.getMessage().matches((".*Field error in object 'payment' on " +
				"field 'counterparty.sortCode.*%s.*").formatted(invalidSortCode)));
	}

	@Test
	void thatNonNumericSortCodeReturnsBadRequest() throws JsonProcessingException {
		String invalidSortCode = "ABCDEF";
		var requestPayment = defaultPaymentBuilder()
				.counterparty(defaultAccountBuilder()
						.sortCode(invalidSortCode)
						.build())
				.build();
		var requestBody = objectMapper.writeValueAsString(requestPayment);

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);

		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertEquals("Invalid Counter Party sort code: ABCDEF" ,err.getMessage());
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

		var res = given().contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post(PAYMENTS_URL)
				.then()
				.statusCode(BAD_REQUEST_CODE)
				.extract().body().as(BadRequest.class);

		assertEquals(1, res.getErrors().size());
		var err = res.getErrors().get(0);
		assertTrue( err.getMessage()
				.matches("^Field error in object 'payment' on field 'counterparty.accountNumber':" +
						".*\\[%s].*".formatted(invalidAccNum)));
	}
}
