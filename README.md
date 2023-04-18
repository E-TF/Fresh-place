### 🌿 *Fresh_Place* 🌿
```
1인가구를 위한 소포장 전문 커머스 서비스입니다. 맛있고 신선한 음식을 구매해보세요!
```
방문 : http://3.37.18.8:8081
![화면](https://user-images.githubusercontent.com/80368511/230976382-583cc4ef-f118-45d8-aca3-d66342d29e56.png)

✅ 서버 구조
---
![deploy](https://user-images.githubusercontent.com/80368511/230966349-1112b1dc-bb71-47b4-ae22-ca722ca7ccb3.png)

✅ ERD
---
![image](https://user-images.githubusercontent.com/80368511/230974079-6e5b31b7-8f22-4a68-b4f1-9ddd3125df42.png)

✅ 중점 사항
---
- 문서화
  + [개요 및 화면 정의](https://github.com/E-TF/Fresh-place/wiki/%F0%9F%8C%BF-Fresh_Place-%F0%9F%8C%BF)
  + [USECASE](https://github.com/E-TF/Fresh-place/wiki/USECASE)
- stateless 한 토큰을 사용한 인증/인가 처리
- BDD Mockito 단위테스트와 통홥 테스트 사용
- Redis Cache를 사용한 카테고리와 상품조회
- 이미지 리사이즈를 CompletableFuture 비동기로 처리하여 관리자가 빠르게 업로드 응답처리
- 관리자 기능 실패 시 메일 받기
- 아이디 중복 문제 Named Lock 을 통해 동시성 해결
- Jenkins를 사용하여 CI/CD 환경 구축
- Docker를 이용하여 CD 구현
- Ngrinder를 이용하여 성능 테스트
 
