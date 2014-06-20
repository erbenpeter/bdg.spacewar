<?php


/**
 * PAGE_HOST: The host of the page, ending with a slash.
 * PAGE_BASE_URI: The base uri of your page.
 * For example: if your project is located on
 * 		http://example.com/myproject
 * PAGE_HTTPS=false
 * PAGE_HOST="http://example.com"
 * PAGE_BASE_URI="/myproject"
 * PAGE_PORT=80
 */
define("PAGE_HTTPS",false);
define("PAGE_HOST","prog.berzsenyi.hu");
define("PAGE_PORT",81);
define("PAGE_BASE_URI","/bdgspacewar");

include "gen.php";



/**
 * Configuration for: Database Connection
 *
 * For more information about constants please @see http://php.net/manual/en/function.define.php
 * If you want to know why we use "define" instead of "const" @see http://stackoverflow.com/q/2447791/1114320
 *
 * DB_HOST: database host, usually it's "127.0.0.1" or "localhost", some servers also need port info
 * DB_NAME: name of the database. please note: database and database table are not the same thing
 * DB_TABLE: name of the database table in which the user data is stored
 * DB_USER: user for your database. the user needs to have rights for SELECT, UPDATE, DELETE and INSERT.
 * DB_PASS: the password of the above user
 */
define("DB_HOST", "127.0.0.1");
define("DB_NAME", "bdgspacewar");
define("DB_TABLE", "users");
define("DB_USER", "bdgspacewar");
define("DB_PASS", "*******");



?>