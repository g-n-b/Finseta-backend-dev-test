package gnb.finseta.backend.services.payments;

import org.openapitools.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentsService {
    void getPayments(BigDecimal minAmount, List<String> currencies);


    void handlePaymentRequest(Payment payment);
}
