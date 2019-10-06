# online-currency-converter

Swagger: http://localhost:8081/v2/api-docs

Desc: Output the given amount in the target currency

API method: GET

sample curl: curl -X GET 'http://localhost:8081/api/currency/convert?amount=1000&source-currency=USD&target-currency=PLN'

Input: 1- amount: required query param, accepts BigDecimal to avoid double fraction loss. 2- source-currency in which the amount is represented: required query param, accepts the standard currencies IDs (USD, EUR, etc). 2- source-currency where the response will be represented:required query param, accepts the standard currencies IDs (USD, EUR, etc).

output:
format: JSON.
sample: { "amount": 3916.09 }

Caching: Embedded redis is used for caching, API average response without caching is 300ms upto 3s depending on many factors and API average response with caching is 15ms upto 50ms.

Notes: The API relies a public free API as live currency converter value: https://free.currconv.com/api/v7/convert
