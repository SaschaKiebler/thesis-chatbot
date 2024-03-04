-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile

CREATE FUNCTION delete_embeddings_with_file() RETURNS trigger
    LANGUAGE plpgsql
AS
'BEGIN
    DELETE FROM embeddings
    WHERE metadata ->> ''fileKey'' = concat(OLD.id, '''');
    RETURN OLD;
END;';

CREATE TRIGGER trigger_delete_embeddings_with_file
    BEFORE DELETE
    ON uploadedfile
    FOR EACH ROW
    EXECUTE PROCEDURE delete_embeddings_with_file();

