# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Abreu/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-keep com.rindus.data.network.entities.** { *; }

# Avoid Logging messages
-assumenosideeffects class android.util.Log {
     public static boolean isLoggable(java.lang.String, int);
     public static int v(...);
     public static int i(...);
     public static int w(...);
     public static int d(...);
     public static int e(...);
 }

 # lifecycle
-keepclassmembers class androidx.lifecycle.** { ; }
-keep class androidx.lifecycle.* { ; }
-dontwarn androidx.lifecycle.*

 # Retrofit 2.X
 ## https://square.github.io/retrofit/ ##
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepclasseswithmembers class * {
     @retrofit2.http.* <methods>;
 }

# OKHTTP https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

