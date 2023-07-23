<h1 align="center">Welcome to the History Microservice 👋</h1>
<p>
</p>

> The microservice is a part of the Mediscreen project. It contains all the patient notes.

## Versions
- Spring Boot: 3.1.2
- Maven: 3.1.2
- JDK: 17

## Run the app

To launch the app, you can run it locally or use docker.

### Run with docker


### Run local

Launch the database :
1. Make sure that you have MongoDB installed.
2. Ensure that the MongoDB server is running.
3. Import the data from the file in resources/mongodb_data.ndjson

This database is available on the configured server address : http://localhost:27017

Launch the app :
1. Make sure you have the required versions of Java and dependencies installed.
2. Open a terminal or command prompt and navigate to the project directory.
3. Run the following command to build the project and create an executable JAR file:
   ` mvn package`
4. Once the build is successful, you can launch the app using the following command:
   ` java -jar target/history-0.0.1-SNAPSHOT.jar `
   This will start the app on the configured server address : http://localhost:8082

## Testing

1. Run the command for testing: `mvn test`
2. The jacoco report will be generated in target/site/index.html

## Endpoints

You can check the endpoints with requirements on Swagger : 
http://localhost:8082/swagger-ui/index.html

## Curls
Note: Make sure the app and the database are running

- `curl -d "patId=11&patient=TestNone&notes=Patient states that they are 'feeling terrific' Weight at or below recommended level" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=12&patient=TestBorderline&notes=Patient states that they are feeling a great deal of stress at work Patient also complains that their hearing seems Abnormal as of late" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=12&patient=TestBorderline&notes=Patient states that they have had a Reaction to medication within last 3 months Patient also complains that their hearing continues to be problematic" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=13&patient=TestInDanger&notes=Patient states that they are short term Smoker " -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=13&patient=TestInDanger&notes=Patient states that they quit within last year Patient also complains that of Abnormal breathing spells Lab reports Cholesterol LDL high" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=14&patient=TestEarlyOnset&notes=Patient states that walking up stairs has become difficult Patient also complains that they are having shortness of breath Lab results indicate Antibodies present elevated Reaction to medication" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=14&patient=TestEarlyOnset&notes=Patient states that they are experiencing back pain when seated for a long time" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=14&patient=TestEarlyOnset&notes=Patient states that they are a short term Smoker Hemoglobin A1C above recommended level" -X POST http://localhost:8082/patHistory/add`
- `curl -d "patId=14&patient=TestEarlyOnset&notes=Patient states that Body Height, Body Weight, Cholesterol, Dizziness and Reaction" -X POST http://localhost:8082/patHistory/add`
