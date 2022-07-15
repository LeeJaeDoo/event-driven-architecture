# Event-Driven-Architecture

1. mysql schema 생성
   - 'point' database 생성
   - 'point' database 내에서 ./point/src/main/java/com/jd/sql/point.sql 내 쿼리 실행
2. docker 설치
3. ./docker-compose.yml 실행(local-kafka 구동)
4. event/point gradle bootrun
5. ./event/src/main/java/com/jd/http/event.http 에서 이벤트 생성 rest api 호출 
6. ./point/src/main/java/com/jd/http/point.http 에서 누적 포인트 총합 조회 api 호출
