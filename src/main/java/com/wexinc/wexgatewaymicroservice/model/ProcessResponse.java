package com.wexinc.wexgatewaymicroservice.model;

import java.util.List;

public class ProcessResponse {
    private String uniqueIdentifier;
    private String interfaceID;
    private String clientId;

    private String errorMessage;

    private String result;

    private String successMessage;

    public List<InterfaceFiles> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<InterfaceFiles> inputFiles) {
        this.inputFiles = inputFiles;
    }

    private List<InterfaceFiles> inputFiles;
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getInterfaceID() {
        return interfaceID;
    }

    public void setInterfaceID(String interfaceID) {
        this.interfaceID = interfaceID;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
