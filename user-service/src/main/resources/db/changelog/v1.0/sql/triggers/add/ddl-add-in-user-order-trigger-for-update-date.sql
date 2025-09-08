CREATE TRIGGER update_date
    BEFORE UPDATE
    ON user_order
    FOR EACH ROW
    EXECUTE procedure moddatetime(update_date);