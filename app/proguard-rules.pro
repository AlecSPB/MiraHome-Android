# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Will/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 忽略警告
-ignorewarning
# 指定代码的压缩级别
-optimizationpasses 5

# 包明不混合大小写
-dontusemixedcaseclassnames

# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 # 优化不优化输入的类文件
-dontoptimize

 # 预校验
-dontpreverify

#------------------高德地图定位混淆-----------------------
-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}

#------------------xutils3.+的混淆-----------------------
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
   public protected *;
}
-keep public interface org.xutils.** {
   public protected *;
}
-keepclassmembers class * extends org.xutils.** {
   public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
   @org.xutils.view.annotation.Event <methods>;
}

#------------------友盟统计混淆-----------------------
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.mooring.mh.R$*{
   public static final int *;
}
-keepclassmembers enum * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}

#----------------友盟推送混淆-------------------------
-dontwarn com.ut.mini.**
-dontwarn okio.**
-dontwarn com.xiaomi.**
-dontwarn com.squareup.wire.**
-dontwarn android.support.**

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
   public <fields>;
   public <methods>;
}

-keep class com.umeng.message.* {
   public <fields>;
   public <methods>;
}

-keep class org.android.agoo.impl.* {
   public <fields>;
   public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class **.R$*{
   public static final int *;
}
#如果compileSdkVersion为23，请添加以下混淆代码
-dontwarn org.apache.http.**
-dontwarn android.webkit.**
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.codec.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class android.net.compatibility.** { *; }
-keep class android.net.http.** { *; }

#---------------------智成云混淆----------------------
-keepattributes Exceptions,InnerClasses
-keep class com.machtalk.sdk.connect.MachtalkSDKConstant {*;}
-keep class com.machtalk.sdk.connect.MachtalkSDKConstant$* {*;}
-keep class com.machtalk.sdk.connect.MachtalkSDK {*;}
-keep class com.machtalk.sdk.connect.MachtalkSDKListener {*;}
-keep class com.machtalk.sdk.util.SDKErrorCode{*;}
-keep class com.machtalk.sdk.domain.** { * ; }
-keep public class com.machtalk.sdk.util.Log { * ; }

#---------------------Gson混淆----------------------
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#这句非常重要，主要是滤掉 com.bgb.scan.model包下的所有.class文件不进行混淆编译
-keep class com.bgb.scan.model.** {*;}
