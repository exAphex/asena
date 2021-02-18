

INSERT INTO ${flyway:defaultSchema}.scripts (id, content, name) SELECT NEXTVAL('${flyway:defaultSchema}.scripts_sequence'), '/*
 * Author: Aydin Tekin
 * Description: Adds the azure domain as mail suffix
 * Created on: 2021-02-15
*/

function getMailSuffixFromAzureDomain(param) {
	return param + "@" + getSystemProperty("azure.domain.name");
}', 'getMailSuffixFromAzureDomain' where (not 'getMailSuffixFromAzureDomain' in (select name from ${flyway:defaultSchema}.scripts));

INSERT INTO ${flyway:defaultSchema}.scripts (id, content, name) SELECT NEXTVAL('${flyway:defaultSchema}.scripts_sequence'), '/*
 * Author: Aydin Tekin 
 * Description: Parses azure password to a adequate format
 * Created on: 2021-02-13
*/

function getAzurePassword(param) {
	return {"password": param};
}', 'getAzurePassword' where (not 'getAzurePassword' in (select name from ${flyway:defaultSchema}.scripts));
