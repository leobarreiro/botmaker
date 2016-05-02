INSERT INTO company (company_id, active, name, website) VALUES (NEXTVAL('botmaker.company_seq'), true, 'Javaleo.org', 'http://javaleo.org');

INSERT INTO "user" (user_id, active, admin, email, name, passphrase, username, company_id) VALUES (NEXTVAL('botmaker.user_seq'), true, true, 'javaleo@javaleo.org', 'Admin User BotRise', '23d42f5f3f66498b2c8ff4c20b8c5ac826e47146', 'admin', 1);

INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), 'A natural integer', 'Natural Number', '^[0-9]{1,}$', 'REGEXP', NULL);
INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), 'Lista das principais churrascarias de Porto Alegre', 'Churrascarias Poa', 'Ravenna, Freio de Ouro, Fogo de Chão, Na Brasa, Bom Mangiare, Laçador, CTG 35, Galpão Crioulo', 'SET', NULL);
INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), 'Cidades disponiveis no serviço de clima', 'Cidades Clima', 'Porto Alegre, Florianopolis, Curitiba, Sao Paulo, Rio de Janeiro, Montevideo, Buenos Aires, Santiago de Chile', 'SET', NULL);
INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), 'A decimal number, with decimal places.', 'Decimal number', '^[0-9]{0,}(.|,){1}[0-9]{0,}$', 'REGEXP', NULL);
INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), 'A date, composed by year (yyyy), month (MM) and day (dd) (in any order)', 'Date format', '^([0-9]{4}\W{1}[0-9]{2}\W{1}[0-9]{2}|[0-9]{2}\W{1}[0-9]{2}\W{1}[0-9]{4})$', 'REGEXP', NULL);
INSERT INTO validator (validator_id, description, name, script_code, script_type, company_id) VALUES (NEXTVAL('botmaker.validator_seq'), '', 'Any String', '[a-zA-Z0-9]{1,}\W*[a-zA-Z0-9]{1,}\W*', 'REGEXP', NULL);


INSERT INTO bot (bot_id, active, bot_type, closed_message, endofdialogmessage, name, token, unknown_message, company_id, list_commands) VALUES (NEXTVAL('botmaker.bot_seq'), true, 'TELEGRAM', 'Bot em manutencao', 'Ok! Ficamos felizes em ajudar voce. Volte sempre que quiser.', '@DatamedBot', '117715702:AAGriUujd6u9EirJ70tDXLGKWzXmQy4TQg8', 'Comando nao reconhecido. Utilize a lista de comandos validos deste bot.', NULL, true);


INSERT INTO command (command_id, active, key, short_description, welcome_message, bot_id, post_process, post_process_script, script_type) VALUES (NEXTVAL('botmaker.command_seq'), NULL, 'clima', 'Informacoes sobre clima', NULL, 1, false, '', NULL);
INSERT INTO command (command_id, active, key, short_description, welcome_message, bot_id, post_process, post_process_script, script_type) VALUES (NEXTVAL('botmaker.command_seq'), NULL, 'imc', 'Calcule o seu IMC com este pratico comando.', NULL, 1, true, 'def imc = Double.valueOf(peso) / (Double.valueOf(altura) * Double.valueOf(altura));
def resultado = '''';

if (imc < 18.5) {
	resultado = ''baixo peso'';
} else if (imc > 18.5 && imc <= 25) {
	resultado = ''saudavel'';
} else if (imc > 25 && imc <= 30) {
	resultado = ''sobre-peso'';
} else if (imc > 30 && imc <= 35) {
	resultado = ''Obesidade I'';
} else if (imc > 35 && imc <= 40) {
	resultado = ''Obesidade II'';
} else if (imc > 40) {
	resultado = ''Obesidade III'';
}

return ''Seu IMC eh de ''+imc.round(2)+''\n''+resultado;', 'GROOVY');



INSERT INTO question (question_id, error_message, instruction, answer_options, order_number, success_message, var_name, command_id, validator_id, post_process_script, process_answer, script_type) VALUES (NEXTVAL('botmaker.question_seq'), 'Informe o seu peso em kilogramas.', 'Qual o seu peso?', NULL, 2, 'Certo.', 'peso', 1, 1, '', false, NULL);
INSERT INTO question (question_id, error_message, instruction, answer_options, order_number, success_message, var_name, command_id, validator_id, post_process_script, process_answer, script_type) VALUES (NEXTVAL('botmaker.question_seq'), 'Voce precisa informar sua altura em metro(s) e centímetro(s).', 'Qual a sua altura?', NULL, 1, 'Ok', 'altura', 1, 1, '', false, NULL);
INSERT INTO question (question_id, error_message, instruction, answer_options, order_number, success_message, var_name, command_id, validator_id, post_process_script, process_answer, script_type) VALUES (NEXTVAL('botmaker.question_seq'), 'Voce precisa escolher uma cidade das opcoes disponiveis.', 'Selecione a cidade que deseja informacoes sobre o clima.', NULL, 1, '', 'cidade', 2, 6, 'import groovy.json.JsonSlurper

def connection = new URL( "https://query.yahooapis.com/v1/public/yql?q=" +
    	URLEncoder.encode(
            	"select item " +
                    	"from weather.forecast where woeid in " +
                    	"(select woeid from geo.places(1) " +
                    	"where text=''$cidade'')",
            	''UTF-8'' ) )
    	.openConnection() as HttpURLConnection

connection.setRequestProperty( ''User-Agent'', ''groovy-2.4.4'' )
connection.setRequestProperty( ''Accept'', ''application/json'' )

def response = ''Clima de ''+cidade+''\n'';

if ( connection.responseCode == 200 ) {
	def json = connection.inputStream.withCloseable { inStream ->
    	    new JsonSlurper().parse( inStream as InputStream )
	}
	def item = json.query.results.channel.item;
	item.forecast.each { f ->
    	//response = response + "${f.date}: L ${f.low}, H ${f.high}, ${f.text}\n";
    	response = response + "${f.date}: ${f.text}\n";
	}
}

return response', true, 'GROOVY');




