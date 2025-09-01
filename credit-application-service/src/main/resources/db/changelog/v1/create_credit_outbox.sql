CREATE TABLE outbox_events
(
    id              UUID PRIMARY KEY,
    aggregate_type  VARCHAR(50),        -- e.g., "CREDIT_APPLICATION"
    aggregate_id    UUID,               -- ссылка на credit_applications.id
    event_type      VARCHAR(100),       -- e.g., "CREDIT_APPLICATION_CREATED"
    payload         JSON,
    topic           VARCHAR(100),       -- Kafka topic
    processed_at    TIMESTAMPTZ,        -- NULL = не отправлено
    created_at      TIMESTAMPTZ
)