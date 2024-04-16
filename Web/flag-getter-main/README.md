# Flag getter

### Использование

1. Запускаем `build.sh` для сборки приложения, оно будет доступно по [host/flag-getter-0.0.1-SNAPSHOT/](http://localhost:8080/flag-getter-0.0.1-SNAPSHOT/)
2. Данная уязвимость это ZeroDay, поэтому файл с флагом кладем в любое место
3. Подробнее об уязвимости и как ее проэкслойтировать [здесь](https://www.rapid7.com/blog/post/2022/03/30/spring4shell-zero-day-vulnerability-in-spring-framework/)
4. Так же в проекте есть пример эксплойта `exploit.py` и скрипт для его запуска `run-poc.sh`

###
Приложение построено вокруг уязвимости **Spring RCE (CVE-2022-22965) Proof of Concept**


