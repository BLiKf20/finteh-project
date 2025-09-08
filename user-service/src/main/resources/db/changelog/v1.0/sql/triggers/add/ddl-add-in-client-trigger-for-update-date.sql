CREATE TRIGGER update_date
    BEFORE UPDATE
    ON client
    FOR EACH ROW
    EXECUTE procedure moddatetime(update_date);