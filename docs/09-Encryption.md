<style>
.burk {
    background-color: red;
    color: yellow;
    display:inline-block;
}
</style>

# 9 Encryption과 Decryption

- Encryption types
- JCE
- Symmetric Encryption
- Asymmetric Encryption


## 1. Encryption types

Encryption types
1. Symmetric Encryption (Shared)
   - Using the same key
2. Asymmetric Encryption (RSA Keypair)
   - Private and Public Key
   - Using Java keytool
## 2. JCE
## 3. Symmetric Encryption
## 4. Asymmetric Encryption

```

Section 9.
암호화 처리를 위한
Encryption과 Decryption
Encryption types
DEV/UAT/PROD
§ Symmetric Encryption (Shared)
- Using the same key
§ Asymmetric Encryption (RSA Keypair)
- Private and Public Key
- Using Java keytool
Spring Cloud
Config Server
Private Git
Repository
Secure
File
Storage
USER
SERVICE
CATALOG
SERVICE
ORDER
SERVICE
{cipher}dbaf2816ace… {cipher}6817afec2a…
Plain/Clear Text Plain/Clear Text
Java Cryptography Extension (JCE)
§ JCE 사용 시에 다음과 같은 오류 발생할 때, 제한 없는 암호화 정책 파일로 교체
- Illegal key size or default parameters
- Unsupported keysize or algorithm parameters
§ https://www.oracle.com/java/technologies/javase-jce-all-downloads.html
Java Cryptography Extension (JCE)
§ 압축 해제 후, jre/lib/security 폴더로 복사
§ Windows)
- Oracle JDK
- ${user.home}\Program Files\Java\jdk-13.0.2\conf\security
§ MacOS)
- Oracle JDK
- ${user.home}/Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/jre/lib/security
- Open JDK
- ${user.home}/Library/Java/JavaVirtualMachines/openjdk-14.0.2/Contents/Home/conf/security
Symmetric Encryption
§ Config Server의 Dependencies
§ bootstrap.yml
Symmetric Encryption
Symmetric Encryption
§ Users Microservice의 application.yml, bootstrap.yml 수정 à Config Server의 user-service.yml로 이동
Symmetric Encryption
§ Users Microservice의 H2 Database의 Password를 Encryption
Symmetric Encryption
§ Spring Cloud Config Server에서 확인
Symmetric Encryption
§ Spring Cloud Config Server의 user-service.yml 변경 à invalid <n/a>
Asymmetric Encryption
§ Public, Private Key 생성 à JDK keytool 이용
§ $ mkdir ${user.home}/Desktop/Work/keystore
§ $ keytool -genkeypair -alias apiEncryptionKey -keyalg RSA \
-dname "CN=Kenneth Lee, OU=API Development, O=joneconsulting.co.kr, L=Seoul, C=KR" \
-keypass "1q2w3e4r" -keystore apiEncryptionKey.jks -storepass "1q2w3e4r"
Asymmetric Encryption
Asymmetric Encryption
user-service.yml
Asymmetric Encryption
ecommerce.yml
http://127.0.0.1:8888/ecommerce/default
Asymmetric Encryption
AuthorizationHeaderFilter.java
```
