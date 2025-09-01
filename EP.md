Отлично! Как системный аналитик, я разобью всю задачу **по эндпоинтам и событиям**, чтобы команда разработчиков могла реализовывать функциональность **итеративно, по сервисам и контекстам**, с чётким пониманием входов, выходов и ответственности.

---

## 📌 **Разделение по эндпоинтам и событиям (по микросервисам)**

Каждый эндпоинт — это **входная точка** в сервис.  
Каждое событие — это **выходная точка**, которая запускает следующий шаг в SAGA.

---

## ✅ 1. `CreditApplicationService`
**Назначение**: Управление жизненным циклом заявки, координация SAGA.

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание | Тело запроса | Ответ |
|------|------|--------|--------------|-------|
| `POST` | `/api/credit-applications` | Создание заявки | `CreateApplicationRequest` | `201 Created`, `application_id` |
| `GET`  | `/api/credit-applications/{id}` | Получение статуса заявки | — | `CreditApplicationResponse` |
| `PUT`  | `/api/credit-applications/{id}/cancel` | Отмена заявки (ручная) | — | `200 OK` |

#### 📥 **Request: CreateApplicationRequest**
```json
{
  "customer_id": "uuid",
  "amount": 500000,
  "term_months": 24
}
```

#### 📤 **Response: CreditApplicationResponse**
```json
{
  "application_id": "uuid",
  "customer_id": "uuid",
  "amount": 500000,
  "term_months": 24,
  "status": "CREATED",
  "created_at": "2025-04-05T10:00:00Z"
}
```

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема (Topic) | Когда публикуется | Тело |
|--------|--------------|------------------|------|
| `CREDIT_APPLICATION_CREATED` | `credit.applications.created` | После сохранения в БД + Outbox | `{ "application_id", "customer_id", ... }` |
| `CUSTOMER_VERIFICATION_REQUESTED` | `customer.verification.requested` | После создания заявки (шаг 1 SAGA) | `{ "application_id", "customer_id" }` |
| `SCORING_REQUESTED` | `scoring.requested` | После успешной верификации | `{ "application_id", "customer_id", "amount", "term_months" }` |
| `FUNDS_RESERVATION_REQUESTED` | `funds.reservation.requested` | После успешного скоринга | `{ "application_id", "amount" }` |
| `NOTIFICATION_REQUESTED` | `notification.requested` | После резервации средств | `{ "application_id", "customer_id", "loan_terms" }` |
| `LOAN_CREATED` | `loan.created` | После отправки уведомления | `{ "application_id", "loan_id", "amount", "interest_rate", "start_date" }` |

---

### 🟥 События (Kafka — входящие, обработка)

| Событие | Тема | Обработчик | Действие |
|--------|------|-----------|---------|
| `customer.verification.completed` | `customer.verification.completed` | `OnCustomerVerifiedHandler` | Переход к скорингу |
| `scoring.approved` | `scoring.approved` | `OnScoringApprovedHandler` | Запрос резервации средств |
| `scoring.rejected` | `scoring.rejected` | `OnScoringRejectedHandler` | Откат SAGA |
| `funds.reservation.completed` | `funds.reservation.completed` | `OnFundsReservedHandler` | Запрос отправки уведомления |
| `funds.reservation.failed` | `funds.reservation.failed` | `OnFundsReservationFailedHandler` | Откат SAGA |
| `notification.sent` | `notification.sent` | `OnNotificationSentHandler` | Создание кредита |
| `loan.activated` | `loan.activated` | `OnLoanActivatedHandler` | Завершение SAGA, статус `COMPLETED` |

---

## ✅ 2. `CustomerVerificationService`
**Назначение**: Проверка клиента (паспорт, ЧС, внешние данные)

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание |
|------|------|--------|
| `POST` | `/internal/verify` | Внутренний вызов от CreditApp (если нужен синхронный ответ) | *(опционально, лучше только через Kafka)* |

> ⚠️ Рекомендуется **только Kafka**, без REST от других сервисов.

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема | Тело |
|--------|------|------|
| `CUSTOMER_VERIFICATION_COMPLETED` | `customer.verification.completed` | `{ "application_id", "status": "APPROVED", "details": { ... } }` |
| `CUSTOMER_VERIFICATION_FAILED` | `customer.verification.failed` | `{ "application_id", "reason": "BLACKLISTED" }` |

---

### 🟥 События (Kafka — входящие)

| Событие | Тема | Обработчик |
|--------|------|-----------|
| `credit.applications.created` | `credit.applications.created` | `VerifyCustomerOnApplicationCreated` |

---

## ✅ 3. `ScoringService`
**Назначение**: Анализ платёжеспособности, запрос в БКИ, принятие решения

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание |
|------|------|--------|
| `POST` | `/internal/score` | (опционально) синхронный скоринг | |

> ⚠️ Основной поток — через Kafka.

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема | Тело |
|--------|------|------|
| `SCORING_APPROVED` | `scoring.approved` | `{ "application_id", "score": 680, "decision": "APPROVED_AUTO" }` |
| `SCORING_REJECTED` | `scoring.rejected` | `{ "application_id", "reason": "HIGH_DEBT" }` |
| `SCORING_PENDING_MANUAL_REVIEW` | `scoring.pending.manual.review` | `{ "application_id", "details": "..." }` |

---

### 🟥 События (Kafka — входящие)

| Событие | Тема | Обработчик |
|--------|------|-----------|
| `scoring.requested` | `scoring.requested` | `RunScoringOnRequested` |

---

## ✅ 4. `PaymentService`
**Назначение**: Резервация и выдача средств

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание |
|------|------|--------|
| `POST` | `/api/reservations` | Резервация средств (если нужен синхронный вызов) | |

> ⚠️ Основной интерфейс — Kafka.

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема | Тело |
|--------|------|------|
| `FUNDS_RESERVATION_COMPLETED` | `funds.reservation.completed` | `{ "application_id", "reservation_id", "amount" }` |
| `FUNDS_RESERVATION_FAILED` | `funds.reservation.failed` | `{ "application_id", "reason": "INSUFFICIENT_FUNDS" }` |
| `FUNDS_RESERVATION_CANCELLED` | `funds.reservation.cancelled` | `{ "reservation_id", "application_id" }` |

---

### 🟥 События (Kafka — входящие)

| Событие | Тема | Обработчик |
|--------|------|-----------|
| `funds.reservation.requested` | `funds.reservation.requested` | `ReserveFundsHandler` |
| `saga.compensate.funds` | `saga.compensate.funds` | `CancelReservationHandler` |

---

## ✅ 5. `NotificationService`
**Назначение**: Отправка уведомлений клиенту

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание |
|------|------|--------|
| `POST` | `/api/notifications` | Отправка уведомления (если нужен синхронный вызов) | |

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема | Тело |
|--------|------|------|
| `NOTIFICATION_SENT` | `notification.sent` | `{ "application_id", "channel": "EMAIL", "recipient": "..." }` |
| `NOTIFICATION_FAILED` | `notification.failed` | `{ "application_id", "error": "..." }` |

---

### 🟥 События (Kafka — входящие)

| Событие | Тема | Обработчик |
|--------|------|-----------|
| `notification.requested` | `notification.requested` | `SendNotificationHandler` |

---

## ✅ 6. `LoanManagementService`
**Назначение**: Управление действующим кредитом

### 🟦 Эндпоинты (REST)

| Метод | Путь | Описание |
|------|------|--------|
| `POST` | `/api/loans` | Создание кредита (если не через Kafka) | |
| `GET`  | `/api/loans/{loan_id}` | Получение информации о кредите | |
| `GET`  | `/api/loans/{loan_id}/schedule` | Получение графика платежей | |
| `POST` | `/api/loans/{loan_id}/early-repayment` | Досрочное погашение | `{ "amount" }` |
| `POST` | `/api/loans/{loan_id}/restructure` | Пересчёт условий (каникулы) | `{ "type": "HOLIDAYS", "months": 3 }` |

---

### 🟨 События (Kafka — исходящие)

| Событие | Тема | Тело |
|--------|------|------|
| `LOAN_ACTIVATED` | `loan.activated` | `{ "loan_id", "application_id", "start_date" }` |
| `LOAN_RESTRUCTURED` | `loan.restructured` | `{ "loan_id", "new_term", "new_payment" }` |
| `LOAN_CLOSED` | `loan.closed` | `{ "loan_id", "paid_at" }` |

---

### 🟥 События (Kafka — входящие)

| Событие | Тема | Обработчик |
|--------|------|-----------|
| `loan.created` | `loan.created` | `CreateLoanOnEvent` |

---

## 📌 **Итог: Как делить работу между разработчиками**

| Команда | Задачи |
|--------|-------|
| **Команда 1: CreditApplicationService** | Реализовать REST API, SAGA Orchestrator, Outbox, обработку входящих событий |
| **Команда 2: CustomerVerification + Scoring** | Реализовать обработку заявок, интеграцию с внешними системами, публикацию результатов |
| **Команда 3: PaymentService** | Реализовать резервацию средств, события, компенсацию |
| **Команда 4: NotificationService** | Отправка email, события |
| **Команда 5: LoanManagementService** | CRUD кредита, график, реструктуризация |

---

## 📎 **Приложения**

### 📄 Приложение A: Список всех Kafka-топиков
| Топик | Направление | Описание |
|------|------------|---------|
| `credit.applications.created` | out | Новая заявка |
| `customer.verification.requested` | out | Запрос проверки клиента |
| `customer.verification.completed` | in | Результат проверки |
| `scoring.requested` | out | Запрос на скоринг |
| `scoring.approved` | in | Успешный скоринг |
| `funds.reservation.requested` | out | Запрос резервации |
| `funds.reservation.completed` | in | Средства зарезервированы |
| `notification.requested` | out | Запрос на уведомление |
| `loan.created` | out | Создание кредита |
| `saga.compensate.*` | - | Компенсирующие события (опционально) |

---

Теперь каждый разработчик знает:
- Какие **эндпоинты** нужно реализовать,
- Какие **события** слушать и публиковать,
- Какие **действия** выполнять.

Готов передать в разработку ✅  
Если нужно — могу подготовить **Jira-эпики и задачи** или **Swagger-спецификации**.