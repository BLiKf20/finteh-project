
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
