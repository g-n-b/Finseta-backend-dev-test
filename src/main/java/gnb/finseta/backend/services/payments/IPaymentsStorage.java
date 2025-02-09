package gnb.finseta.backend.services.payments;

import org.openapitools.model.Payment;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentsStorage {

	/**
	 * @param minimumPaymentAmount Only payments larger than this will be returned
	 * @param currencies           Only payments made in this currency will be returned
	 * @return A List of Payments that satisfy the filtering parameters
	 */
	List<Payment> getPayments(@Nullable BigDecimal minimumPaymentAmount, @Nullable List<String> currencies);

	/**
	 * @param payment The payment to be stored
	 * @return the Payment that gets stored
	 */
	Payment createPayment(Payment payment);
}
