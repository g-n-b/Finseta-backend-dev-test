# Submission for Finseta Technical Assessment

By [Taylor Norton-Brown](https://linkedin.com/in/tgnb)

## The Project

Uses OpenApi to generate the controller and client facing DTOs to ensure consistency.
It will create a java implementation off the publicly available swagger doc [here](                                https://raw.githubusercontent.com/fxpress/backend-tech-test/refs/heads/main/openapi.yaml
).

### Compiling

```bash
mvn clean install
```

### Running

```bash
# In the base dir
mvn spring-boot:run
```

### Testing

#### Using CRUD

```bash
# Create a payment
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"currency": "GBP", "amount": 10.00, "counterparty": {"type": "SORT_CODE_ACCOUNT_NUMBER", "accountNumber": "12345678", "sortCode": "123456"}}' http://localhost:8080/payments
```

```bash
# Get Payments
curl -X GET \
  'http://localhost:8080/payments?minAmount=10.00&currencies=GBP,EUR'
```

#### Automated tests

Testing using JUnit5.0 happens on a 
```bash
mvn clean install
```