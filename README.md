Preferhythm
====

[![Download](https://api.bintray.com/packages/kazakago/maven/preferhythm/images/download.svg)](https://bintray.com/kazakago/maven/preferhythm/_latestVersion)
[![Build Status](https://www.bitrise.io/app/5ed0f7b8abbcdca8/status.svg?token=nX7BTDs-VibK-DfRC4jvNQ)](https://www.bitrise.io/app/5ed0f7b8abbcdca8)
[![license](https://img.shields.io/github/license/kazakago/preferhythm.svg)](LICENSE.md)

Automatically generate classes to use Android's SharedPreferences easily and safely.

## Requirement

- Android 4.0.3 (API 15) or later

## Install

Add the following gradle dependency exchanging x.x.x for the latest release.

### Java

```groovy
dependencies {
    compile 'com.kazakago.preferhythm:preferhythm:x.x.x'
    annotationProcessor 'com.kazakago.preferhythm:preferhythm-processor:x.x.x'
}
```

### Kotlin

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    compile 'com.kazakago.preferhythm:preferhythm:x.x.x'
    kapt 'com.kazakago.preferhythm:preferhythm-processor:x.x.x'
}
```

### Tips

If you have not added the `Google maven repository` to the project, please add the following to the gradle file.  
This is included by default in project templates for `Android Studio 3.0` and later. [More details](https://developer.android.com/studio/build/dependencies.html#google-maven).  

```
repositories {
    google()
}
```

## Supported Type

- Primitive Type
  - int
  - long
  - float
  - boolean
- Object Type
  - Integer 
  - Long
  - Float
  - Boolean
  - String
  - Set\<String\>

This is the same as Android's SharedPreferences specification. [More details](https://developer.android.com/training/basics/data-storage/shared-preferences.html).


## Usage

Create a preference model class and add `@PrefClass` annotation.  
Next, define field variable and add `@PrefField` annotation.

```java
@PrefClass // `@PrefClass` is nessesary for your preferences model class.
class MyPreferences {

    @PrefField // `@PrefField` is nessesary for your preference value.
    int intValue = 3; // default value is `3`.

    @PrefField
    boolean booleanValue; // default value is `false`. (primitive default value)

    @PrefField
    String stringValue; // default value is `null`.

    @NonNull
    @PrefField
    String nonNullStringValue = "foo"; // NonNull annotation with default value.

}
```

When building, `[PREFERENCES_MODEL_NAME] + Manager` class is auto generated based on the class with `@PrefClass` annotation.  
Also, Put and get methods are auto created based on the original field name with `@PrefField` annotation.  

```java
public class MainActivity extends Activity {

    // `MyPreferencesManager` class is auto generated.
    private MyPreferencesManager myPreferencesManager = new MyPreferencesManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        System.out.print(myPreferencesManager.getIntValue()); // 3 (default value)

        myPreferencesManager.setIntValue(100); // set 100
        myPreferencesManager.commit(); // commit change.

        System.out.print(myPreferencesManager.getIntValue()); // 100
        
        myPreferencesManager.clear(); // clear value.
        myPreferencesManager.commit(); // commit change.

        System.out.print(myPreferencesManager.getIntValue()); // 3 (default value)
    }

}
```

Refer to the sample module ([Java](https://github.com/KazaKago/Preferhythm/tree/master/samplejava) & [Kotlin](https://github.com/KazaKago/Preferhythm/tree/master/samplekotlin)) for details.

## Important

### Preferences Name

By default, the SharedPreferences name is **Class Name**.  
Therefore, if you change the class name, the saved value can not be retrieved.  

If you want to set the SharedPreferences name to an arbitrary value, please add parameters to `@PrefField`.  

```java
@PrefClass("YOUR_ORIGINAL_PREFERENCES_NAME")
class YourPreferencesClass {

...

}

```

### Key Name

By default, the key name when saving to SharedPreferences is **Field Name**.  
Therefore, if you change the field name, the saved value can not be retrieved.  

If you want to set the key name to an arbitrary value, please add parameters to `@PrefField`.  

```java
@PrefClass
class YourPreferencesClass {

    @PrefField("YOUR_ORIGINAL_KEY_NAME")
    int yourSaveValue;

}

```

## Advanced

### Override Put and Get Method.

you can override put and get method in `[PREFERENCES_MODEL_NAME] + Manager` class.

```java
public class CustomMyPreferencesManager extends MyPreferencesManager {

    public CustomMyPreferencesManager(@NonNull Context context) {
        super(context);
    }
    
    @Nullable
    @Override
    public String getStringValue() {
        // ### Your custom step. (if needed) ###
        // ex) decryption code.
        // ###
        return super.getStringValue();
    }

    @NonNull
    @Override
    public MyPreferencesManager putStringValue(@Nullable String value) {
        // ### Your custom step. (if needed) ###
        // ex) encryption code.
        // ###
        return super.putStringValue(value);
    }

}
```

## Kotlin Support

This library supports Kotlin's Nullable.

```kotlin
@PrefClass
class MyPreferences {

    @PrefField
    val intValue: Int = 3 // NonNull & default value is `3`.

    @PrefField
    val booleanValue: Boolean = false // NonNull & default value is `false`

    @PrefField
    val stringValue: String = "foo" // NonNull & default value is `foo`

    @PrefField
    val nullableStringValue: String? = null // Nullable & default value is `null`

    @PrefField
    val nullableStringWithInitValue: String? = "bar" // Nullable & default value is `bar`

}
```

## License
MIT License

Copyright (c) 2017 KazaKago

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
