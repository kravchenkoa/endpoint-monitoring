Endpoints Monitoring Service
==
Useful Information
--
- [API](./doc/api.md)

##
## TLDR
- Setting up a non-in-memory database is very time consuming
- Security is hard
- Async monitoring is hard
- Docker is hard
- http://192.168.99.100:8080/api/monitoredEndpoints with `Authorization: Bearer ACCESS_TOKEN` header
- Compile without tests and run `docker-compose up --build`
- Let me know if something's wrong or not working as expected
##
##Debrief

All in all an extremely educating task that took me a lot longer to "complete" than probably intended, but I've learned so much about every aspect of the task, as I was new to anything beyond a simple rest api with in memory db. Overall I've enjoyed the process even through many hardships and numerous failures.

There are a lot of things that I'm still not satisfied with - most importantly not being able to use existing data in db when the application is turned on, and not distinguishing between bad and unauthorized requests, but this was already a huge time investment, so maybe another time!:)
## 
## Docker
To run the application in docker it is necessary to first compile and create the jar with maven **without** running tests. [In IDEA there is a skip tests button in maven tab](https://stackoverflow.com/a/32006543)

Then `docker-compose up --build` starts the database and application in their own containers.

For me `http://192.168.99.100:8080/api/monitoredEndpoints` was the address of the application in docker.
##
## Tests
Tests cause a number of database exceptions when ran all at once, however assertions do not fail somehow. When I run them one by one in IDEA no exceptions are visible.

I didn't figure out a way to run tests in docker or to have tests be runnable while keeping `application.properties` database on docker internal. So in order to run tests I uncomment `docker db external use` and run mysql in docker, or `local db` for a local db. You can then run tests in IDEA (not docker).
