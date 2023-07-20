## ViewController
- thymeleaf template 화면을 return 하는 view 관련 controller

## ChatController
- thymeleaf 화면으로부터 비동기로 전달받은 json data를 service 로직을 통해 질의 및 번역된 답변 정보를 받아 return 해주는 controller

(
@PostMapping("")
public String test(@RequestBody String question) throws IOException, InterruptedException{

private final ChatService chatService;
    private final PapagoTranslateService papagoTranslateService;

log.info(question);

// 사용자 언어 감지 & 번역
question = papagoTranslateService.doTranslate(question);

// 답변 수령(영문)
String answer = chatService.getChatResponse(question);

// 답변 번역
answer = papagoTranslateService.doTranslate(answer);

log.info(answer);

return answer;
}
)
