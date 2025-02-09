package gnb.finseta.backend.services.payments;

import org.openapitools.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentsService {
    List<Payment> getPayments(BigDecimal minAmount, List<String> currencies);

    Payment handlePaymentRequest(Payment payment);
}
