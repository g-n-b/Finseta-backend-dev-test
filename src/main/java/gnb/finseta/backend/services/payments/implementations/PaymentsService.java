package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.exceptions.InvalidRequestFieldException;
import gnb.finseta.backend.logging.IRequestLogger;
import gnb.finseta.backend.services.payments.IPaymentsService;
import gnb.finseta.backend.services.payments.IPaymentsStorage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.openapitools.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class PaymentsService implements IPaymentsService {
	private final IPaymentsStorage storageManager;
	private final IRequestLogger logger;

	@Override
	public List<Payment> getPayments(@Nullable BigDecimal minAmount, @Nullable List<String> currencies) {
		if (!CollectionUtils.isEmpty(currencies)) {
			currencies.forEach(this::validateCurrencyCode);
		}
		validateGetPaymentAmount(minAmount);
		return storageManager.getPayments(minAmount, currencies);
	}

	@Override
	public Payment handlePaymentRequest(Payment payment) {
		validateCurrencyCode(payment.getCurrency());
		validateNewPaymentAmount(payment.getAmount());
		validateCounterPartySortCode(payment.getCounterparty().getSortCode());
		return storageManager.createPayment(payment);
	}

	private void validateCounterPartySortCode(@NotNull String sortCode) throws InvalidRequestFieldException {
		final String counterPartySortCodeRegex = "\\d{6}";
		if (sortCode.matches(counterPartySortCodeRegex)) return;
		throw new InvalidRequestFieldException("Invalid Counter Party sort code: %s".formatted(sortCode));
	}

	private void validateNewPaymentAmount(BigDecimal amount) throws InvalidRequestFieldException {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidRequestFieldException("Create Payment amount: %s".formatted(amount));
		}
	}

	private void validateGetPaymentAmount(BigDecimal amount) throws InvalidRequestFieldException {
		if (isNull(amount))
			return;
		logger.log("Amount %s".formatted(amount.toString()));
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new InvalidRequestFieldException("Get Payment amount: %s".formatted(amount));
		}
	}

	private void validateCurrencyCode(String currencyCode) throws InvalidRequestFieldException {
		String threeAlphabeticalCharacters = "[a-zA-Z]{3}";
		logger.log("Currency code %s".formatted(currencyCode));
		if (currencyCode.matches(threeAlphabeticalCharacters))
			return;
		throw new InvalidRequestFieldException("Currency code: %s".formatted(currencyCode));
	}
}
