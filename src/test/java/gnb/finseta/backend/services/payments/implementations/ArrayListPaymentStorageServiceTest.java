package gnb.finseta.backend.services.payments.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static gnb.finseta.backend.TestUtils.defaultPaymentBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayListPaymentStorageServiceTest {

	private static final String GBP = "GBP";
	private static final String NZD = "NZD";
	ArrayListPaymentStorageService subject = new ArrayListPaymentStorageService();

	@BeforeEach
	void init() {
		subject = new ArrayListPaymentStorageService();
	}

	@Test
	void thatGetPaymentsOnEmptyListReturnsNoPayments() {
		var res = subject.getPayments(BigDecimal.ZERO, List.of(NZD));
		assertEquals(0, res.size());
	}

	@Test
	void thatAddingPaymentWillReturnThatSamePayment() {
		var payment = defaultPaymentBuilder().build();

		var res = subject.createPayment(payment);
		assertEquals(payment, res);
	}

	@Test
	void thatPaymentWillBeRetrievedIfUnFiltered() {
		var payment = defaultPaymentBuilder().build();

		subject.createPayment(payment);
		var res = subject.getPayments(BigDecimal.ZERO, List.of(NZD));
		assertEquals(1, res.size());
		assertEquals(payment, res.get(0));
	}

	@Test
	void thatPaymentsCanBeFilteredByCurrency() {
		var paymentNz = defaultPaymentBuilder().build();
		var paymentUk = defaultPaymentBuilder().currency("GBP").build();

		subject.createPayment(paymentNz);
		subject.createPayment(paymentUk);
		var res = subject.getPayments(BigDecimal.ZERO, List.of(NZD));
		assertEquals(1, res.size());
		assertEquals(paymentNz, res.get(0));
	}

	@Test
	void thatPaymentsCanBeFilteredByAmount() {
		var paymentNz = defaultPaymentBuilder().amount(BigDecimal.valueOf(2)).build();
		var paymentUk = defaultPaymentBuilder().amount(BigDecimal.TEN).currency(GBP).build();

		subject.createPayment(paymentNz);
		subject.createPayment(paymentUk);
		var res = subject.getPayments(BigDecimal.valueOf(5), List.of(GBP, NZD));
		assertEquals(1, res.size());
		assertEquals(paymentUk, res.get(0));
	}
}