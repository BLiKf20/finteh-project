CREATE TRIGGER update_date
    BEFORE UPDATE
    ON address
    FOR EACH ROW
    EXECUTE procedure moddatetime(update_date);