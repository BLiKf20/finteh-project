
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
