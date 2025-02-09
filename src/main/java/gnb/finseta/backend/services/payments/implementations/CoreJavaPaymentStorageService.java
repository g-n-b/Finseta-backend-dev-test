package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.services.payments.IPaymentsStorage;
import org.openapitools.model.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class CoreJavaPaymentStorageService implements IPaymentsStorage {

	List<Payment> payments = new ArrayList<>();

	@Override
	public List<Payment> getPayments(BigDecimal minimumPaymentAmount, List<String> currencies) {
		return payments.stream()
				.filter(isLargerThan(minimumPaymentAmount))
				.filter(isCurrencyIn(currencies))
				.toList();
	}

	@Override
	public Payment createPayment(Payment payment) {
		payments.add(payment);
		return payment;
	}

	private static Predicate<Payment> isCurrencyIn(List<String> currencies) {
		return payment -> currencies.contains(payment.getCurrency());
	}

	private static Predicate<Payment> isLargerThan(BigDecimal minAmount) {
		return payment -> payment.getAmount().compareTo(minAmount) > 0;
	}
}
