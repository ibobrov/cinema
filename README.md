# job4j_cinema
job4j_cinema is a web application with an emphasis on the server side of the site for purchasing cinema tickets.
I wrote the layout adapted for phones and tablets. All layouts were used under a free license.
Project test coverage percentage - 80%.

**The main logic of the App:**
* registration, sign in, sign out
* displaying all film sessions on today and tomorrow
* displaying the list of sessions for one chosen film
* displaying all films
* displaying full information about one chosen film
* purchasing tickets

Any user can get acquainted with film sessions, as well as with detailed information about all the films that are shown. Purchasing tickets is available only to registered users.

**The database is implemented with the following tables:**
* files - file storage information for film posters
* genres - film genres
* halls - cinema halls
* films - films for distribution
* film_sessions - available sessions
* users - users registered on the site
* tickets - tickets already purchased for certain film sessions

Please note that all database tables, except "tickets" and "users", are scripted and filled with liquibase.

Spring Framework is used as the **main framework**. The pages are developed with Thymeleaf and Bootstrap and **dependencies** are used: PostgreSQL 42.5, Liquibase 4.15 and Sql2o 1.6.

**Environment requirements:** Java 17, PostgreSQL 42.5, Apache Maven 3+

**Used technologies:**
* Java 17
* Maven 3.1
* PostgreSQL 13.2 (deploy), 15 (local)
* Spring Boot 2.7
* Junit Jupiter 5
* AssertJ 3
* Mockito 3.5
* Liquibase 4.15
* Sql2o 1.6
* H2database 2.1
* Thymeleaf 3.0
* Bootstrap 5.3
* Checkstyle 3.1
* JoCoCO 0.8

**How to run the project:**
1. To run the project, you need to clone the project from this repository;
2. Then you need to create a local database "cinema";
3. Specify the login and password for the database you created in the db/liquibase.properties file;
4. Run liquibase to pre-create and autofill tables;
5. Launch the application using one of the following methods:
   5.1 Through the Main class, located in the folder src\main\java\ru\job4j\cinema;
   5.2 Compiling and running the project via maven with mvn spring-boot:run;
   5.3 After building the project via maven and running the built file with the command java -jar job4j_cinema-1.0-SNAPSHOT.jar;
6. Open the page http://localhost:8080/index in the browser;

---

You can contact me using the contacts in my profile.
Also try the live demo [here](https://cinemarailway-production.up.railway.app/).

**App screenshots**

![main_page.JPG](img/main_page.PNG)

![all_sessions_page.JPG](img/all_sessions_page.PNG)

![films_page.JPG](img/films_page.PNG)

![film_info_page.JPG](img/film_page.PNG)

![ticket_page.JPG](img/ticket_page.PNG)

![register_page.JPG](img/register_page.PNG)

![login_page.JPG](img/login_page.PNG)

![another_schema.JPG](img/another_schema.PNG)

![paid_page.JPG](img/paid_page.PNG)

![updated_ticket_page.JPG](img/updated_ticket_page.PNG)

![error_paid.JPG](img/error_paid.PNG)

![error.JPG](img/error.PNG)

![404.JPG](img/404.PNG)

![500.JPG](img/500.PNG)

![login_page_full.JPG](img/login_page_full.PNG)

![register_page_full.JPG](img/register_page_full.PNG)