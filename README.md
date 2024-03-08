# Spring Boot 3.2.2/3.2.3 validation message regression

These settings are ineffective for RestController methods which have validation constraints directly on parameters:
```
server.error.include-binding-errors=always
server.error.include-message=always
```

## Instructions

1. Run `./mvnw clean verify`.
2. Observe that the test using the endpoint without the `@Pattern` constraint works as expected. The endpoint with the constraint
   however returns no detailed error message for the `mydata` just as if the `server.error` properties would be set to `never`.
3. Change `spring-boot-starter-parent`version to `3.2.0` or `3.2.1` and rerun the tests. Observe that both cases return
   detailed error messages.

## Response comparison
```
curl -X POST --location "http://localhost:8080/api/dummy/with-constraint" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d '{
"propertyA": ""
}'
```

Response body with Spring Boot 3.2.0 and 3.2.1:
```
{
  "timestamp": "2024-03-08T06:34:00.115+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='requestData'. Error count: 1",
  "errors": [
    {
      "codes": [
        "NotBlank.requestData.propertyA",
        "NotBlank.propertyA",
        "NotBlank.java.lang.String",
        "NotBlank"
      ],
      "arguments": [
        {
          "codes": [
            "requestData.propertyA",
            "propertyA"
          ],
          "arguments": null,
          "defaultMessage": "propertyA",
          "code": "propertyA"
        }
      ],
      "defaultMessage": "must not be blank",
      "objectName": "requestData",
      "field": "propertyA",
      "rejectedValue": "",
      "bindingFailure": false,
      "code": "NotBlank"
    }
  ],
  "path": "/api/dummy/with-constraint"
}
```

Response body with Spring Boot 3.2.2 and 3.2.3:
```
{
  "timestamp": "2024-03-08T06:31:44.778+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failure",
  "path": "/api/dummy/with-constraint"
}
```
