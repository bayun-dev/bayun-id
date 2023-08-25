# API Методы

## Список доступных методов

### Авторизация
| Name                                          | Description                                                         |
|:----------------------------------------------|:--------------------------------------------------------------------|
| [`/login`](#/login)                           | Авторизирует пользователя по имени пользователя и паролю.           |
| [`/login/availability`](#/login/availability) | Проверяет доступность входа для пользователя по имени пользователя. |

## Подробное описание методов

### `/login`
> Авторизирует пользователя по имени пользователя и паролю.

#### Запрос
```http request
POST /api/login
Content-Type: application/x-www-form-urlencoded
```

#### Параметры
* `username` `string` Username
* `password` `string` Password

#### Результат
Метод возвращает объект, содержащий единственное поле:
* `redirectUrl` `string` url, по которому клиент ДОЛЖЕН перейти для продолжения авторизации.

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:
* `USERNAME_INVALID` The provided username is not valid.
* `PASSWORD_INVALID` The provided password is not valid.
---

### `/login/availability`
> Проверяет доступность входа для пользователя по имени пользователя.

#### Запрос
```http request
POST /api/login/availability
Content-Type: application/x-www-form-urlencoded
```

#### Параметры
* `username` `string` Username

#### Результат
Метод возвращает объект, содержащий следующие поля:
* `available` `boolean` информация о доступности авторизации.
  * `true` авторизация доступна.
  * `false` авторизация недоступна.
* `reason` `string` `optional` причина, по которой авторизация недоступна.
  * `USERNAME_NOT_OCCUPIED` имя пользователя не зарегистрированно.
  * `ACCOUNT_BLOCKED` аккаунт заблокирован. 
  * `ACCOUNT_DELETED` аккаунт удален.

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:  
* `USERNAME_INVALID` The provided username is not valid.
