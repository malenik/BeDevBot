package com.malenik.BeDevBot;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class WatsonAssistantService {

    private static final String IAM_API_KEY = "NbDxPAzi9k6R5Jl0kmDwD-Fto-MgRtLxwoULatXMTk1N";
    private static final String IAM_URL = "https://gateway-wdc.watsonplatform.net/assistant/api";
    private static final String WORKSPACE_ID = "3bd40d70-7e46-426d-a3b8-0d6180f6aebc";
    private static final String VERSION_DATE = "2018-07-10";

    private static final Logger log = LogManager.getLogger("WatsonAssistantService");

    public String ask(String text) {
        IamOptions options = new IamOptions.Builder()
                .apiKey(IAM_API_KEY)
                .build();

        Assistant assistant = new Assistant(VERSION_DATE, options);
        assistant.setEndPoint(IAM_URL);

        InputData input = new InputData.Builder(text).build();
        MessageOptions messageOptions = new MessageOptions.Builder(WORKSPACE_ID)
                .input(input)
                .build();
        MessageResponse response = assistant.message(messageOptions).execute();

        return response.getOutput().getText().stream().collect(Collectors.joining());
    }
}
