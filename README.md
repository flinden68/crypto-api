# crypto-api
crypto assesment

## Datastore
The datastore used for this project is MongoDb, when installed locally it will run on
default port 27017
```
mongodb://localhost:27017/crypto-store
```

## Run the application
```
http://localhost:8787/
```

## Build a docker image
native in 2.3.0 and higher
```
spring-boot:build-image
```

or via a DockerFile
```
mvn clean install
docker build  -t crypto-api 
```
