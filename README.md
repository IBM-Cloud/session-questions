# Session Questions

## About

This app is meant to collect questions about a topic presented at a conference talk.
Users can ask questions by visiting the site, and clicking the "Ask A Question" button
under the session they attended.  Users can also ask questions by texting their question
to a number assigned to the session.

Admins can create and edit sessions as well as answer questions via email and text message.

The application is actually composed of three separate modules and produces two separate
applications.  The session-questions-data module contains common code for interacting with the
database and publishing messages through a message queue.  The session-questions-text module
is a web application which has a single API endpoint with the purpose of receiving POST
requests from Twilio about new text messages.  The session-questions-web module contains
the client side code and the REST APIs clients can use to perform
basic CRUD operations on the data.

## Deploying To BlueMix

### Maven

This project uses Maven to build, run, and deploy the app.  You will need to install 
[Maven](http://maven.apache.org/download.cgi) in order to deploy the application.

The app takes advantage of the 
[Cloud Foundry Maven Plugin](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-maven-plugin) 
to provide a consistent way of deploying the app to BlueMix. Before using Maven to deploy 
the app you need to do a small amount of configuration so Maven knows your BlueMix credentials. 
To do this open the settings.xml file in ~/.m2. Within the servers element add the code 
below inserting your credentials.

    <server>
      <id>BlueMix</id>
      <username>user@email.com</username>
      <password>bluemixpassword</password>
    </server>

If you do not see a settings.xml file in ~/.m2 than you will have to create one. To do this just
copy the settings.xml file from [maven install dir]/conf into ~/.m2.

### Twilio

Before deploying, you will also want to create a [Twilio](https://www.twilio.com) account.  
The application uses Twilio to send and receive text messages, so you will need to have a 
Twilio account ID and auth token to deploy the app.  You can create and account and use Twilio, 
in a limited fashion, for free.  A free account should be good enough to test the app with.

When you buy a phone number from Twilio you will be able to specify a REST endpoint Twilio
will use when receiving text messages.  For this application you will want to specify
http://session-questions-text.mybluemix.net/api/twilio.  Make sure you change the host name to
be the correct host name for your deployment.

### Doing The Deploy

After you have installed and configured Maven and have a Twilio account id and auth token you can deploy the
app to BlueMix.  Run the following command in the root of the project replacing yourOrg with your Bluemix organization
and replacing twilioAccountId and twilioAuthToken with your Twilio account ID and auth token.  You probably
also want to change the web-url and text-url to make it something unique.

    mvn -P deploy -Dweb-url=http://session-questions-web.mybluemix.net -Dtext-url=http://session-questions-text.mybluemix.net -Dorg=yourOrg -Dtwilio-accountId=twilioAccountId -Dtwilio-authToken=twilioAccountToken

The Maven deploy profile will do everything for you.  It will create an ElephantSQL, CloudAMQP, SendGrid, 
and Twilio service.  It will upload the jar file to BlueMix and create an application based on the app-url you 
specified.  Finally, it will then bind the services it created to the new application and start the application.

## Running Locally

The application can also be run locally, although there are some limitations to doing so.

* Receiving text messages may not be possible if Twilio cannot reach your machine. 
* You will need an SMTP server in order to send emails.  
* The two separate web apps will not share any data as each will use its own in memory
DB and message queue.  However you can still test the functionality of each independently
of the other.

To run locally run
    
    cd session-questions-text
    mvn -P run

    cd session-questions-web
    mvn -P run

When running locally the web apps will start on random ports that are currently not bring used
on your machine.  Spring Boot will print the port in the logs when the application starts.

    Tomcat started on port(s): 50107/http

## Dependencies

* [Spring Boot](http://projects.spring.io/spring-boot/)
* [Java Mail](http://www.oracle.com/technetwork/java/javamail/index.html)
* [Twilio SDK](https://www.twilio.com/docs/java/install)
* [Spring Cloud](https://github.com/spring-projects/spring-cloud)
* [MySQL Connector](http://dev.mysql.com/downloads/connector/j/)
* [PostgreSQL Driver](http://jdbc.postgresql.org/)
* [Spring Rabbit](http://projects.spring.io/spring-amqp/)
* [Bootstrap](http://getbootstrap.com/)
* [Angular JS](https://angularjs.org/)
* [Bootstrap Angular JS Module](http://angular-ui.github.io/bootstrap/)

## License
See the LICENSE file in the root of the project.