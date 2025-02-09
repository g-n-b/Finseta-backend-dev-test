package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.exceptions.InvalidRequestFieldException;
import gnb.finseta.backend.services.payments.IPaymentsService;
import gnb.finseta.backend.services.payments.IPaymentsStorage;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Payment;

import java.math.BigDecimal;
import java.util.List;

import static gnb.finseta.backend.TestUtils.defaultAccountBuilder;
import static gnb.finseta.backend.TestUtils.defaultPaymentBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PaymentsServiceTest {
	IPaymentsStorage storageMock = mock(IPaymentsStorage.class);
	IPaymentsService subject = new PaymentsService(storageMock);

	@Test
	void thatPaymentServiceCallsStorageServiceOnCreatePayment() {
		Payment payment =  defaultPaymentBuilder().build();
		subject.handlePaymentRequest(payment);

		verify(storageMock).createPayment(eq(payment));
	}

	@Test
	void thatPaymentServiceCallsStorageServiceOnGetPayments() {
		subject.getPayments(BigDecimal.ONE, List.of("NZD"));

		verify(storageMock).getPayments(eq(BigDecimal.ONE), eq(List.of("NZD")));
	}

	@Test
	void thatNullParametersForCallsToGetPaymentsAreOkAndReachStorage() {
		subject.getPayments(null, null);

		verify(storageMock).getPayments(eq(null), eq(null));
	}

	@Test
	void thatInvalidCounterPartySortCodeThrowsEx() {
		var payment = defaultPaymentBuilder()
				.counterparty(defaultAccountBuilder()
						.sortCode("123").build())
				.build();

		var ex = assertThrows(InvalidRequestFieldException.class,
				() -> subject.handlePaymentRequest(payment));

		assertEquals("Invalid Counter Party sort code: 123", ex.getMessage());
	}

	@Test
	void thatInvalidCurrencyCodeThrowsException() {
		var ex = assertThrows(InvalidRequestFieldException.class,
				() -> subject.getPayments(null, List.of("NZ")));

		assertEquals("Currency code: NZ", ex.getMessage());
	}

	@Test
	void thatInvalidMinAmountThrowsException() {
		var ex = assertThrows(InvalidRequestFieldException.class, () ->
				subject.getPayments(BigDecimal.valueOf(-1), null));

		assertEquals("Get Payment amount: -1", ex.getMessage());
	}
}