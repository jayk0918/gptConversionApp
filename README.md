# gptConversionApp (README-작성중)

## 개발 동기
1. ChatGPT를 한글로 사용할 경우 비교적 느린 답변 속도와 낮은 정확성으로 불편함이 야기될 수 있다.
2. 이를 보완하고자 번역 api를 도입하여 질의 내용을 영문으로 전환하고 전달받은 답변을 사용자의 언어로 변환하여 출력하는 어플리케이션을 개발하고자 하였다.


## 개발 목표
1. ChatGPT에 들어가는 질의 내용은 사용자의 선호에 따라 입력받을 수 있게 한다.
2. 번역 API를 구성하여 사용자의 질의 내용을 영어로 번역하고, 회신 답변을 사용자가 질의한 언어로 번역한다.
3. ChatGPT API (OpenAI API)를 도입하여 사용자가 Application에서 질의응답을 할 수 있도록 구성한다.
4. 사용자에게 원하는 번역 API를 선택할 수 있게 구성한다.
5. 회원/비회원의 구분 없이 사용은 가능하나 회원의 경우 개인화하여 본인의 질의응답 기록을 저장하고 사용자가 호출 시 과거 질의응답 이력을 조회할 수 있게 한다.


## 사용 기술
- Frontend : jsp(thymeleaf) / javascript / jquery
- Backend : Java 17, gradle
- IDE : Spring Tools Suite 4
- Database : Maria DB(예정)
- Deployment : AWS Free tier (예정)
