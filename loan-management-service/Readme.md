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
