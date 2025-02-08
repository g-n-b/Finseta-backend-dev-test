# Finseta Backend Tech Test

You are provided with an OpenAPI specification for making and retrieving payments. We would like you to build a service 
that implements the specification.

The service should be written in Java, but you are free to choose a framework.

You should provide instructions on how to run the service in your submission.

When complete please upload it to a public GitHub/GitLab repository and share the link with us.

You do **not** need to:

* Implement authentication
* Implement database persistence, an in memory option of your choice is fine
* Perform any validations other than those listed below

# Acceptance Criteria

## Scenario One: Create valid payment

* Given I am a user
* When I submit a valid payment
  * `amount` > 0.00
  * `currency` is not empty
  * `counterparty.type` is `SORT_CODE_ACCOUNT_NUMBER`
  * `counterparty.sortCode` must be 6 numeric characters
  * `counterparty.accountNumber` must be 8 numeric characters
* Then a HTTP CREATED response should be returned

## Scenario Two: Create invalid payment

* Given I am a user
* When I submit an invalid payment
  * `amount` <= 0.00
  * `currency` is empty
  * `counterparty.type` is empty
  * `counterparty.sortCode` is not 6 numeric characters
  * `counterparty.sortCode` is not 8 numeric characters
* Then a HTTP Bad Request should be returned 

## Scenario Three: Retrieve payments

* Given I am a user
* When I retrieve all payments
* And I enter a list of currencies
* And I enter a minimum amount
* Then a HTTP OK status should be returned
* And a filtered list of payments matching query parameters should be returned 
