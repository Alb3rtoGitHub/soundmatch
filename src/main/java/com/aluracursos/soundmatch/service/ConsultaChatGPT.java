package com.aluracursos.soundmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

// Para utilizar ChatGPT es necesario una API-KEY y tener crédito en la cuenta.
public class ConsultaChatGPT {
    public static String obtenerInformacion(String texto){
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_APIKEY"));

        CompletionRequest requisicion = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("hablame sobre el artista: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();


        var respuesta = service.createCompletion(requisicion);
        return respuesta.getChoices().get(0).getText();
    }
}
