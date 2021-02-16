

INSERT INTO asena.scripts (id, content, name) SELECT NEXTVAL('asena.scripts_sequence'), '/*
 * Author: Aydin Tekin
 * Description: Adds the azure domain as mail suffix
 * Created on: 2021-02-15
*/

function getMailSuffixFromAzureDomain(param) {
	return param + "@" + getSystemProperty("azure.domain.name");
}', 'getMailSuffixFromAzureDomain' where (not 'getMailSuffixFromAzureDomain' in (select name from asena.scripts));

INSERT INTO asena.scripts (id, content, name) SELECT NEXTVAL('asena.scripts_sequence'), '/*
 * Author: Aydin Tekin 
 * Description: Parses azure password to a adequate format
 * Created on: 2021-02-13
*/

function getAzurePassword(param) {
	return {"password": param};
}', 'getAzurePassword' where (not 'getAzurePassword' in (select name from asena.scripts));
