# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\2.1.1\sdk/tools/proguard/proguard-android.txt
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
#混淆开启时需要下面这句话
#-ignorewarnings -keep class * { public private *; }
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}