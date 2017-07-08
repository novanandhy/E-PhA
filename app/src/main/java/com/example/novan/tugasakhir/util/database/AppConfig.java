package com.example.novan.tugasakhir.util.database;

/**
 * Created by Novan on 01/05/2017.
 */

public class AppConfig {
    static String DOMAIN = "websitegratis.esy.es";
    static String IP_HOME = "192.168.1.10";
    static String IP_PRIVATE = "192.168.43.166";

    // Server user login url
    public static String URL_LOGIN = "http://"+DOMAIN+"/android_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://"+DOMAIN+"/android_api/register.php";

    // Server user update url
    public static String URL_UPDATE = "http://"+DOMAIN+"/android_api/update.php";

    // Server relapse history update url
    public static String URL_STORE_RELAPSE = "http://"+DOMAIN+"/android_api/set_relapse_history.php";

    // Server medicine history update url
    public static String URL_STORE_MEDICINE = "http://"+DOMAIN+"/android_api/set_medicine_history.php";

    // Server get medicine history
    public static String URL_GET_HISTORY_MEDICINE = "http://"+DOMAIN+"/android_api/get_medicine_history.php";

    // Server get medicine history
    public static String URL_GET_HISTORY_RELAPSE = "http://"+DOMAIN+"/android_api/get_relapse_history.php";
}
