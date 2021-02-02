INSERT INTO asena.scripts (id, content, name) SELECT NEXTVAL('asena.scripts_sequence'), '/*
 * Author: Aydin Tekin
 * Description: Retrieves the username from the primary SCIM mail address
 * Created on: 2021-02-02
*/

function sacGetUserNameFromMail(param) {
    for (var i = 0; i < param.length; i++) {
        var obj = param[i];
        if (obj.get("primary")) {
            return "" + obj.get("value");
        }
    }
	return null;
}', 'sacGetUserNameFromMail' where (not 'sacGetUserNameFromMail' in (select name from asena.scripts))