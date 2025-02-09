package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.services.payments.IPaymentsService;
import gnb.finseta.backend.services.payments.IPaymentsStorage;
import org.junit.jupiter.api.Test;
import org.openapitools.model.Payment;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PaymentsServiceTest {
	IPaymentsStorage storageMock = mock(IPaymentsStorage.class);
	IPaymentsService subject = new PaymentsService(storageMock);

	@Test
	void thatPaymentServiceCallsStorageServiceOnCreatePayment() {
		Payment payment =  Payment.builder()
				.amount(BigDecimal.ONE)
				.currency("NZD")
				.build();
		subject.handlePaymentRequest(payment);

		verify(storageMock).createPayment(eq(payment));
	}

	@Test
	void thatPaymentServiceCallsStorageServiceOnGetPayments() {
		subject.getPayments(BigDecimal.ONE, List.of("NZD"));

		verify(storageMock).getPayments(eq(BigDecimal.ONE), eq(List.of("NZD")));
	}
}