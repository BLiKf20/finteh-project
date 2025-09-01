CREATE TABLE credit_applications
(
    id              UUID PRIMARY KEY,
    client_id       UUID            NOT NULL,
    amount          DECIMAL         NOT NULL,
    term_months     INT             NOT NULL,
    status          VARCHAR(20)     NOT NULL,
    created_at      TIMESTAMPTZ     NOT NULL,
    updated_at      TIMESTAMPTZ     NOT NULL,

    CONSTRAINT status_check CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED', 'CREATED', 'VERIFIED',
                                              'SCORING', 'FUNDS_RESERVED', 'NOTIFIED', 'TIMEOUT', 'REJECTED'))
)