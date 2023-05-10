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

The requirement to use [Fonoapi](https://github.com/shakee93/fonoapi)  is very confusing:
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

### Programming Language

[Kotlin](https://kotlinlang.org/) is selected as the implementation language.