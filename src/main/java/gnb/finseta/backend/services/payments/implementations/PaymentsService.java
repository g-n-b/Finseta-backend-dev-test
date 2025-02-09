package gnb.finseta.backend.services.payments.implementations;

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
		return storageManager.getPayments(minAmount, currencies);
	}

	@Override
	public Payment handlePaymentRequest(Payment payment) {
		return storageManager.createPayment(payment);
	}
}
