# thesis-chatbot

## Lokale Ausführung des Programms mit maven
### Requirements
Um das Programm lokal ausführen zu können, muss ein .env File im obersten Ordner des Projekts angelegt werden

Das .env File muss folgende Variablen beinhalten:
```
OPENAI_APIKEY =

TOGETHERAI_API_KEY = (hier erhältlich: https://www.together.ai/)

QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://localhost:5432/thesis-db

POSTGRES_USER=gibler

POSTGRES_PASSWORD=connormcgyver
```

Wenn das Programm über Maven gestartet werden soll, wird zusätzlich eine laufende Instanz des Docker-Images pgvector/pgvector:pg14 benötigt:
```shell script
docker pull pgvector/pgvector:pg14
```

```shell script
docker run -e POSTGRES_USER=gibler -e POSTGRES_PASSWORD=connormcgyver -e POSTGRES_DB=thesis-db -p 5432:5432 -d pgvector/pgvector:pg14
```

### Ausführung

Im Anschluss kann mit dem folgenden Befehl das Programm gestartet werden:
```shell script
./mvnw compile quarkus:dev
```


Die zweite Möglichkeit ist das Starten mit Docker-Compose. Hier wird auch das .env File benötigt, anschließend ist lediglich das Ausführen des folgenden Befehls notwendig:
```shell script
docker-compose -f docker-compose-htwg.yaml up -d
```
