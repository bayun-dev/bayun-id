# Обработка ошибок
При работе с API будут возникать ошибки. Ошибка характеризуется несколькими параметрами:

* **Код ошибки** - строковый литерал ```/A-Z_0-9/```, отражающий всю информацию об ошибке.
* **Описание ошибки** - текстовое описание кода ошибки.

## Схема ответа об ошибке
```http request
Content-Type: application/jsons
```

```json
{
  "errors": [
    {
      "code": "ERROR_CODE",
      "description": "text description of the ERROR_CODE."
    }
  ]
}
```

## Коды ошбок
| Код                   | Описание                                   |
|:----------------------|:-------------------------------------------|
| ACCOUNT_BLOCKED       | Account blocked.                           |
| ACCOUNT_DELETED       | Account deleted.                           |
| CREDENTIALS_NOT_FOUND | The provided credentials not found.        |
| DATE_OF_BIRTH_INVALID | The provided date of birth is not valid.   |
| EMAIL_INVALID         | The provided email is not valid.           |
| FIRSTNAME_INVALID     | The provided first name is not valid.      |
| GENDER_INVALID        | The provided gender is not valid.          |
| LASTNAME_INVALID      | The provided last name is not valid.       |
| PASSWORD_INVALID      | The provided password is not valid.        |
| USERNAME_INVALID      | The provided username is not valid.        |
| USERNAME_NOT_OCCUPIED | The provided username is not occupied.     |
| USERNAME_OCCUPIED     | The provided username is already occupied. |
