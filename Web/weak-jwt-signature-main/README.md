# Общая информация о приложении

## Запуск микросервиса

Перед сборкой образа приложения в src/main/resources/application.yaml задать два параметра:
1. Пароль админа по умолчанию **SEC_PASSWORD** - пример, Q6Ko0154xwdVURWZvXKq
2. Значение секретного флага **CTF_SECRET_FLAG** - пример, odna_oshibka_i_ty_oshibsya

Для сборки докер образа _weak-jwt-signature_ в корне проекта выполнить скрипт:
```shell
./image-create.sh
```

Для запуска докер контейнера _weak-jwt-signature_ в корне проекта выполнить скрипт:
```shell
./image-run.sh
```

[Стартовая страница приложения](http://localhost:8080/swagger-ui/index.html#/)

## Анализ уязвимости

Основная суть уязвимости состоит в слабой подписи для JWT токена и наличия практической возможности восстановления секретного ключа из исходного и подписанного текста.
Имея секретный ключ и зная алгоритм подписи, можно осуществить подмену токена в запросе, требующего наличия расширенных прав у пользователя.

В документации swagger содержится endpoint для HTTP GET запроса флага по URI **/flag**. Таким образом можно провести примерно следующую последовательность действий:
1. Выполнить запрос получения флага HTTP GET **/flag** - в результате ошибка доступа 403. 
2. Зарегистрировать пользователя, после чего аутентифицировать и получить токен доступа.
3. Еще раз попытаться выполнить запрос на получение флага - все еще доступ запрещен 403.
4. Выполнить запрос на получение ролей, в ответе - ADMIN, USER, GUEST.
5. Итог - однозначно требуется роль ADMIN, чтобы получить доступ к флагу.

Следующим шагом следует провести анализ самого токена, поскольку он имеет странное кол-во символов в подписи - оно равно сумме кол-ва символов хедера и тела.
Используя стандартный онлайн декодер [ссылка](https://jwt.io/), смотрим структуру токена на примере пользователя testing:

Исходный токен: BASE64 URL кодировка
```base64
eyJhbGciOiIzMTItT1JYIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ0ZXN0aW5nIiwicm9sIjoiVVNFUiIsImlhdCI6IjE3MTE2NDM4MzQyOTEiLCJleHAiOiIxNzExNjU0NjM0MjkxIn0.Eh5BUAkTWF9bXlRRKiomSUdVAxEaUEdOPy8uCEceB0cHUQdbFkcXGxYASQseAUlBUAoYPl9bITYmUEdYHwwYUUdZQ1RDX1RPQFZbS1FGR1RbC0cGSBVWXEdIRVRSRFBAWVZNUVxSDw
```

HEADER:ALGORITHM & TOKEN TYPE
```json
{
  "alg": "312-ORX", 
  "typ": "JWT"
}
```

PAYLOAD:DATA
```json
{
 "sub": "testing",
 "rol": "USER",
 "iat": "1711643834291",
 "exp": "1711654634291"
}
```

Основная суть подсказки состоит в именовании алгоритма подписи **312-ORX** по сути это:
1. Перестановка по тройкам, где 1 => 3, 2 => 1, 3 => 2.
2. Инвертировав ORX с учетом описанной выше перестановки, получим XOR - это второе преобразование текста.
3. Конкатенировав header и payload токена и выполнив последовательно шаг1 и шаг2 - получим исходную подпись.

Для восстановления секретного ключа необходимо выполнить следующую последовательность действий:
1. Декодировать заголовок и исходный текст, после чего конкатенировать их в один массив.
2. Сделать Перестановку по тройкам, где 1 => 3, 2 => 1, 3 => 2.
3. Сделать XOR полученного массива и основной подписи, учитывая что по длине они равны.
4. Результатом должен быть повторяющий xor ключ **secretkey**.

Резюмируя все вышесказанное, можно написать примерно следующий скрипт восстановления ключа:

```python
import base64


def getRawTextSignature(token: str):
    # Разбиваем токен на три части: заголовок, тело и подпись
    parts = token.split('.')

    # Декодируем каждую часть
    header = base64.urlsafe_b64decode(parts[0] + "==").decode("utf-8")
    payload = base64.urlsafe_b64decode(parts[1] + "==").decode("utf-8")
    signature = base64.urlsafe_b64decode(parts[2] + "==").decode("utf-8")

    # Возвращаем заголовок, тело и подпись
    return header, payload, signature


def xor_decrypt(str1: str, str2: str):
    # если кол-во символов не совпадает - ошибка
    if len(str1) != len(str2):
        raise ValueError("Strings must be of equal length")

    # выполняем xor по-байтово
    result = ""
    for char1, char2 in zip(bytes(str1, 'utf-8'), bytes(str2.encode("utf-8"))):
        result += chr(char1 ^ char2)

    return result


def triple_permutation(input_str: str):
    # ивертируем исходную строку в массив
    result = list(input_str)

    # Проходим по строке с шагом 3
    for i in range(0, len(input_str) - 2, 3):
        # Внутри каждой тройки происходит перестановка символов
        # Если символа недостаточно, он остается на месте
        temp = result[i]
        result[i] = input_str[i+2]  # символ на первой позиции
        result[i+2] = input_str[i+1]  # символ на третьей позиции
        result[i+1] = temp  # символ на второй пиции

    return "".join(result)


if __name__ == "__main__":
    token = "eyJhbGciOiIzMTItT1JYIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ0ZXN0aW5nIiwicm9sIjoiVVNFUiIsImlhdCI6IjE3MTE2NDM4MzQyOTEiLCJleHAiOiIxNzExNjU0NjM0MjkxIn0.Eh5BUAkTWF9bXlRRKiomSUdVAxEaUEdOPy8uCEceB0cHUQdbFkcXGxYASQseAUlBUAoYPl9bITYmUEdYHwwYUUdZQ1RDX1RPQFZbS1FGR1RbC0cGSBVWXEdIRVRSRFBAWVZNUVxSDw"
    raw_header, raw_payload, raw_signature = getRawTextSignature(token)
    raw_text = raw_header + raw_payload
    assert(raw_text == '{"alg":"312-ORX","typ":"JWT"}{"sub":"testing","rol":"USER","iat":"1711643834291","exp":"1711654634291"}')
    print("1. исходный текст:")
    print(raw_text)
    print()

    triple_permutation_result = triple_permutation(raw_text)
    assert(triple_permutation_result == 'a{""lg3:"-12XOR"",pty"":TJW{"}u"s:b"e"tist"ngr,""olU:"RSE"",tia"":117416338942,1"x"e:p"7"1611654234"91}')
    print("2. перестановка по тройкам:")
    print(triple_permutation_result)
    print()

    secret_key_result = xor_decrypt(triple_permutation_result, raw_signature)
    assert(secret_key_result == 'secretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecr')
    print("3. секретный ключ")
    print(secret_key_result)
    print()
```

В результате получим строку вида: secretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecretkeysecr.
Зная секретный ключ "secretkey" можем осуществить подмену токена, заменив роль USER на ADMIN и подписав полученный текст. В исходный скрипте при этом потребуется
добавить метод xor исходного текста и секретного ключа.

```python
import base64


def getRawTextSignature(token: str):
    # Разбиваем токен на три части: заголовок, тело и подпись
    parts = token.split('.')

    # Декодируем каждую часть
    header = base64.urlsafe_b64decode(parts[0] + "==").decode("utf-8")
    payload = base64.urlsafe_b64decode(parts[1] + "==").decode("utf-8")
    signature = base64.urlsafe_b64decode(parts[2] + "==").decode("utf-8")

    # Возвращаем заголовок, тело и подпись
    return header, payload, signature


def xor_encrypt(raw_text: str, key: str):
    key_bytes = key.encode("utf-8")
    raw_text_bytes = raw_text.encode("utf-8")

    result = ""
    # выполняем xor по-байтово
    for i in range(len(raw_text_bytes)):
        result += chr(raw_text_bytes[i] ^ key_bytes[i%len(key_bytes)])

    return result


def triple_permutation(input_str: str):
    # ивертируем исходную строку в массив
    result = list(input_str)

    # Проходим по строке с шагом 3
    for i in range(0, len(input_str) - 2, 3):
        # Внутри каждой тройки происходит перестановка символов
        # Если символа недостаточно, он остается на месте
        temp = result[i]
        result[i] = input_str[i+2]  # символ на первой позиции
        result[i+2] = input_str[i+1]  # символ на третьей позиции
        result[i+1] = temp  # символ на второй пиции

    return "".join(result)


def make_token(header: str, payload: str, sign: str):
  # получаем url base64 значение каждой из частей токена
  header_encoded = base64.urlsafe_b64encode(header.encode("utf-8")).decode("utf-8").rstrip("=")
  payload_encoded = base64.urlsafe_b64encode(payload.encode("utf-8")).decode("utf-8").rstrip("=")
  signature_encoded = base64.urlsafe_b64encode(sign.encode("utf-8")).decode("utf-8").rstrip("=")

  return f"{header_encoded}.{payload_encoded}.{signature_encoded}"


if __name__ == "__main__":
    token = "eyJhbGciOiIzMTItT1JYIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ0ZXN0aW5nIiwicm9sIjoiVVNFUiIsImlhdCI6IjE3MTE2NDM4MzQyOTEiLCJleHAiOiIxNzExNjU0NjM0MjkxIn0.Eh5BUAkTWF9bXlRRKiomSUdVAxEaUEdOPy8uCEceB0cHUQdbFkcXGxYASQseAUlBUAoYPl9bITYmUEdYHwwYUUdZQ1RDX1RPQFZbS1FGR1RbC0cGSBVWXEdIRVRSRFBAWVZNUVxSDw"
    raw_header, raw_payload, raw_signature = getRawTextSignature(token)
    raw_text = raw_header + raw_payload
    assert(raw_text == '{"alg":"312-ORX","typ":"JWT"}{"sub":"testing","rol":"USER","iat":"1711643834291","exp":"1711654634291"}')
    print("1. исходный текст:")
    print(raw_text)
   
    raw_paylod_fake = raw_payload.replace("USER", "ADMIN")
    assert(raw_paylod_fake == '{"sub":"testing","rol":"ADMIN","iat":"1711643834291","exp":"1711654634291"}')
    print("2. подмененный текст:")
    print(raw_paylod_fake)
    print()

    triple_permutation_result = triple_permutation(raw_header + raw_paylod_fake)
    assert(triple_permutation_result == 'a{""lg3:"-12XOR"",pty"":TJW{"}u"s:b"e"tist"ngr,""olA:"IDM,N"a"i:t"7"1611843234"91e,""xp1:"171465463129"}')
    print("3. перестановка по тройкам:")
    print(triple_permutation_result)
    print()

    signature_fake = xor_encrypt(triple_permutation_result, "secretkey")
    fake_token: str = make_token(raw_header, raw_paylod_fake, signature_fake)
    assert(fake_token == 'eyJhbGciOiIzMTItT1JYIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ0ZXN0aW5nIiwicm9sIjoiQURNSU4iLCJpYXQiOiIxNzExNjQzODM0MjkxIiwiZXhwIjoiMTcxMTY1NDYzNDI5MSJ9.Eh5BUAkTWF9bXlRRKiomSUdVAxEaUEdOPy8uCEceB0cHUQdbFkcXGxYASQseAUlBUAoYKl9bOiEuXitWCkcQSRFBRUdFXVRIS1FQQFZASVxIFklBUB0EWl9bQlJSRlNBX1NKQldaUBg')
    print("4. поддельный токен")
    print(fake_token)
    print()
```

Подставив итоговое значение поддельного токена мы получим права администратора. Сделав соответствующий запрос на эндпоинт GET /flag
извлекаем секретный флаг **odna_oshibka_i_ty_oshibsya**. Теперь у нас есть власть и доступ не только к флагу, но и данным любого другого пользователя:)
