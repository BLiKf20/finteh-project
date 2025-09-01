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

