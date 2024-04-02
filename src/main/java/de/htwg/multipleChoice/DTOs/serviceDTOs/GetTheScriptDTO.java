package de.htwg.multipleChoice.DTOs.serviceDTOs;

import dev.langchain4j.model.output.structured.Description;

public class GetTheScriptDTO {

    @Description("the id of the script as a UUID")
    String scriptId;
    @Description("the name of the script")
    String name;
    @Description("the text of the script or the error message")
    String text;
    @Description("if the script was found or not as a boolean value")
    Boolean success;


    public GetTheScriptDTO(String scriptId, String name, String text, Boolean success) {
        this.scriptId = scriptId;
        this.name = name;
        this.text = text;
        this.success = success;
    }

    public GetTheScriptDTO(String text, Boolean success) {
        this.text = text;
        this.success = success;
    }

    public GetTheScriptDTO() {

    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


}
