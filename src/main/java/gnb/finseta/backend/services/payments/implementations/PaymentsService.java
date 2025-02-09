package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.exceptions.InvalidRequestFieldException;
import gnb.finseta.backend.services.payments.IPaymentsService;
import gnb.finseta.backend.services.payments.IPaymentsStorage;
import lombok.AllArgsConstructor;
import org.openapitools.model.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentsService implements IPaymentsService {
	private final IPaymentsStorage storageManager;

	@Override
	public List<Payment> getPayments(BigDecimal minAmount, List<String> currencies) {
		currencies.forEach(this::validateCurrencyCode);
		return storageManager.getPayments(minAmount, currencies);
	}

	@Override
	public Payment handlePaymentRequest(Payment payment) {
		validateCurrencyCode(payment.getCurrency());
		return storageManager.createPayment(payment);
	}

	private void validateCurrencyCode(String currencyCode) {
		String threeAlphabeticalCharacters = "[a-zA-Z]{3}";
		if (currencyCode.matches(threeAlphabeticalCharacters))
			return;
		throw new InvalidRequestFieldException("Currency code", currencyCode);
	}
}
