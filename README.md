## PDF ENCRYPTOR

A Spring Boot application that adds password protection to pdf files.

## FEATURES

Adds password to pdf files, just send a post request to /pdf endpoint and give file & password.

Then download your encrypted pdf file. You will be able to open encrypted pdf file when you enter correct password .

## TOOLS

- Java 8
- Spring Boot 2.4.0
- Apache PDFBox 2
- Lombok

## COMPILE AND RUN

**Compile**  
Requires **jdk8** and **maven**

    mvn clean install

`target/pdfencryptor-0.0.1-SNAPSHOT.jar` file should be generated.

**Run**

    java -jar target/pdfencryptor-0.0.1-SNAPSHOT.jar  

Application will run from 8090 port.

**Change Port**

You can change port from `src/main/resources/application.properties`
Or give your custom port with `-Dserver.port` parameter like this;

    java -Dserver.port=8888 -jar target/pdfencryptor-0.0.1-SNAPSHOT.jar

## SAMPLE REQUEST

Following curl request sends **sample.pdf** fıle to server for encryptıon. And sets **12345678** as password. Then saves
encrypted pdf file as **sample_encrypted.pdf** .

    curl --location --request POST 'localhost:8090/pdf' \
    --form 'file=@sample.pdf' \
    --form 'password=12345678' -o 'sample_encrypted.pdf'

## DEPLOYMENT

This repository has not deployed to anywhere, just download jar file
from [releases](https://github.com/gulteking/pdfencryptor/releases) and run from your own environment.

## DEVELOPERS

- Mehmet Fatih Gültekin (gultekin.mehmetfatih@gmail.com)

## WITH EXTERNAL TOMCAT

```shell
mvn clean install
```

```shell
cp target/pdfencryptor.war /home/dnl/.sdkman/candidates/tomcat/current/webapps
```

Run tomcat on SDKMAN (pastikan tomcat server yang kita pakai sama versinya dengan versi di pom.xml)

```shell
$HOME/.sdkman/candidates/ \
cd candidates/tomcat/current/bin \
chmod +x catalina.sh \
./catalina.sh run 
```

taruh .war ke

```shell
cd candidates/tomcat/current/webapps
```

- nanti dia akan deploy secara automatis dengan nama sesuai nama file .war nya

- untuk menghapus yang sudah di deploy cukup hapus folder nama aplikasi dan .war nya

```sql
SELECT * FROM TAX_USERS;
SELECT * FROM TRADE_CONFO_USERS;
SELECT * FROM TRADE_LOAN_USERS;
```

```curl
curl --location 'http://localhost:8080/kofax-rpa-limitations/api/v1/hmac/generate-signature' \
--header 'X-API-KEY: A95Jlwb3xfN7NGqhikavGZ5eyYCtcCXlDFvJNA1Ep6zuTKgqRH0pbRdjksoQLyj33StVx5GdhOQ52KmrbIZHbauJ208L9Qk8ayPTcPoixYCyPgi4d6YX7urYjO3ACcHJ' \
--header 'Content-Type: application/json' \
--data-raw '{
    "getAlertByIdentifier": {
        "alertIdentifier": "WLF808-25384108-710815&~@!#$%^&*(){}+=|:'\''<>?/~`"
    },
    "sessionId": "JSESSIONID=NGpURYRU6VTT19QY6TXIaKeNcWmkz_U-ljsL4y13f0fSBGHbtYm7!868568294; path=/;SameSite=NONE; secure; HttpOnly"
}'

curl --location 'http://localhost:8080/kofax-rpa-limitations/api/v1/pdf/encrypt' \
--header 'X-API-KEY: A95Jlwb3xfN7NGqhikavGZ5eyYCtcCXlDFvJNA1Ep6zuTKgqRH0pbRdjksoQLyj33StVx5GdhOQ52KmrbIZHbauJ208L9Qk8ayPTcPoixYCyPgi4d6YX7urYjO3ACcHJ' \
--form 'file=@"/home/yeahbutstill/Downloads/BPJS.pdf"' \
--form 'password="123456789112"'
```