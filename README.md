# user-ballance-service

Сервис управления пользователями, их контактами, балансами и переводами.  
Тестовое задание, выполненное на **Java 11** с использованием **Spring Boot 2.7.18**.

[![GitHub](https://img.shields.io/badge/github-repo-blue)](https://github.com/твой-логин/user-ballance-service)

## 📚 Стек

- **Java 11**
- **Spring Boot 2.7.18** (Web, Data JPA, Security, Validation, Cache, Redis)
- **PostgreSQL 15** (основная БД)
- **Redis 7** (кэширование)
- **Liquibase** (миграции)
- **JWT** (jjwt 0.11.5) — аутентификация и авторизация
- **Swagger** (springdoc-openapi-ui 1.7.0)
- **Testcontainers Cloud** (интеграционные тесты с PostgreSQL)
- **JUnit 5 + Mockito** (unit-тесты)
- **Maven** (сборка)

## 🏗️ Архитектура

Проект построен по трёхслойной архитектуре:

- **API** (`controller`) – REST-контроллеры, DTO, валидация входных данных
- **Service** (`service`) – бизнес-логика, проверки, кэширование
- **DAO** (`repository`) – доступ к данным через Spring Data JPA

## 🗄️ Модель данных
users
├── id (PK)
├── name
├── date_of_birth
└── password

account
├── id (PK)
├── user_id (FK → users.id, UNIQUE)
├── balance
└── initial_balance

email_data
├── id (PK)
├── user_id (FK → users.id)
└── email (UNIQUE)

phone_data
├── id (PK)
├── user_id (FK → users.id)
└── phone (UNIQUE)


- У каждого пользователя строго один счёт
- Минимум по одному email и телефону
- Баланс не может быть отрицательным

## 🔑 Аутентификация и авторизация

- `POST /auth/login` – принимает `email+password` или `phone+password`, возвращает JWT
- JWT содержит только claim `user_id`
- Все защищённые эндпоинты требуют заголовок `Authorization: Bearer <token>`

## 📋 Основные возможности

### Управление контактами (только свои)
- Добавить / удалить / обновить email (уникальность проверяется)
- Добавить / удалить / обновить телефон (аналогично)
- Нельзя удалить последний email или телефон

### Поиск пользователей (доступен всем авторизованным)
- `GET /users/search?name=...&dateOfBirth=...&phone=...&email=...&page=0&size=20`
- Фильтры:
    - `name` – LIKE `prefix%` (регистронезависимый)
    - `dateOfBirth` – больше переданной даты
    - `phone` – точное совпадение
    - `email` – точное совпадение

### Переводы между пользователями
- `POST /transfer { "toUserId": 2, "value": 100.00 }`
- Отправитель определяется из JWT
- Пессимистическая блокировка (`SELECT ... FOR UPDATE`)
- Валидации: сумма > 0, нельзя себе, достаточно средств

### Начисление процентов
- Каждые 30 секунд баланс увеличивается на 10%
- Лимит: не более 207% от начального депозита (`initial_balance`)

### Кэширование (Redis)
- `GET /users/me` кэшируется (TTL 10 минут)
- При изменении контактов кэш сбрасывается

## 📖 Swagger UI

После запуска приложения доступен по адресу:  
`http://localhost:8080/swagger-ui/index.html`

Там же можно авторизоваться (кнопка **Authorize**) и тестировать все эндпоинты.

## 🚀 Локальный запуск

### Требования
- **Java 11**
- **PostgreSQL 15** (локально или через Docker)
- **Redis 7** (локально или через Docker)
- **Testcontainers Cloud** (для интеграционных тестов) – см. ниже

### Настройка БД
Создайте базу данных `user_balance_db` в PostgreSQL (владелец `postgres`, пароль `postgres`).  
Либо используйте Docker-контейнеры:

```bash
docker-compose up -d   # поднимает postgres и redis
```
### Запуск приложения

```bash
./mvnw clean package -DskipTests
java -jar target/user-ballance-service-1.0-SNAPSHOT.jar
```

Или в IDE запустите `UserBallanceServiceApplication`.

### Тестовые пользователи (создаются миграциями)

| Имя         | Email              | Телефон       | Пароль   |
|-------------|--------------------|---------------|----------|
| Иван Иванов | ivan@example.com   | 375291234567  | password |
| Петр Петров | petr@example.com   | 375291234568  | password |

## 🧪 Тестирование

### Unit-тесты
```bash
mvn test -Dtest=TransferServiceTest
```

### Интеграционные тесты (Testcontainers Cloud)

Для выполнения тестов с реальной PostgreSQL требуется **Testcontainers Cloud**, который запускает контейнеры в облаке и не зависит от версии локального Docker.

1. Установите [Testcontainers Desktop](https://testcontainers.com/desktop/)
2. Запустите приложение, войдите (можно использовать Docker ID)
3. В трее выберите **"Run with Testcontainers Cloud"**
4. Запустите тест:

```bash
mvn test -Dtest=UserSearchIntegrationTest
```

Если Testcontainers Cloud не настроен, тест автоматически попытается использовать локальный Docker (требуется Docker 28.x, несовместим с Docker 29).

## 📁 Структура проекта

```
src/
├── main/
│   ├── java/by/staryhroft/userbalance/
│   │   ├── config/          # SecurityConfig, CacheConfig, SwaggerConfig, GlobalExceptionHandler
│   │   ├── controller/      # AuthController, UserController, TransferController
│   │   ├── dto/             # LoginRequest, SearchRequest, UserDto, ...
│   │   ├── entity/          # User, Account, EmailData, PhoneData
│   │   ├── exception/       # Кастомные исключения
│   │   ├── repository/      # Spring Data JPA репозитории
│   │   ├── scheduler/       # InterestScheduler
│   │   ├── security/        # JwtTokenProvider, JwtAuthenticationFilter
│   │   └── service/         # UserService, ContactService, TransferService, InterestService
│   └── resources/
│       ├── application.yml
│       └── db/changelog/    # Liquibase миграции
└── test/
    ├── java/.../
    │   ├── controller/      # UserSearchIntegrationTest
    │   └── service/impl/    # TransferServiceTest
    └── resources/
        ├── application-test.properties   # настройки для H2 (запасной вариант)
        └── testcontainers.properties    # отключение ryuk (опционально)
```

## ⚠️ Примечания

- Проект разработан для **Java 11**, не использует text blocks и другие фичи более новых версий.
- При использовании Testcontainers Cloud убедитесь, что приложение запущено и активно подключение.
```