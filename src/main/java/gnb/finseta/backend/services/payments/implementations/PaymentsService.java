package gnb.finseta.backend.services.payments.implementations;

import gnb.finseta.backend.services.payments.IPaymentsService;
import org.openapitools.model.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentsService implements IPaymentsService {
    @Override
    public void getPayments(BigDecimal minAmount, List<String> currencies) {

    }

    @Override
    public void handlePaymentRequest(Payment payment) {

    }
}
