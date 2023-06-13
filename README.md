# File-Market-Backend

## How to use API Demo
1. 해당 repository clone
2. gradle 설치 및 동기화 wait
3. 터미널 경로를 clone 받은 repository로
```
$ ./gradlew clean bootJar
$ JAR_PATH=./build/libs/your-project-name.jar
$ VERSION=$(./gradlew -q printVersion)
$ SNAPSHOT_JAR=your-project-name-$VERSION-SNAPSHOT.jar
$ cp $JAR_PATH $SNAPSHOT_JAR
```

이후 https://github.com/CloudComputingD/Local-Deployment를 참고하여 데모 사용이 가능합니다. 
