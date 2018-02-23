package iitd.messfeeback.myapplication;

/**
 * Created by korku02 on 26/06/17.
 */

public class Config {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_HOSTEL = "hostel";
    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "accesstoken";
    public static final String FEEDBACK_DATE = "created";
    public static final String FEEDBACK_TYPE = "meal_type";
    public static final String FEEDBACK_RATING = "rating";
    public static final String ATTENDANCE_DATE = "created";
    public static final String ATTENDANCE_TYPE = "meal_tpye";
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";
    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String GET_MENU_URL = "http://10.194.24.178:8080/api/getmenudaily/";
    public static final String LOGIN_URL = "http://10.194.24.178:8080/login/";
    public static final String SUBMIT_URL = "http://10.194.24.178:8080/api/meal/";
    public static final String REGISTER_URL = "http://10.194.24.178:8080/register/";
    public static final String ATTENDANCE_URL = "http://10.194.24.178:8080/api/attendance/";
    public static final String UPDATE_DATA_URL = "http://10.194.24.178:8080/api/getlastdata/";
    public static final String FORGOT_PASS = "http://10.194.24.178:8080/forgotpasswordemail/";

}
