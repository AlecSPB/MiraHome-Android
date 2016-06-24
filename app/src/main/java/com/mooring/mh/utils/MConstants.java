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
    public static final String TOKEN = SERVICE_URL + "token";

    /**
     * 获取短信验证码
     * <p/>
     * mobile_phone: '手机号码'
     */
    public static final String SMS_CODE = SERVICE_URL + "sms_code";

    /**
     * 获取邮件验证码
     * <p/>
     * email: 'mail@mirahome.me'
     */
    public static final String VERIFY_CODE = SERVICE_URL + "verify_code";

    /**
     * 用手机号码注册用户
     * <p/>
     * password: '',
     * mobile_phone: '',
     * sms_code: '短信验证码',
     * ios_device_id: 'ios设备token'
     */
    public static final String MOBILE_PHONE_USER = SERVICE_URL + "mobile_phone_user";

    /**
     * 检查手机号码是否已经被注册
     * <p/>
     * mobile_phone: '18600001111'
     */
    public static final String IS_MOBILE_PHONE_USED = SERVICE_URL + "is_mobile_phone_used";

    /**
     * 检查email是否已经被注册
     * <p/>
     * email: ''
     */
    public static final String IS_EMAIL_USED = SERVICE_URL + "is_email_used";

    /**
     * 使用Email注册用户
     * <p/>
     * email: '',
     * password: '',
     * verify_code: '', //验证码
     * ios_device_id: 'ios设备token'
     */
    public static final String EMAIL_USER = SERVICE_URL + "email_user";

    /**
     * 发送重置密码邮件
     * <p/>
     * email: ''
     */
    public static final String SEND_RESET_PASSWORD_EMAIL = SERVICE_URL + "send_reset_password_email";

    /**
     * 使用邮件验证码重置密码
     * <p/>
     * verify_code: '123456',
     * new_password: '12345678',
     * email: 'suyuan@mirahome.me'
     */
    public static final String RESET_PASSWORD_BY_EMAIL = SERVICE_URL + "reset_password_by_email";

    /**
     * 发送重置密码手机验证码
     * <p/>
     * mobile_phone: '18600001111'
     */
    public static final String SEND_RESET_PASSWORD_SMS_CODE = SERVICE_URL + "send_reset_password_sms_code";

    /**
     * 使用手机验证码重置密码
     * <p/>
     * verify_code: '123456',
     * new_password: '12345678',
     * mobile_phone: '18616120000'
     */
    public static final String RESET_PASSWORD_BY_PHONE = SERVICE_URL + "reset_password_by_phone";

    /**
     * 手机号登录
     * <p/>
     * mobile_phone: '手机号码',
     * password: ''
     */
    public static final String LOGIN_BY_MOBILE_PHONE = SERVICE_URL + "login_by_mobile_phone";

    /**
     * 使用email登录
     * <p/>
     * email: '',
     * password: ''
     */
    public static final String LOGIN_BY_EMAIL = SERVICE_URL + "login_by_email";

    /**
     * 使用weixin登录
     * <p/>
     * access_token: 'miratoken',
     * open_id: '
     */
    public static final String LOGIN_WEIXIN = SERVICE_URL + "login_weixin";

    /**
     * 使用weibo登录
     * <p/>
     * access_token: 'miratoken',
     * weibo_id: ''
     */
    public static final String LOGIN_WEIBO = SERVICE_URL + "login_weibo";

    /**
     * 使用facebook登录
     * <p/>
     * access_token: 'miratoken',
     * facebook_id: ''
     */
    public static final String LOGIN_FACEBOOK = SERVICE_URL + "login_facebook";

    /**
     * 使用qq登录
     * <p/>
     * access_token: 'miratoken',
     * qq_id: ''
     */
    public static final String LOGIN_QQ = SERVICE_URL + "login_qq";

    /**
     * 退出登陆
     * <p/>
     * apikey: ''
     */
    public static final String LOGOUT = SERVICE_URL + "logout";

    /**
     * 修改密码
     * <p/>
     * old_password: '',
     * new_password: '',
     * apikey: '' //登录成功以后从服务器返回
     */
    public static final String CHANGE_PASSWORD = SERVICE_URL + "change_password";

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
    public static final String MEMBER = SERVICE_URL + "member";

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
    public static final String MANAGE_MEMBER = SERVICE_URL + "member/:member_id";


    /**
     * 获取当前user下所有成员
     */
    public static final String GET_MEMBER_BY_USER_ID = SERVICE_URL + "get_member_by_user_id";
    /**
     * 设置闹钟
     * <p/>
     * week_day: 1, //星期几 1-7
     * hour: 10,
     * minute: 10,
     * member_id: 1
     */
    public static final String SET_ALARM = SERVICE_URL + "set_alarm";

    /**
     * 上传文件
     * <p/>
     * Filedata: ''
     */
    public static final String UPLOAD = SERVICE_URL + "upload";

    /*设备相关*/
    /**
     * 绑定用户到设备
     * <p/>
     * device_id: 1,
     */
    public static final String BIND_DEVICE = SERVICE_URL + "bind_device";

    /**
     * 获取设备状态
     * device_id: 1,
     * user_id: 1
     */
    public static final String DEVICE_INFO = SERVICE_URL + "device_info/:device_id";

    /**
     * 设置成员到设备
     * <p/>
     * device_side: 1, //1或2
     * member_id: 1
     */
    public static final String MEMBER_TO_DEVICE = SERVICE_URL + "member_to_device";

    /**
     * 设备上的成员换边
     */
    public static final String CHANGE_SIDE = SERVICE_URL + "change_side";

    /**
     * 获取日报告
     * <p/>
     * member_id: 1
     * year_month: '2010-08'  //月份必须为两位数字
     */
    public static final String DAILY_REPORT = SERVICE_URL + "daily_report";

    /**
     * 获取月报告
     * <p/>
     * member_id: 1
     * year_month: '2010-08'  //月份必须为两位数字
     */
    public static final String MONTHLY_REPORT = SERVICE_URL + "monthly_report";

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
    public final static String SP_KEY_USERNAME = "username";//用户名
    public final static String SP_KEY_PASSWORD = "password";//密码
    public final static String SP_KEY_TOKEN = "token";//token
    public final static String SP_KEY_UID = "uid";//用户ID
    public final static String SP_KEY_MEMBER_ID = "member_id";//成员ID
    public final static String SP_KEY_FIRST_START = "appFirstStart";//初次启动
    public final static String HAS_LOCAL_USER = "has_local_user";//本地是否由用户
    public final static String DEVICE_ID = "deviceId";//设备ID
    public final static String DEVICE_MODEL = "deviceModel";//设备Model
    public final static String DEVICE_TYPE = "deviceType";//设备type
    public final static String DEVICE_NAME = "deviceName";//设备name
    public final static String DEVICE_ONLINE = "deviceOnline";//设备在线
    public final static String DEVICE_LAN_ONLINE = "deviceLanOnline";//设备局域网在线
    public final static String ENTRANCE_FLAG = "entrance_flag";//进入标志
    public final static String TEMPERATURE_UNIT = "temperature_unit";//温度单位
    public final static String CURR_BLANKET_MODEL = "curr_blanket_model";//当前毯子所处模式
    public final static String LOGOUT_KICKOFF = "logout_kickoff";//用户被挤下线标志
    public final static String CURR_USER_ID = "curr_user_id";//当前用户id,用来提取出当前用户使用
    public final static String CURR_USER_LOCATION = "curr_user_location";//当前用户位置

    /**
     * 设备属性值
     */
    public final static String ATTR_ENVIR_TEMPERATURE = "101";//环境温度
    public final static String ATTR_ENVIR_HUMIDITY = "102";//环境湿度值
    public final static String ATTR_ENVIR_NOISE = "103";//环境噪声值
    public final static String ATTR_ENVIR_LIGHT = "104";//环境光照值
    public final static String ATTR_LEFT_ACTUAL_TEMP = "105";//左边实际温度
    public final static String ATTR_LEFT_TARGET_TEMP = "106";//左边目标温度
    public final static String ATTR_RIGHT_ACTUAL_TEMP = "107";//右边实际温度
    public final static String ATTR_RIGHT_TARGET_TEMP = "108";//右边目标温度
    public final static String ATTR_LEFT_HEART_RATE = "109";//左边用户心率
    public final static String ATTR_RIGHT_HEART_RATE = "110";//右边用户心率
    public final static String ATTR_LEFT_RESP_RATE = "111";//左边呼吸频率
    public final static String ATTR_RIGHT_RESP_RATE = "112";//右边呼吸频率
    public final static String ATTR_LEFT_MOVEMENT = "113";//左边体动
    public final static String ATTR_RIGHT_MOVEMENT = "114";//右边体动
    public final static String ATTR_LEFT_SOMEONE = "115";//左边有人
    public final static String ATTR_RIGHT_SOMEONE = "116";//右边有人
    public final static String ATTR_DRYING_SWITCH = "117";//烘干开关标志
    public final static String ATTR_DRYING_TIME = "118";//烘干过程时间
    public final static String ATTR_LEFT_MODE = "119";//左模式
    public final static String ATTR_RIGHT_MODE = "120";//右模式
    public final static String ATTR_BLANKETS_TOWARD = "121";//毯子朝向
    public final static String ATTR_TIME_ZONE = "122";//时区
    public final static String ATTR_IS_CONNECTED = "123";//是否连接
    public final static String ATTR_SINGLE_OR_DOUBLE = "124";//单双人
    public final static String ATTR_ALARM_LEFT = "125";//左边闹钟
    public final static String ATTR_ALARM_RIGHT = "126";//右边闹钟
    public final static String ATTR_DRYING_ON_TIME = "127";//烘干开始时间
    public final static String ATTR_LEFT_TARGET_TEMP_SWITCH = "128";//左边目标温度开关
    public final static String ATTR_RIGHT_TARGET_TEMP_SWITCH = "129";//右边目标温度开关
    public final static String ATTR_SUMMER_SEASON = "130";//夏时令

    /**
     * 用户在床上的位置
     */
    public final static int BED_OUT = 0;
    public final static int BED_LEFT = 1;
    public final static int BED_RIGHT = 2;

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
    public static final int USER_INFO_REQUEST = 0X17;

    /**
     * 添加用户finish标识
     */
    public static final int USER_INFO_RESULT = 0X18;

    /**
     * 编辑闹钟跳转标识
     */
    public static final int ALARM_EDIT_REQUEST = 0X19;

    /**
     * 编辑闹钟回调标识
     */
    public static final int ALARM_EDIT_RESULT = 0X20;

    /**
     * 闹钟重复次数跳转标识
     */
    public static final int REPEAT_ALARM_REQUEST = 0X21;

    /**
     * 闹钟重复次数回调标识
     */
    public static final int REPEAT_ALARM_RESULT = 0X22;

    /**
     * 设备连接成功
     */
    public static final int CONNECTED_SUCCESS = 0X23;

    /**
     * 跳转照相机
     */
    public static final int CAMERA_PHOTO = 0X24;

    /**
     * 跳转相册
     */
    public static final int GALLERY_PHOTO = 0X25;

    /**
     * 跳转相册
     */
    public static final int CROP_PHOTO = 0X26;

    /**
     * 位置权限
     */
    public static final int PERMISSIONS_LOCATION = 0X27;

    /**
     * 文件读写权限
     */
    public static final int PERMISSIONS_STORAGE = 0X28;

    /**
     * 双人毯,单个人
     */
    public static final int DOUBLE_BLANKET_SINGLE = 0X29;

    /**
     * 双人毯,多人
     */
    public static final int DOUBLE_BLANKET_MULTIPLE = 0X30;

    /**
     * 单人毯
     */
    public static final int SINGLE_BLANKET = 0X31;

    /**
     * 添加用户成功
     */
    public static final int ADD_USER_SUCCESS = 0X32;
    /**
     * 修改密码请求
     */
    public static final int CONFIR_PSW_REQUEST = 0X33;
    /**
     * 修改密码回调
     */
    public static final int CONFIR_PSW_RESULT = 0X34;
    /**
     * 工具类常量
     */
    public final static int DEGREES_C = 0X35;//摄氏度
    public final static int DEGREES_F = 0X36;//华氏度
    /**
     * 跳转已存在设备请求
     */
    public final static int EXISTING_REQUEST = 0X37;
    /**
     * 跳转已存在设备回调
     */
    public final static int EXISTING_RESULT = 0X37;

}
