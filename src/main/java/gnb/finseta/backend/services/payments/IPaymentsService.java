package gnb.finseta.backend.services.payments;

import org.openapitools.model.Payment;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentsService {
    List<Payment> getPayments(@Nullable BigDecimal minAmount, @Nullable List<String> currencies);

    Payment handlePaymentRequest(Payment payment);
}
