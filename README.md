# ProtoCircuits

The project is a standard Maven project, so you can import it to your IDE of choice.

## Running the JAR executable on the client-side

1. On the command line use `java -jar protocircuits-1.0-SNAPSHOT.jar`
2. Open your browser on at http://localhost:8080/ and wait a little bit.
3. The application will open in the _Running View_ with the Experiment 4 waiting to begin uppon clicjing on the Start button.

NOTE: Please ignore other views, since the UI is not responsive as it should be.

## Building and running on production mode

On the command line use `mvn package -Pproduction` and check the targets directory.

## Development and debugging of the application

### Running the application from the command line.
To run from the command line use `mvn spring-boot:run`, and open http://localhost:8080 in your browser.

### Running and debugging the application in Intellij IDEA
- Locate the Application.java class in the Project view. It is in the src folder, under the main package's root.
- Right-click on the Application class
- Select "Debug 'Application.main()'" from the list

After the application has started, you can view it at http://localhost:8080/ in your browser. 

### Running and debugging the application in Eclipse
- Locate the Application.java class in the Package Explorer. It is in `src/main/java`, under the main package.
- Right-click on the file and select `Debug As` --> `Java Application`.

Do not worry if the debugger breaks at a `SilentExitException`. This is a Spring Boot feature and happens on every startup.

After the application has started, you can view it at http://localhost:8080/ in your browser.

## Project structure

- `MainView.java` in `src/main/java` contains the navigation setup.
- `views` package in `src/main/java` contains the server-side Java views of the application.
- `views` folder in `frontend/` contains the client-side HTML and CSS views of the application.
- `simulations` package in `src/main/java` contains the Java classes inherent to the multi-agent system simulation.
