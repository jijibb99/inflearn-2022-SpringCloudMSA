# Z. 추가할 내용
MSA 1.0을 위해서 필요한 내용 (2주간 완성될 내용)
1. 패키지 구조
2. 매핑 정리 (DTO, VO)
3. 카프카 CDC, Queue 직접 연동
4. 공통 유틸
5. POM 공통 부분 별도 분리


## 1. MSA 발표 가능한 최소 사영
1. 표준
   - 레이어 구조
   - Package 구조정의
   - 명명 표준
   - 도메인(인스턴스)
   - 공통 모듈 관리 방안
   - 모듈 Lib 관리 방안
   - 표준 전문 / CommonArea
   - Java 코딩 표준
2. 시스템 인터페이스
   - 동기  : Feign 
   - 비동기 : kafka, rabbitmq
     - Multi 처리 방안
     - 순서 보장 방안
     - 대사 방안
     - kafka Connector
3. 데이터 정합성
   - saga 패턴 구현
   - 개별 업무 보상 거래
4. 인증 관리
   - token
   - Spring Security
5. 시스템 공통 모듈 표준 정의
   - 매핑 대인
   - Swagger 표준 정의
   - JPA
                      

## 2. 다음으로 기약하는 내용

1. Kubernetes, Cloud로 이사하기
2. istio 적용하기이
3. DevOps 적용하기
- 