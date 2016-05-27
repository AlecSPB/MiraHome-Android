package com.mooring.mh.utils;

/**
 * 网络请求地址
 * <p/>
 * Created by Will on 16/3/31.
 */
public class MConstants {

    /**
     * 服务器地址
     */
    public static final String SERVICE_URL = "https://api.mirahome.me:3000/v1/";

    /**
     * 获取token
     */
    public static final String TOKEN = "token";

    /**
     * 获取短信验证码
     * <p/>
     * mobile_phone: '手机号码'
     */
    public static final String SMS_CODE = "sms_code";

    /**
     * 获取邮件验证码
     * <p/>
     * email: 'mail@mirahome.me'
     */
    public static final String VERIFY_CODE = "verify_code";

    /**
     * 用手机号码注册用户
     * <p/>
     * password: '',
     * mobile_phone: '',
     * sms_code: '短信验证码',
     * ios_device_id: 'ios设备token'
     */
    public static final String MOBILE_PHONE_USER = "mobile_phone_user";

    /**
     * 检查手机号码是否已经被注册
     * <p/>
     * mobile_phone: '18600001111'
     */
    public static final String IS_MOBILE_PHONE_USED = "is_mobile_phone_used";

    /**
     * 检查email是否已经被注册
     * <p/>
     * email: ''
     */
    public static final String IS_EMAIL_USED = "is_email_used";

    /**
     * 使用Email注册用户
     * <p/>
     * email: '',
     * password: '',
     * verify_code: '', //验证码
     * ios_device_id: 'ios设备token'
     */
    public static final String EMAIL_USER = "email_user";

    /**
     * 发送重置密码邮件
     * <p/>
     * email: ''
     */
    public static final String SEND_RESET_PASSWORD_EMAIL = "send_reset_password_email";

    /**
     * 使用邮件验证码重置密码
     * <p/>
     * verify_code: '123456',
     * new_password: '12345678',
     * email: 'suyuan@mirahome.me'
     */
    public static final String RESET_PASSWORD_BY_EMAIL = "reset_password_by_email";

    /**
     * 发送重置密码手机验证码
     * <p/>
     * mobile_phone: '18600001111'
     */
    public static final String SEND_RESET_PASSWORD_SMS_CODE = "send_reset_password_sms_code";

    /**
     * 使用手机验证码重置密码
     * <p/>
     * verify_code: '123456',
     * new_password: '12345678',
     * mobile_phone: '18616120000'
     */
    public static final String RESET_PASSWORD_BY_PHONE = "reset_password_by_phone";

    /**
     * 手机号登录
     * <p/>
     * mobile_phone: '手机号码',
     * password: ''
     */
    public static final String LOGIN_BY_MOBILE_PHONE = "login_by_mobile_phone";

    /**
     * 使用email登录
     * <p/>
     * email: '',
     * password: ''
     */
    public static final String LOGIN_BY_EMAIL = "login_by_email";

    /**
     * 使用weixin登录
     * <p/>
     * access_token: 'miratoken',
     * open_id: '
     */
    public static final String LOGIN_WEIXIN = "login_weixin";

    /**
     * 使用weibo登录
     * <p/>
     * access_token: 'miratoken',
     * weibo_id: ''
     */
    public static final String LOGIN_WEIBO = "login_weibo";

    /**
     * 使用facebook登录
     * <p/>
     * access_token: 'miratoken',
     * facebook_id: ''
     */
    public static final String LOGIN_FACEBOOK = "login_facebook";

    /**
     * 使用qq登录
     * <p/>
     * access_token: 'miratoken',
     * qq_id: ''
     */
    public static final String LOGIN_QQ = "login_qq";

    /**
     * 退出登陆
     * <p/>
     * apikey: ''
     */
    public static final String LOGOUT = "logout";

    /**
     * 修改密码
     * <p/>
     * old_password: '',
     * new_password: '',
     * apikey: '' //登录成功以后从服务器返回
     */
    public static final String CHANGE_PASSWORD = "change_password";

    /**
     * 添加成员
     * <p/>
     * member_name: '',
     * member_image: '',
     * gender: 1, //1 男， 2 女
     * birth_date: '2010-10-1',
     * height: 200, //cm
     * weight: 70 //kg
     */
    public static final String MEMBER = "member";

    /**
     * 更新或删除成员
     * 删除时没有参数
     * <p/>
     * member_name: '',
     * member_image: '成员头像url',
     * gender: '1',//1 男， 2 女
     * birth_date: '',
     * height: 10,
     * weight: 10
     */
    public static final String MANAGE_MEMBER = "member/:member_id";


    /**
     * 获取当前user下所有成员
     */
    public static final String GET_MEMBER_BY_USER_ID = "get_member_by_user_id";
    /**
     * 设置闹钟
     * <p/>
     * week_day: 1, //星期几 1-7
     * hour: 10,
     * minute: 10,
     * member_id: 1
     */
    public static final String SET_ALARM = "set_alarm";

    /**
     * 上传文件
     * <p/>
     * Filedata: ''
     */
    public static final String UPLOAD = "upload";

    /*设备相关*/
    /**
     * 绑定用户到设备
     * <p/>
     * device_id: 1,
     */
    public static final String BIND_DEVICE = "bind_device";

    /**
     * 获取设备状态
     * device_id: 1,
     * user_id: 1
     */
    public static final String DEVICE_INFO = "device_info/:device_id";

    /**
     * 设置成员到设备
     * <p/>
     * device_side: 1, //1或2
     * member_id: 1
     */
    public static final String MEMBER_TO_DEVICE = "member_to_device";

    /**
     * 设备上的成员换边
     */
    public static final String CHANGE_SIDE = "change_side";

    /**
     * 获取日报告
     * <p/>
     * member_id: 1
     * year_month: '2010-08'  //月份必须为两位数字
     */
    public static final String DAILY_REPORT = "daily_report";

    /**
     * 获取月报告
     * <p/>
     * member_id: 1
     * year_month: '2010-08'  //月份必须为两位数字
     */
    public static final String MONTHLY_REPORT = "monthly_report";

    /**
     * open weather map 服务器地址
     */
    public static final String WEATHER_SERVER = "http://api.openweathermap.org/data/2.5/weather";

    /**
     * open weather map 的appId
     */
    public static final String OPEN_WEATHER_ID = "a6c9476969cc87a5e3ba40bd69d25e3b";


    /**
     * sharedPreferences文件信息
     */
    public final static String SP_KEY_USERNAME = "username";
    public final static String SP_KEY_CURRUSERS = "currUsers";
    public final static String SP_KEY_PASSWORD = "password";
    public final static String SP_KEY_TOKEN = "token";
    public final static String SP_KEY_FIRST_START = "appFirstStart";
    public final static String DEVICE_ID = "deviceId";
    public final static String DRYING_START_TIME = "drying_start_time";
    public final static String DRYING_TIMES = "drying_times";
    public final static String DRYING_OPEN = "drying_open";
    public final static String LEFT_TARGET_TEMP = "left_target_temp";
    public final static String RIGHT_TARGET_TEMP = "right_target_temp";
    public final static String FIRST_ADD_DEVICE = "first_add_device";
    public final static String ENTRANCE_FLAG = "entrance_flag";
    public final static String TEMPERATURE_UNIT = "temperature_unit";

    /**
     * 设备属性值
     */
    public final static String ATTR_ENVIR_TEMPERATURE = "101";//环境温度
    public final static String ATTR_ENVIR_HUMIDITY = "102";
    public final static String ATTR_ENVIR_NOISE = "103";
    public final static String ATTR_ENVIR_LIGHT = "104";
    public final static String ATTR_LEFT_ACTUAL_TEMP = "105";
    public final static String ATTR_LEFT_TARGET_TEMP = "106";
    public final static String ATTR_RIGHT_ACTUAL_TEMP = "107";
    public final static String ATTR_RIGHT_TARGET_TEMP = "108";
    public final static String ATTR_LEFT_HEART_RATE = "109";
    public final static String ATTR_RIGHT_HEART_RATE = "110";
    public final static String ATTR_LEFT_RESP_RATE = "111";
    public final static String ATTR_RIGHT_RESP_RATE = "112";
    public final static String ATTR_LEFT_MOVEMENT = "113";
    public final static String ATTR_RIGHT_MOVEMENT = "114";
    public final static String ATTR_LEFT_SOMEONE = "115";
    public final static String ATTR_RIGHT_SOMEONE = "116";
    public final static String ATTR_DRYING_SWITCH = "117";
    public final static String ATTR_DRYING_TIME = "118";
    public final static String ATTR_LEFT_MODE = "119";
    public final static String ATTR_RIGHT_MODE = "120";
    public final static String ATTR_BLANKETS_TOWARD = "121";
    public final static String ATTR_TIME_ZONE = "122";
    public final static String ATTR_IS_CONNECTED = "123";
    public final static String ATTR_SINGLE_OR_DOUBLE = "124";
    public final static String ATTR_ALARM_LEFT = "125";
    public final static String ATTR_Alarm_RIGHT = "126";
    public final static String ATTR_DRYING_ON_TIME = "127";
    public final static String ATTR_LEFT_TARGET_TEMP_SWITCH = "128";
    public final static String ATTR_RIGHT_TARGET_TEMP_SWITCH = "129";
    public final static String ATTR_SUMMER_SEASON = "130";

    /**
     * 注册成功
     */
    public static final int SIGN_UP_SUCCESS = 0X11;

    /**
     * 修改密码成功
     */
    public static final int CONFIRM_SUCCESS = 0X12;

    /**
     * 左边用户标识
     */
    public static final int LEFT_USER = 0X13;

    /**
     * 右边用户标识
     */
    public static final int RIGHT_USER = 0X14;

    /**
     * 跳转添加用户
     */
    public static final int ADD_USER_REQUEST = 0X15;

    /**
     * 添加用户finish标识
     */
    public static final int ADD_USER_RESULT = 0X16;
    /**
     * 跳转添加用户
     */
    public static final int USER_INFO_REQUEST = 0X15;

    /**
     * 添加用户finish标识
     */
    public static final int USER_INFO_RESULT = 0X16;

    /**
     * 编辑闹钟跳转标识
     */
    public static final int ALARM_EDIT_REQUEST = 0X17;

    /**
     * 编辑闹钟回调标识
     */
    public static final int ALARM_EDIT_RESULT = 0X18;

    /**
     * 闹钟重复次数跳转标识
     */
    public static final int REPEAT_ALARM_REQUEST = 0X19;

    /**
     * 闹钟重复次数回调标识
     */
    public static final int REPEAT_ALARM_RESULT = 0X20;

    /**
     * 设备连接成功
     */
    public static final int CONNECTED_SUCCESS = 0X21;

    /**
     * 跳转照相机
     */
    public static final int CAMERA_PHOTO = 0X22;

    /**
     * 跳转相册
     */
    public static final int GALLERY_PHOTO = 0X23;

    /**
     * 跳转相册
     */
    public static final int CROP_PHOTO = 0X24;

    /**
     * 位置权限
     */
    public static final int PERMISSIONS_LOCATION = 0X25;

    /**
     * 文件读写权限
     */
    public static final int PERMISSIONS_STORAGE = 0X26;

    /**
     * 工具类常量
     */
    public final static int DEGREES_C = 0X111;//摄氏度
    public final static int DEGREES_F = 0X112;//华氏度

}
