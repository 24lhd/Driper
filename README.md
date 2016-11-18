# Driper
## Video demo
https://www.youtube.com/watch?v=qbS1mapIyso

## Installation
In my 'build.gradle'

```groovy
dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    compile files('libs/jsoup-1.9.2.jar')
    compile 'com.google.android.gms:play-services:9.6.1'
    compile 'com.google.firebase:firebase-database:9.6.1'
    compile 'com.google.firebase:firebase-auth:9.6.1'
    compile 'com.android.support:cardview-v7:24.2.1'
    compile 'com.google.firebase:firebase-storage:9.6.1'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.android.support:multidex:+'
    compile project(':WrappingLayout')
    compile project(':SlidingUpPanel')
    compile 'com.android.support:design:25.0.0'
    compile 'com.android.support:support-v4:25.0.0'
    compile 'com.android.support:appcompat-v7:25.0.0'
    compile 'com.android.support:support-annotations:25.0.0'
}
```












