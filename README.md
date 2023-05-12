## Intro

### Task

- Create a service that allows a phone to be booked / returned.
- The following information should also be available for each phone
  - Availability (Yes / No)
  - When it was booked
  - Who booked the phone
- Please use Fonoapi to expose the following information for each phone and create work-around if it
  isn’t working

### Design

### External API

The requirement to use [Fonoapi](https://github.com/shakee93/fonoapi) is very confusing:

- the API is very badly documented (no request or response information is available)
- the existing documentation is not accurate. The term "available option" is confusing,
  the HTTP protocol has either path, request parameters or headers. No options.
- the provided example shows that in order to get info about a phone,
  the HTTP POST method is used. It significantly deviates from the general agreement
  for HTTP services
- the API does not work
- since the API does not work the only solution I have is to "create work-around if it isn't working"
  I can try to reverse-engineer any client (they provided a few) to understand how it works.

Links:

- [intro](https://medium.com/@shakee93/a-free-api-to-get-mobile-device-descriptions-fonoapi-2f3b22cd102a)
- [result](https://github.com/shakee93/fonoapi/blob/master/resultset.md)

### Model

To represent the booking of phone the following models can be used
![Model](docs/overview-1.png)

The model with a Booking table better represents the real use case. It can be used
to see the previous bookings and plan future booking.
But since it was not asked, [this model](docs/jhipster-jdl-booking.jdl) will not be applied.

A simpler model uses in-place editing inside the Phone table. The Phone table has a link
to user to indicate the current owner of the booked phone.
When the `bookedOn` date is empty, the phone is free to be booked. When the `bookedBy` date
is present, it indicates when the phone was booked and the link to user is mandatory
to indicate the current owner.
The phone gets 2 fields (`brand` and `device`) which will be required to call Fonoapi.

### JHipster

[JHipster](https://www.jhipster.tech/) is chosen to create a template
to be expended in the future.

The model is coded in the [JDL file](docs/jhipster-jdl-inplace.jdl)

The usage of JHipster is documented [here](docs/INFO.md)

### Implementation Notes

[Kotlin](https://kotlinlang.org/) is selected as the implementation language.

Fonoapi properties
[are kept in resources](https://github.com/asomov/phone-task/blob/master/src/main/resources/application.properties)
to be flexible

Fonoapi implementation [is done in a separate service](https://github.com/asomov/phone-task/blob/master/src/main/kotlin/com/bt/booking/service/FonoapiService.kt)
to be able to test or mock

[Swagger](https://swagger.io/tools/swagger-ui/) is used to expose the UI for the backend

![Picture1](docs/booking-screen-1.png)

External resources [are started in docker](https://github.com/asomov/phone-task/tree/master/src/main/docker)

Liquibase [is used to inject](https://github.com/asomov/phone-task/blob/master/src/main/resources/config/liquibase/changelog/phones-data.xml) initial data

### Running the app

Start PostgresQL

    cd /src/main/docker
    docker-compose -f postgresql.yml up

Make sure Java 11 is configured:

    ./mvnw --version

    Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
    Java version: 11.0.18, vendor: Ubuntu, runtime: /usr/lib/jvm/java-11-openjdk-amd64
    Default locale: en_US, platform encoding: UTF-8

Start the app

    ./mvnw

    Application 'booking' is running! Access URLs:
    Local:      localhost:8080/
    External:   127.0.1.1:8080/

Open the UI and login with `admin:admin`

![pictire-2](docs/booking-screen-2.png)

Any tool can be used to test (like curl):

```
curl -X 'GET' \
  'http://localhost:8080/api/phones/1001' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTY4NjU0MDQ3M30.BXZ6dwFFUAJxFIeMgmaqtm2WxvSKHmndqu_T2CBRxMkOR0H6BosaUMMa6F7ZILnToVz5xX2dkGStCxXnwqcsjw'
```

Response:

```
{
  "id": 1001,
  "name": "Samsung Galaxy S9",
  "brand": "Samsung",
  "device": "Galaxy S9",
  "bookedOn": "2023-05-10T09:33:00Z",
  "bookedBy": {
    "createdBy": "system",
    "createdDate": null,
    "lastModifiedBy": "system",
    "lastModifiedDate": null,
    "id": 2,
    "login": "user",
    "firstName": "User",
    "lastName": "User",
    "email": "user@localhost",
    "activated": true,
    "langKey": "en",
    "imageUrl": "",
    "resetKey": null,
    "resetDate": null
  },
  "technology": "Not available",
  "bands2g": "Not available",
  "bands3g": "Not available",
  "bands4g": "Not available"
}
```
