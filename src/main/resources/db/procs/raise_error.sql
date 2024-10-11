--
-- helper function to force a specific exception (useful in testing server side
-- errors).  Refs: https://www.postgresql.org/docs/current/errcodes-appendix.html
--
-- usage:
--
CREATE FUNCTION raise_error(errcode text default 'raise_exception',
                            message text default 'Generic exception') RETURNS boolean
    LANGUAGE plpgsql AS
$$
BEGIN
    RAISE EXCEPTION USING
        errcode = coalesce(errcode, 'raise_exception'),
        message = coalesce(message, 'Generic exception');
    RETURN true;
END;
$$;