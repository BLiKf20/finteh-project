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