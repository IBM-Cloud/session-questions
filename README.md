# Session Questions

## About

This app is meant to collect questions about a topic presented at a conference talk.
Users can ask questions by visiting the site, and clicking the "Ask A Question" button
under the session they attended.  Users can also ask questions by texting their question
to a number assigned to the session.

Admins can create and edit sessions as well as answer questions via email and text message.

## Deploying To BlueMix

We use the 
[Cloud Foundry Maven Plugin](https://github.com/cloudfoundry/cf-java-client/tree/master/cloudfoundry-maven-plugin) 
to deploy the app to BlueMix. Before using Maven to deploy the app you just need to do a small 
amount of configuration so Maven knows your BlueMix credentials. To do this open the 
settings.xml file in ~/.m2. Within the servers element add the code below inserting your credentials.

    <server>
      <id>BlueMix</id>
      <username>user@email.com</username>
      <password>bluemixpassword</password>
    </server>

If you do not see a settings.xml file in ~/.m2 than you will have to create one. To do this just
copy the settings.xml file from /conf into ~/.m2.

This app can be deployed to any Cloud Foundry instance, such as BlueMix.  To deploy the
application to BlueMix run

    mvn -P deploy -Dapp-url=http://session-questions.ng.bluemix.net -Dorg=yourOrg -Dtwilio-accountId=twilioAccountId -Dtwilio-authToken=twilioAuthToken

The application can also be run locally, although receiving text messages may not be possible
if Twilio cannot reach your machine.  In addition you will need an SMTP server in order to 
send emails.  To run locally run

    mvn -P run

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