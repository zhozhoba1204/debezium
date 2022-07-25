
С помощью запущенного Docker, в ручном режиме установим wal2json. 
Чтобы добраться до косноли Postgres контейнера, для начала найдем ID контейнера и выполним следующий набор команд:
```
docker exec -ti 4a10f43aad19 bash
```
```
$ docker ps
```
```
CONTAINER ID   IMAGE               
c429f6d35017   debezium/connect    
7d908378d1cf   debezium/kafka      
cc3b1f05e552   debezium/zookeeper  
4a10f43aad19   postgres:latest     
```
```
$ docker exec -ti 4a10f43aad19 bash
```
Теперь, когда мы внутри контейнера ставим wal2json:
```
$ apt-get update && apt-get install postgresql-14-wal2json
```
Активируем Debezium
Нам нужен POST запрос данные которого отформатированны в JSON формате. 
JSON определяет параметры коннектора который мы пытаемся создать. 
Поместим данные в файл и будем его использовать с cURL.
```
$ echo '
{
  "name": "demo-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "tasks.max": "1",
    "plugin.name": "wal2json",
    "database.hostname": "db",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "postgres",
    "database.dbname": "postgres",
    "database.server.name": "ARCTYPE",
    "table.include.list": "public.t_user,public.document",
    "transforms": "ts_updated_at",
    "transforms.ts_updated_at.type": "org.apache.kafka.connect.transforms.TimestampConverter$Value",
    "transforms.ts_updated_at.target.type": "Timestamp",
    "transforms.ts_updated_at.field": "updated_at",
    "transforms.ts_updated_at.format": "yyyy-MM-dd'T'HH:mm:ssXXX'Z'",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "false",
    "value.converter.schemas.enable": "false",
    "snapshot.mode": "always"
  }
}
' > debezium.json
```
Теперь можно отправить эту конфигурацию в Debezium
```
$ curl -i -X POST \
         -H "Accept:application/json" \
         -H "Content-Type:application/json" \
         127.0.0.1:8083/connectors/ \
         --data "@debezium.json"
```  
Ответ должен быть со следующим содержанием JSON если это уже не настроенный коннектор.
```
{
  "name": "arctype-connector",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "tasks.max": "1",
    "plugin.name": "wal2json",
    "database.hostname": "db",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "arctype",
    "database.dbname": "postgres",
    "database.server.name": "ARCTYPE",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "false",
    "value.converter.schemas.enable": "false",
    "snapshot.mode": "always",
    "name": "arctype-connector"
  },
  "tasks": [],
  "type": "source"
}
```
