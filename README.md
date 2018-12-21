# B64-diff-ed API

JSON Base 64 encoded comparator service. With this API you will be able to register
JSON pairs encoded in Base 64 (https://www.base64encode.org/) and then compare  their decoded content.
The API consist in **3 principal endpoints** (add left data, add right data, and compare them) and **2 
extra enpoints** (Retrieve the existing nodes and clear/delete de added nodes)

This project is made with **Java 8, Maven and Springboot**. The persistence of the JSON pairs
 is achieved through a **Map in memory**. 
 
 Java 8 Base64 library is used for the data encoding and decoding. The **type of Base 64 encoding** is **Simple**.
  
  In this type of encoding the output is mapped to a set of characters lying in ***A-Za-z0-9+/.***
  The encoder does not add any line feed in output, and ***the decoder rejects any character other 
  than A-Za-z0-9+/***
 
The application consist in **Nodes** that contains two branches, ***left data and right data***. Populate
a node with both of them an then you will be able to compare these.
 

## How to run?

- You must have ***JDK (version 8 or later)*** and ***MAVEN*** installed in your computer 
- Download the repo (or clone it with `GIT`)
- Comiple the project opening a terminal and going to the repo folder downloaded, then executing:

	`mvn clean install` 
	
- Afer the compilation, in order to run the microservice (Start the embebed tomcat server in the 
default port) go to repo folder, then */target*, and execute the following command:

	``java -jar b64diffed.jar``
	
- If the compilation and running went well now you should be able to use the AP; (eg.);

    ```http://localhost:8080/v1/diff/1/left```

## API Catalog
If you download the **b64-diffed-catalog.json** file and then import it 
into **postman** you will have the api catalog ready to test. Below, the API catalog
is detailed;

## 1- Add node's data

POST endpoint that populates a node with a json the left branch data of a node, consumes TEXT/PLAIN media type;

`/v1/diff/{node_id}/left`

- **Request Example**

```
http://localhost:8080/v1/diff/1/left
METHOD: POST
CONSUMES: TEXT (text/plain)
BODY (base64 encoded json):

ewoKImNhbmJlcnJhIjogeyJjYW5pbm8iOiJjYXRlbWJ1cnkiLCJjYXJsYSI6ImNhc2NpIn0sCgoiY2FzYSI6ImNhY2EiLAoKImNhc28iOlsiY29zbyIsImNvY28iLCJtYXJpdmkiXQoKCn0=
```
- **Response Example (HTTP STATUS ONLY)**

    *HTTP 201 - Created* 

### Data Validation
- If Node is already created and also has left data, you will get an **HTTP 409**, conflict.
The node shouldn't exists or its left data must be empty (The request does not overwrite 
node's data).
- If the JSON enconded is malformed the endpoint will return **HTTP 409**

## 2- Add node's right data

POST endpoint that populates a node with a json the right branch data of a node, consumes TEXT/PLAIN media type;

`/v1/diff/{node_id}/right`

- **Request Example**

```
http://localhost:8080/v1/diff/1/right
METHOD: POST
CONSUMES: TEXT (text/plain)
BODY (base64 encoded json):

ewoKImNhbmJlcnJhIjogeyJjYW5pbm8iOiJjYXRlbWJ1cnkiLCJjYXJsYSI6ImNhc2NpIn0sCgoiY2FzYSI6ImNhY2EiLAoKImNhc28iOlsiY29zbyIsImNvY28iLCJtYXJpdmkiXQoKCn0=
```
- **Response Example (HTTP STATUS ONLY)**

    *HTTP 201 - Created* 

### Data Validation
- If Node is already created and also has right data, you will get an HTTP 409, conflict.
The node shouldn't exists or its left data must be empty (The request does not overwrite 
node's data).
- If the JSON enconded is malformed the endpoint will return **HTTP 409**

## 3- Differentiate Node's data
GET endpoint that differentiate the node left and right data.
`/v1/diff/{node_id}`

- **Request Example**

```
http://localhost:8080/v1/diff/3
METHOD: GET
```
- **Response Example**

```json
{
    "message": "DIFFERENT DATA",
    "differenceDetail": "someData[2], Expected: something, got: else"
}
```
### Data Validation
- If the Node does not contains data in both sides (left and right) the endpoint will
return **HTTP 409**
- If the Node does not exist the endpoint will return **HTTP 404**

## 4 (extra) - Retrieve the added nodes
Get endpoint that retrieves the existing nodes in memory. This will allow you
to check if a node is enough populated to compare its data.

`/v1/diff/nodes`

- **Request Example**

```
http://localhost:8080/v1/diff/nodes
METHOD: GET
```
- **Response Example**
```json
    [
        {
            "nodeId": 1,
            "rightData": "POPULATED",
            "leftData": "EMPTY"
        },
        {
            "nodeId": 2,
            "rightData": "EMPTY",
            "leftData": "POPULATED"
        }
    ]

```
    
## 5 (extra) - Clear all nodes
DELETE endpoint that delete all the data added.

`/v1/diff/clear`

- **Request Example**

```
http://localhost:8080/v1/diff/clear
METHOD: DELETE
```
- **Response Example (HTTP STATUS ONLY)**

    *HTTP 200 - OK* 