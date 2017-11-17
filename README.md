Kurzbeschreibung
================

Verwendete Anleitung: <https://jersey.github.io/documentation/latest/getting-started.html>

Projekt wurde erzeugt mit:

    $ mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
    -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
    -DgroupId=searchpp -DartifactId=searchpp -Dpackage=searchpp \
    -DarchetypeVersion=2.26

Projekt starten mit:

    $ mvn exec:java

Seite aufrufen:

    $ curl http://localhost:8080/myapp/myresource
    $ firefox http://localhost:8080/myapp/myresource

Google-Login-Example
====================

<http://blog.sodhanalibrary.com/2014/11/login-with-google-with-java-tutorial.html>
