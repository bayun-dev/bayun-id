# API Методы

## Список доступных методов

### Действия с аккаунтами
| Name                                        | Description                                        |
|:--------------------------------------------|:---------------------------------------------------|
| [GET `/accounts/{id}`](#get-accountsid)     | Отдает информацию об аккаунте по идентификатору.   |
| [PATCH `/accounts/{id}`](#patch-accountsid) | Изменяет информацию об аккаунте по идентификатору. |

### Авторизация
| Name                                        | Description                                                         |
|:--------------------------------------------|:--------------------------------------------------------------------|
| [`/login`](#login)                          | Авторизирует пользователя по имени пользователя и паролю.           |
| [`/login/availability`](#loginavailability) | Проверяет доступность входа для пользователя по имени пользователя. |

### Регистрация
| Name                                          | Description                                                                         |
|:----------------------------------------------|:------------------------------------------------------------------------------------|
| [`/signup`](#signup)                          | Регистрирует новый аккаунт.                                                         |
| [`/signup/availability`](#signupavailability) | Проверяет доступность регистрации для имени пользователя.                           |

## Подробное описание методов

### GET `/accounts/{id}`
> Отдает информацию об аккаунте по идентификатору.
> 
> Для получения информации об авторизированном аккаунте можно использовать строковый литерал `me` вместо идентификатора аккаунта.

#### Запрос
```http request
GET /api/accounts/{id}
Accept: application/json
```

#### Результат
Метод возвращает объект [Account](#).

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:
* `ACCOUNT_NOT_FOUND` Account not found.
---

### PATCH `/accounts/{id}`
> Изменяет информацию об аккаунте по идентификатору.
>
> Для изменения информации об авторизированном аккаунте можно использовать строковый литерал `me` вместо идентификатора аккаунта.

#### Запрос
```http request
PATCH /api/accounts/{id}
Content-Type: application/x-www-form-urlencoded
Accept: application/json
```

#### Параметры
* `fistName` `string` `optional` New first name
* `lastName` `string` `optional` New last name
* `dateOfBirth` `string` `optional` New date of birth
* `gender` `string` `optional` New gender
* `password` `string` `optional` New password
* `email` `string` `optional` New email

#### Результат
Метод возвращает объект, содержащий единственное поле:
* `changed` `boolean` информация об изменении аккаунта.
  * `true` аккаунт изменен.
  * `false` ни одно из полей не сохранено.

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:
* `ACCOUNT_NOT_FOUND` Account not found.
* `DATE_OF_BIRTH_INVALID` The provided date of birth is not valid.
* `EMAIL_INVALID` The provided email is not valid.
* `FIRSTNAME_INVALID` The provided first name is not valid.
* `GENDER_INVALID` The provided gender is not valid.
* `LASTNAME_INVALID` The provided last name is not valid.
* `PASSWORD_INVALID` The provided password is not valid.
---

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
* `ACCOUNT_BLOCKED` Account blocked.
* `ACCOUNT_DELETED` Account deleted.
* `CREDENTIALS_NOT_FOUND` The provided credentials not found.
* `PASSWORD_INVALID` The provided password is not valid.
* `USERNAME_INVALID` The provided username is not valid.
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
---

### `/signup`
> Регистрирует новый аккаунт.

#### Запрос
```http request
POST /api/signup
Content-Type: application/x-www-form-urlencoded
```

#### Параметры
* `username` `string` New account username
* `fistName` `string` New account first name
* `lastName` `string` New account last name
* `dateOfBirth` `string` New account date of birth
* `gender` `string` New account gender
* `password` `string` New account password
* `email` `string` `optional` New account email

#### Результат
Метод возвращает объект, несодержащий полей.

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:
* `DATE_OF_BIRTH_INVALID` The provided date of birth is not valid.   
* `EMAIL_INVALID` The provided email is not valid.           
* `FIRSTNAME_INVALID` The provided first name is not valid.      
* `GENDER_INVALID` The provided gender is not valid.          
* `LASTNAME_INVALID` The provided last name is not valid.       
* `PASSWORD_INVALID` The provided password is not valid.        
* `USERNAME_INVALID` The provided username is not valid.
* `USERNAME_OCCUPIED` The provided username is already occupied.
---

### `/signup/availability`
> Проверяет доступность регистрации для имени пользователя.

#### Запрос
```http request
POST /api/signup/availability
Content-Type: application/x-www-form-urlencoded
```

#### Параметры
* `username` `string` New username

#### Результат
Метод возвращает объект, содержащий единственное поле:
* `available` `boolean` информация о доступности для регистрации.
  * `true` регистрация доступна.
  * `false` регистрация недоступна.

#### Коды ошибок
В ходе выполнения могут произойти общие ошибки, а так же:
* `USERNAME_INVALID` The provided username is not valid.
