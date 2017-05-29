# Java Autocomplete

Java example with Play Framework, Spring and Elasticsearch

## Install

* clone this project 
* cd to the project's path
* [install docker](https://www.docker.com/community-edition#/download) 
* [install sbt](http://www.scala-sbt.org/download.html) (or uncomment java in docker-compose)
* Check the variables default.url and elastic_host of the application.conf file. If your docker container is in another IP, change it.
* Execute ```docker-compose up```
* For starting the app execute: ```sbt run```
* For executing the tests execute ```sbt test```

## Endpoints

* http://127.0.0.1:9000: Autocomplete page
* http://127.0.0.1:9000/api/games/load-from-json: Load games into MySQL from the json file included in the project.
* http://127.0.0.1:9000/api/games/update-es-index: Read the games from MySQL and update the Elasticsearch with this data.
* http://127.0.0.1:9000/api/games/autocomplete?q=query: Search into Elasticsearch