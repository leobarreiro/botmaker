SELECT * FROM botmaker.validator;

ALTER TABLE botmaker.validator ADD COLUMN validator_type VARCHAR(20) DEFAULT NULL;
UPDATE botmaker.validator SET validator_type = script_type;
ALTER TABLE botmaker.validator DROP COLUMN script_type;

SELECT * FROM botmaker.validator;