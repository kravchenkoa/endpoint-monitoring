# API
- [Authorization](#authorization)
- [Monitoring Results](#monitoring-results)
- [Invalid Requests](#invalid-requests)
##
## Authorization
- Authentication and authorization is done in **every** request based on `Authorization` header with value of `Bearer ACCESS_TOKEN` where `ACCESS_TOKEN` is one of the two specified tokens
    - `93f39e2f-80de-4033-99ee-249d92736a25`
    - `dcb20f8a-5657-4f1b-9f7f-ce65739b359e`
- On invalid authentication a response with a 401 status (Unauthorized) is sent.
- **All** authenticated requests return a 200 (OK) status (barring a server error or requesting unsupported methods). If the user is not authorized to view the specified endpoint or result an empty body is returned.
##
## API
All requests that send data must have `Content-Type: application/json` header
#### `/api/monitoredEndpoints`	
- `GET`
    - Returns all endpoints
- `POST`
    - Creates a new endpoint from passed JSON containing `url`. User can optionally specify `name` and/or `monitoringInterval`. Other `MonitoredEndpoint` attributes are ignored
        - Examples:
            - `{url: "http://google.com"}`
            - `{url: "http://google.com", name: "google", monitoringInterval: 1}`
       - `id` generated automatically
        - `dateOfCreation` generated automatically
        - `dateOfLastCheck` modified by the application only
        - `monitoringUser` filled in automatically as current user
    - Inserted monitoredEndpoint is returned

#### `/api/monitoredEndpoints/{id}`
- `GET`
    - Returns the endpoint for current user
- `PUT`
    - Updates the endpoint from passed JSON. Only `name` and `monitoringInterval` are updated. To change the `url` it is necessary to create a new endpoint (as to prevent mixing results of different websites).
    - Updated monitoredEndpoint is returned
- `DELETE`
    - Deletes the endpoint. Returns number of deleted monitoring results. 

#### `/api/monitoredEndpoints/{id}/results`
- `GET`
    - Returns last 10 results for specified endpoint. 

##
## Invalid Requests
- Unauthenticated requests return a 401 status
- Unauthorized requests return a 200 status and an empty body
- `GET` requests on non existing endpoints return an empty body
- `POST` requests return `null` on invalid `url` (functionality of the url is not tested, just validity), and if `monitoringInterval` is bigger than zero.
- `PUT` requests are checked only if `monitoringInterval` is bigger than zero.
- `DELETE` requests return `-1` if unauthorized or specified endpoint does not exist