## Add project specific ProGuard rules here.
## You can control the set of applied configuration files using the
## proguardFiles setting in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## Keep line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
#
## Keep generic signature for Kotlin
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
#
## ============================================
## Protocol Buffer Rules
## ============================================
## Keep all Protocol Buffer generated classes
-keep class com.qtglobal.practicaltest.proto.** { *; }
-keep class com.google.protobuf.** { *; }
#
## ============================================
## Room Database Rules
## ============================================
## Keep Room entities and DAOs
-keep class com.qtglobal.practicaltest.data.local.database.EmailEntity { *; }
-keep class com.qtglobal.practicaltest.data.local.database.EmailDao { *; }
-keep class com.qtglobal.practicaltest.data.local.database.EmailDatabase { *; }
#
## Room default rules
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**
#
## ============================================
## SQLCipher Rules
## ============================================
## Keep SQLCipher classes
-keep class net.zetetic.database.** { *; }
-dontwarn net.zetetic.database.**
#
## ============================================
## Hilt/Dagger Rules
## ============================================
## Hilt generates code that needs to be kept
-keep class com.qtglobal.practicaltest.di.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
#
## Keep Hilt entry points
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @javax.inject.Inject class * { *; }
#
## ============================================
## Domain Models Rules
## ============================================
## Keep data classes used for serialization/deserialization
-keep class com.qtglobal.practicaltest.domain.model.Email { *; }
-keep class com.qtglobal.practicaltest.domain.model.Email$* { *; }
#
## Keep data class constructors and properties
-keepclassmembers class com.qtglobal.practicaltest.domain.model.Email {
    <init>(...);
}
#
## ============================================
## Repository and Services Rules
## ============================================
## Keep repository interfaces and implementations
-keep interface com.qtglobal.practicaltest.data.repository.EmailRepository { *; }
-keep class com.qtglobal.practicaltest.data.repository.EmailRepositoryImpl { *; }
#
## Keep service interfaces and implementations
-keep interface com.qtglobal.practicaltest.data.services.** { *; }
-keep class com.qtglobal.practicaltest.data.services.** { *; }
#
## ============================================
## ViewModel Rules
## ============================================
## Keep ViewModels
-keep class com.qtglobal.practicaltest.ui.viewmodel.** { *; }
#
## ============================================
## Kotlin Coroutines Rules
## ============================================
## Keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
#
#
## ============================================
## Android Keystore and Security Rules
## ============================================
## Keep security crypto classes
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**
#
## Keep KeystoreManager
-keep class com.qtglobal.practicaltest.util.KeystoreManager { *; }
#
## ============================================
## Utility Classes Rules
## ============================================
## Keep utility classes that might be used via reflection
-keep class com.qtglobal.practicaltest.util.** { *; }
#
## ============================================
## Result Class Rules
## ============================================
## Keep Result sealed class and its implementations
-keep class com.qtglobal.practicaltest.util.Result { *; }
-keep class com.qtglobal.practicaltest.util.Result$* { *; }
#
## ============================================
## General Android Rules
## ============================================
## Keep Application class
-keep class com.qtglobal.practicaltest.PracticeTestApplication { *; }
#
## Keep Activities
-keep class com.qtglobal.practicaltest.MainActivity { *; }
#
## Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
#
## Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#
## ============================================
## Warnings Suppression
## ============================================
## Suppress warnings for libraries that don't need ProGuard rules
-dontwarn kotlin.reflect.jvm.internal.**
-dontwarn kotlin.**
-dontwarn javax.annotation.**
-dontwarn org.jetbrains.annotations.**
#
## Google Error Prone annotations (generated by R8)
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi
