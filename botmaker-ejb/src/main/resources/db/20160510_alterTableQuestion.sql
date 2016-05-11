ALTER TABLE botmaker.question ADD COLUMN valida_id BIGINT DEFAULT NULL;
UPDATE botmaker.question SET valida_id = validator_id;
ALTER TABLE botmaker.question DROP COLUMN validator_id;
ALTER TABLE botmaker.question ADD COLUMN validator_id BIGINT DEFAULT NULL, ADD CONSTRAINT fk_question_validator FOREIGN KEY (validator_id) REFERENCES botmaker.validator(validator_id) ;
UPDATE botmaker.question SET validator_id = valida_id;
ALTER TABLE botmaker.question DROP COLUMN valida_id;