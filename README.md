# gptConversionApp (README-작성중)

## 개발 동기
1. ChatGPT를 한글로 사용할 경우 비교적 느린 답변 속도와 낮은 정확성으로 불편함이 야기될 수 있다.
2. 이를 보완하고자 번역 api를 도입하여 질의 내용을 영문으로 전환하고 전달받은 답변을 사용자의 언어로 변환하여 출력하는 어플리케이션을 개발하고자 하였다.

## 사용 기술
- Frontend : jsp(thymeleaf) / javascript / jquery
- Backend : Java 17, gradle
- Database : Maria DB(예정)
- Deployment : AWS Free tier (예정)
- APIs : OpenAI free tier(model : text-davinci-001), 네이버 파파고 번역 api, 네이버 파파고 언어 감지 api

## 환경설정

### Gradle
build.gradle에 아래 의존성을 주입한다.

1. openAi json exception handling dependency
```
implementation 'com.fasterxml.jackson.core:jackson-core:2.14.2'
implementation 'com.fasterxml.jackson.core:jackson-annotations:2.14.2'
implementation 'com.fasterxml.jackson.core:jackson-databind:2.14.2'
```

2. json data parsing dependency
```
implementation 'com.googlecode.json-simple:json-simple:1.1.1'
```

3. slf4j
```
implementation 'org.slf4j:slf4j-api:1.7.25'
testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.2'
testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
```

## 사용자 커스터마이징
- 기본 논리구조는 PapagoDetectService(질문 언어 감지 서비스) - PapagoTranslateService(질문 번역 서비스) - ChatService(OpenAi 응답 서비스) - PapagoTranslateService(응답 번역 서비스)로 이루어져 있다.



### PapagoDetectService(질문 언어 감지 서비스)
```
private final String papagoClientId = "사용자 clientId key";
private final String papagoClientSecret = "사용자 clientSecret key";
private final String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
```
- 네이버 파파고 api에서 등록한 clientId와 clientSecret값을 주입하여 사용 가능


```
JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
result = (String) jsonObject.get("langCode");
```
- 질문 언어를 감지후 결과 코드 return은 { "langCode" : "ko" }와 같은 json 형식으로 return 받음
- 이를 String으로 parsing하여 결과 코드로서 활용 가능함



### PapagoTranslateService(질문 번역 서비스)
```
private final String papagoClientId = System.getProperty("PapagoClientId");
private final String papagoClientSecret = System.getProperty("PapagoClientSecret");
private final String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
```
- 네이버 파파고 api에서 등록한 clientId와 clientSecret값을 주입하여 사용 가능, 다만 언어 감지 서비스와 다른 key값을 가지므로 관리 유의 요망

```
private String post(String apiUrl, Map<String, String> requestHeaders, String question, String detectLanguage, boolean receivedAnswer){
	    HttpURLConnection con = connect(apiUrl);
	    String postParams = "";
	    if(receivedAnswer == false) {
	    	postParams = "source="+detectLanguage+"&target=en&text="+question;
	    }else {
	    	postParams = "source=en&target="+detectLanguage+"&text="+question;
	    }
      -- additional codes written --
```
- postParam은 번역 request url로, 번역하고자 하는 source 언어와 번역하고자 하는 언어인 target, 실제로 번역 행위가 이루어지는 question이라는 3가지 파라미터로 나뉘어 있다.
- 연속적인 질의응답 번역 과정을 post 메소드를 통한 단일화 작업을 위해 if-else로 분기처리를 진행하였음
- receivedAnswer라는 boolean 변수를 통해 openAi로 부터 답변을 받지 않았을 경우에는 false로 처리하여 target 언어를 en(영어)로 고정하였고, 답변을 받은 후 true로 변할 시 else 로직을 타도록 구성함.

```
if(detectLanguage.equals("en")) {
    String answer = chatService.getChatResponse(question);
}
```
- 만약 최초 질문을 한 언어가 영어(en)일 경우 번역이 지원되지 않으므로 번역 관련 service로직을 생략하고 바로 openAi로 로직을 구성할 필요가 있음


```
translate result return(json)
{
    "message": {
        "@type": "response",
        "@service": "naverservice.nmt.proxy",
        "@version": "1.0.0",
        "result": {
            "srcLangType":"ko",
            "tarLangType":"en",
            "translatedText": "tea"
        }
    }
}

JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
JSONObject resultObject = (JSONObject) ((JSONObject) jsonObject.get("message")).get("result");
String result = (String) resultObject.get("translatedText");
```
- 번역이 완료된 후 return을 받는 json 형식은 2depth로 이루어져 있고, 번역이 완료된 결과는 translatedText에 저장되어있음
- 단일 String을 return받고자 할 때는 json parsing 작업이 필요함



### ChatService(OpenAi 응답서비스)

```
private final String openAiKeys = "사용자 openAiKey";
private final String apiModel = "text-davinci-001";
private final String openAiUri = "https://api.openai.com/v1/completions";
```
- 현 프로젝트에서는 openAi의 무료 제공 모델인 text-davinci-001를 사용하였음
- openAiKeys에 api 사용자가 발급받은 key를 지정하여 openAi 무료 정책 한도를 적용받아 사용 가능


```
ChatGptRequest chatGptRequest = new ChatGptRequest(apiModel, question, {float temperature}, {int max_token});
...
- (float) temperature : chatgpt의 답변 척도, 숫자가 낮을 수록 정형화된 응답이 출력되고 높을 수록 고도화 된 답변이 출력됨. 0~1까지 설정 가능(소수점 첫째자리까지 가능)
- (int) max_token : 답변 글자수 제한, 100까지 출력 가능
