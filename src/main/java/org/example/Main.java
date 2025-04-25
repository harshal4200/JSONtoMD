package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Read JSON
        String json = new String(Files.readAllBytes(Paths.get("src/main/resources/demo.json")));

        // 2. Convert JSON to Java object
        ObjectMapper mapper = new ObjectMapper();
        Question question = mapper.readValue(json, Question.class);

        // 3. Prepare Markdown
        StringBuilder md = new StringBuilder();

        md.append("### ").append(question.title).append("\n");
        md.append("**Topic:** ").append(question.topic).append("\n");
        md.append("**Subtopic:** ").append(question.subtopic).append("\n");
        md.append("**Level:** ").append(question.level).append("\n");
        md.append("**Tags:** ").append(question.tags).append("\n\n");

        md.append("#### Description:\n").append(question.description).append("\n\n");

        md.append("### Examples:\n");
        Map<String, String> examplesMap = mapper.readValue(
                question.examples, new TypeReference<Map<String, String>>() {}
        );
        for (String key : examplesMap.keySet()) {
            md.append("- ").append(key).append(":\n```\n").append(examplesMap.get(key)).append("\n```\n");
        }

        md.append("\n### Hints:\n");
        Map<String, String> hintsMap = mapper.readValue(
                question.hints, new TypeReference<Map<String, String>>() {}
        );
        for (String key : hintsMap.keySet()) {
            md.append("- ").append(key).append(": ").append(hintsMap.get(key)).append("\n");
        }

        md.append("\n### Constraints:\n");
        Map<String, String> consMap = mapper.readValue(
                question.constraints, new TypeReference<Map<String, String>>() {}
        );
        for (String key : consMap.keySet()) {
            md.append("- ").append(key).append(": ").append(consMap.get(key)).append("\n");
        }

        md.append("\n### Code Snippets:\n");
        Map<String, String> codeMap = mapper.readValue(
                question.code_snippets, new TypeReference<Map<String, String>>() {}
        );
        for (String lang : codeMap.keySet()) {
            md.append("```").append(lang).append("\n").append(codeMap.get(lang)).append("\n```\n");
        }

        // 4. Save to .md file
        Files.write(Paths.get("questions.md"), md.toString().getBytes());

        System.out.println("✅ Markdown file generated successfully: questions.md");
    }
}
