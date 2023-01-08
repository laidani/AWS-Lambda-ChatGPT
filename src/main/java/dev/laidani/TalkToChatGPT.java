package dev.laidani;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class TalkToChatGPT {
    private static final String MODEL = "text-davinci-003";
    private static final String TOKEN = "sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final OpenAiService OPEN_AI_SERVICE = new OpenAiService(TOKEN);

    public String handleRequest(Event event) {
        final String question = event.getBody();
        return getChatGPTResponse(question);
    }

    private String getChatGPTResponse(String question) {
        CompletionRequest completionRequest = getCompletionRequest(question);
        return Optional.ofNullable(OPEN_AI_SERVICE.createCompletion(completionRequest))
                .map(CompletionResult::getChoices)
                .stream()
                .flatMap(List::stream)
                .map(CompletionChoice::getText)
                .map(this::removeSpaces)
                .collect(Collectors.joining());
    }

    private static CompletionRequest getCompletionRequest(String question) {
        return CompletionRequest.builder()
                .model(MODEL)
                .prompt(question)
                .maxTokens(1000)
                .build();
    }

    private String removeSpaces(String text) {
        return text.replace("\n", "").trim();
    }
}
