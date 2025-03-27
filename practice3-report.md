# 1. CI для распределенной системы.
Уже было выполнено в `practice2`: статический анализатор + автоматическое тестирование.

# 2. Масштабирование компонентов.
Был добавлен балансировщик нагрузки (L7) + репликации компонента. В частности, был добавлен `njinx` + вызывается 3 
репликации микросервиса `export-service` через `docker compose`. Настройки `njinx` доступны в файле 
[nginx-export.conf](nginx-export.conf).

# 3. Централизованное логирование микросервисов.
## ELK
Было добавлено централизованное логирование микросервисов через `ELK` (Elasticsearch, Logstash, Kibana).  
Примеры:
![tasks](/draft3/assets/elkmain.png)
![tasks](/draft3/assets/elkdataview.png)  
![tasks](/draft3/assets/elklogs.png)  
![tasks](/draft3/assets/elksamle.png)  

## Пример логов микросервисов: 
```json
{
  "@timestamp": [
    "2025-03-24T18:16:00.894Z"
  ],
  "@version": [
    "1"
  ],
  "@version.keyword": [
    "1"
  ],
  "level": [
    "INFO"
  ],
  "level_value": [
    20000
  ],
  "level.keyword": [
    "INFO"
  ],
  "logger_name": [
    "hse.dss.controller.ExportController"
  ],
  "logger_name.keyword": [
    "hse.dss.controller.ExportController"
  ],
  "message": [
    "Received export request for taskId: 1"
  ],
  "message.keyword": [
    "Received export request for taskId: 1"
  ],
  "service": [
    "export-service"
  ],
  "service.keyword": [
    "export-service"
  ],
  "thread_name": [
    "http-nio-8083-exec-2"
  ],
  "thread_name.keyword": [
    "http-nio-8083-exec-2"
  ],
  "_id": "oT9eyZUBl5r0M9XHYjfy",
  "_index": "microservices-logs-2025.03.24",
  "_score": null
}
```

```json
{
  "@timestamp": [
    "2025-03-24T18:16:01.417Z"
  ],
  "@version": [
    "1"
  ],
  "@version.keyword": [
    "1"
  ],
  "level": [
    "INFO"
  ],
  "level_value": [
    20000
  ],
  "level.keyword": [
    "INFO"
  ],
  "logger_name": [
    "hse.dss.service.TestService"
  ],
  "logger_name.keyword": [
    "hse.dss.service.TestService"
  ],
  "message": [
    "Exported tests for task 1"
  ],
  "message.keyword": [
    "Exported tests for task 1"
  ],
  "service": [
    "web-backend"
  ],
  "service.keyword": [
    "web-backend"
  ],
  "thread_name": [
    "http-nio-8081-exec-4"
  ],
  "thread_name.keyword": [
    "http-nio-8081-exec-4"
  ],
  "_id": "pj9eyZUBl5r0M9XHZTcO",
  "_index": "microservices-logs-2025.03.24",
  "_score": null
}
```

## Гайд по запуску:
1. Скачать репозиторий.
2. Зайти в командную строку.
3. Запустить docker:  `docker compose up -d --build --scale export-service=3`
4. Пользоваться на: http://localhost:8081/tasks
5. `Kibana` доступна на: http://localhost:5601/
6. Закрыть docker: ctrl + c

# 4. Тесты.
Не были добавлены новые тесты.