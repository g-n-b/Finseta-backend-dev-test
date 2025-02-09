package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.services.payments.IPaymentsStorage;
import org.openapitools.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

@Service
public class ArrayListPaymentStorageService implements IPaymentsStorage {

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
		if (CollectionUtils.isEmpty(currencies))
			return payment -> true;
		return payment -> currencies.contains(payment.getCurrency());
	}

	private static Predicate<Payment> isLargerThan(BigDecimal minAmount) {
		if (isNull(minAmount))
			return payment -> true;
		return payment -> payment.getAmount().compareTo(minAmount) > 0;
	}
}
