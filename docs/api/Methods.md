# API Методы

## Доступные методы
Все методы нечувствительны к регистру. 
Мы поддерживаем HTTP-методы `GET` и `POST`.  
Для передачи параметров в запросах используйте:
* `URL query string`
* `application/x-www-form-urlencoded`
* `multipart/form-data`

При успешном вызове будет возвращен JSON-объект, содержащий результат.

### Действия с аккаунтом
* [`me.get`](#me-get) Отдает информацию об аккаунте.
* [`me.save`](#me-save) Изменяет информацию об аккаунте.
* [`me.delete`](#me-delete) Удаляет информацию об аккаунте.
* [`me.emailConfirm`](#me-emailconfirm) Подтверждает адрес электронной почты.

### Авторизация и регистрация
* [`auth.singIn`](#auth-singin) Авторизирует пользователя.
* [`auth.signOut`](#auth-signup) Деавторизирует пользователя.
* [`auth.signUp`](#auth-signup) Регистрирует новый аккаунт.
* [`auth.resetPassword`](#auth-resetpassword) Сброс пароля от аккаунта.

## Подробное описание методов

### `me.get`
> Отдает информацию об аккаунте.

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true,
  "id": "uuid",
  "username": "string",
  "firstName": "string",
  "lastName": "string",
  "avatarId": "string",
  "email": "string",
  "emailConfirmed": true
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `403` `AUTH_RESTART` Restart the authorization process.
* `403` `ACCESS_DENIED` Access denied. If account blocked or deleted.
---

### `me.save`
> Изменяет информацию об аккаунте.

#### Параметры
* `fistName` `string` `optional` New first name
* `lastName` `string` `optional` New last name
* `password` `string` `optional` New password
* `email` `string` `optional` New email
* `avatar` `InputFile` `optional` New avatar
* `dropAvatar` `boolean` `optional` if **true** then drop an avatar

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `400` `INVALID_REQUEST_PARAM` One of the parameters specified was missing or not valid.
* `403` `AUTH_RESTART` Restart the authorization process.
* `403` `ACCESS_DENIED` Access denied. If account blocked or deleted.
---


### `me.delete`
> Удаляет информацию об аккаунте.

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `400` `INVALID_REQUEST_PARAM` One of the parameters specified was missing or not valid.
* `400` `PASSWORD_INVALID` The provided password is not valid.
* `403` `AUTH_RESTART` Restart the authorization process.
* `403` `ACCOUNT_BLOCKED` Account blocked.
* `403` `ACCOUNT_DELETED` Account deleted.
---

### `auth.signIn`
> Авторизирует пользователя.

#### Параметры
* `username` `string` Username
* `password` `string` Password

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true,
  "redirectUri": "uri"
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `400` `INVALID_REQUEST_PARAM` One of the parameters specified was missing or not valid.
* `400` `USERNAME_UNOCCUPIED` The username is not yet being used.
* `400` `PASSWORD_INVALID` The provided password is not valid.
* `403` `ACCOUNT_BLOCKED` Account blocked.
* `403` `ACCOUNT_DELETED` Account deleted.
---

### `auth.signOut`
> Деавторизирует пользователя.

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true
}
```

#### Коды ошибок
В ходе выполнения не могут произойти ошибки.

---

### `auth.signUp`
> Регистрирует новый аккаунт.

#### Параметры
* `username` `string` New account username
* `fistName` `string` New account first name
* `lastName` `string` New account last name
* `password` `string` New account password
* `email` `string` New account email

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `400` `INVALID_REQUEST_PARAM` One of the parameters specified was missing or not valid.
* `400` `USERNAME_OCCUPIED` The username is already in use.

---

### `auth.resetPassword` 
> Сброс пароля от аккаунта.

#### Параметры
* `username` `string` Username

#### Результат
Метод возвращает следующий объект:

```json
{
  "ok": true
}
```

#### Коды ошибок
В ходе выполнения могут произойти следующие ошибки:
* `403` `EMAIL_NOT_CONFIRMED` One of the parameters specified was missing or not valid.