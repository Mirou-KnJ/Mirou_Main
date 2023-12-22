# Mirou(Miracle Routine)

## 소개

### 개발 기간 : 10.18 ~ 12.19 (약 9주)

### 팀원 : 김성훈, 장정우

### 개요
우리는 매일 아침 침대에서 일어나 양치하고, 출근길을 걸어 나서는
반복적으고 평범한 일상을 살고 있습니다.
저희는 평범하고 반복적인 일상속에서 지루함을 느끼셨을 분들께
일상의 특별함을 전해드리고 싶었습니다.

매번 달라지는 일상 속 챌린지를 통해 평범한 일상에서 도전정신을 자극하고
특별해진 일상의 재미와 보상을 한번에 경험할 수 있는 서비스,
Mirou(Miracle Routine) 입니다.

## 프로젝트 구현사항
### <회원 기능>
- OAuth2 소셜 로그인 (카카오)
- Remember Me 기반 자동로그인

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/f160adb4-784a-47c3-bfa9-69bfefd9c33f" width="350" height="600"/>



### <관리자 기능>
- 인증 유형별 챌린지 생성 및 관리
- 챌린지 별 보상 생성 및 관리
- 상품 및 상점 등록 및 관리

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/9ba6868b-3834-4c33-a792-f3163e562041" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/9aa354aa-10c8-4afb-b538-a0d608594742" width="400" height="350"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/83be8fb7-5739-4689-a4e3-1b782dc59b2f" width="300" height="500"/>



### <챌린지 기능>
- 챌린지 태그 필터링 조회
- 챌린지 보상 사용자 개별 관리

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/bb7cd3ba-383c-4b04-b696-6eb907cc2209" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/7663878b-eb23-405c-8a0e-d0aa6452e50f" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/9505da9e-2f6d-4715-9dbc-2cba249c5d02" width="350" height="600"/>



### <챌린지 인증 기능>
- 챌린지에 따라 다른 인증 방식 제공
- 이미지 기반 인증
- 현재 위치 기반 인증
- 인증글 조회 및 좋아요, 신고하기 기능

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/31b6de3c-0782-422e-807f-24ea2fa7caa5" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/05920e80-9d7d-4a38-a005-c532f4ab4903" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/08597c9e-41e2-4b55-9e40-d7010ee967f1" width="350" height="600"/>


### <코인, 포인트 기능>
- 챌린지 인증 시 내부 알고리즘에 따라 코인 지급(평균 기대치 : 0.5375N)
- 코인/포인트 히스토리 확인 가능

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/84988e89-fcd6-420a-906a-32e4c2903343" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/a7b8f050-347d-4c61-932a-b472a1d8bab4" width="350" height="600"/>


### <상점 기능>
- 코인을 소비하여 상품 구매
- 구매한 상품을 아이템 보관함에서 확인 및 사용 처리

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/6f3f6432-0d56-4880-8b9d-4833c654248a" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/17c370e7-8154-43a8-8ed5-ececfbd81975" width="350" height="600"/>



### <이미치 처리 기능>
- Object Storage에 이미지 업로드
- 페이지 별 이미지 후처리
- 이미지 라벨 데이터 검출
- 이미지 유효성, 유해성 검사

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/82a35e06-b9b0-4569-bfe7-4b7fdfee1c89" width="350" height="600"/>
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/ac715dd4-b53f-4220-8053-ca5a8e6979a5" width="350" height="600"/>



### <알림 기능>
- 사용자가 특정 기능을 이용했을 경우, Event처리를 통한 자동 알림 생성
- Toastr를 활용한 일반 동작 알림 UI 제공

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/bc3b6114-105e-4248-8c18-9c4779f9f702" width="350" height="600"/>



### <스케줄링 기능>
- 매일 기한이 마감된 챌린지의 종료
- 매일 참여 내용이 만료된 참여정보의 종료
- 매주 월요일 사용자에게 포인트 지급
- 매주 월요일 주간 리포트 알림 제공

<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/2c475cff-5b02-4a7f-93aa-1dcc3fef35d6" width="350" height="600"/>




## 프로젝트 산출물

### 요구사항 명세서
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/3e4204e5-f4b7-4c2f-a1a4-b35283fbc865" width="350" height="600"/>


### 사용 기술
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/0d250d55-9b00-4ad4-a272-6b1c15432fde" width="350" height="600"/>


### 시스템 아키텍처
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/d9b5e36b-f884-4df4-8a04-0cf9fe3ed4a8" width="350" height="600"/>


### ERD 다이어그램
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/ccd6298c-e4ec-4686-8664-377c9659967f" width="350" height="600"/>


### Database
<img src="https://github.com/Mirou-KnJ/Mirou_Main/assets/39723465/eab36e24-befa-48e6-959a-5d88f292dc5c" width="350" height="600"/>


### 배포 주소
Mirou.site
