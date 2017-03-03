## 2 projects to demonstrate that PUT requests with multipart are not working as expected in Zuul

There are 2 Maven projects in this repo:
* gateway: This project runs Zuul and forwards requests to the fileservice project
* fileservice: This is a small restful service that exposes POST and PUT endpoints

### Compile and run the Zuul gateway project

* cd gateway
* mvn clean install
* java -jar target/gateway-0.0.1-SNAPSHOT.jar
* Zuul will run on localhost:8081 and will forward all requests from http://localhost:8081/file/** to http://localhost:8082/file/**

### Compile and run the restful fileservice project

* cd fileservice
* mvn clean install
* java -jar target/gateway-0.0.1-SNAPSHOT.jar
* The service runs on localhost:8082 and has 3 endpoints:
  * POST to "localhost:8082/file/post". Consumes multipart/form-data. Expects 1 form parameter named "file", payload should be a file
  * PUT to "localhost:8082/file/put". Consumes multipart/form-data. Expects 1 form parameter named "file", payload should be a file
  * GET to "localhost:8082/file/put". Returns a String, is just used to demonstrate that the service is running. 

### Test POST and PUT functionality with cURL

* POST a file to fileservice via Zuul: `curl -X POST -H "Content-Type: multipart/form-data" -F "file=@/path/to/file.txt" "http://localhost:8081/file/post"`
  * This will result in a 200 status code and a string will be returned: "post file success"

* PUT a file to fileservice via Zuul: `curl -v -X PUT -H "Content-Type: multipart/form-data" -F "file=@/path/to/file.txt" "http://localhost:8081/file/put"`
  * This will result in a 404 status code

* PUT a file directly to file service: `curl -X PUT -H "Content-Type: multipart/form-data" -F "file=@helloworld.txt" "http://localhost:8082/file/put"`
  * This will result in a 200 status code and a string will be returned: "put file success"

### Further info

When sending a PUT via Zuul, the request looks like this when it hits the fileservice:

> - Path: file/put
> - Header: {user-agent=[curl/7.35.0], accept=[*/*], expect=[100-continue], content-type=[multipart/form-data;boundary=hkBnDNXOcDTwkuL1qLhglF6i4NA2YREd], x-forwarded-host=[localhost:8081], x-forwarded-proto=[http], x-forwarded-prefix=[/file], x-forwarded-port=[8081], x-forwarded-for=[127.0.0.1], accept-encoding=[gzip], content-length=[38], host=[localhost:8082], connection=[Keep-Alive]}
> - Entity: --hkBnDNXOcDTwkuL1qLhglF6i4NA2YREd--

Notice that the Entity is incomplete

However, when sending a PUT directly to fileservice, the request looks like this 

> - Path: file/put
> - Header: {user-agent=[curl/7.35.0], host=[localhost:8082], accept=[*/*], content-length=[203], expect=[100-continue], content-type=[multipart/form-data; boundary=------------------------c1efb86a9054e387]}
> - Entity: --------------------------c1efb86a9054e387
> Content-Disposition: form-data; name="file"; filename="helloworld.txt"
> Content-Type: text/plain
>
> this is my file content
>
>--------------------------c1efb86a9054e387--

Notice that the Entity is complete this time.
