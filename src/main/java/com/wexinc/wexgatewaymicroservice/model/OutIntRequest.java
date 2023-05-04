package com.wexinc.wexgatewaymicroservice.model;

import java.util.List;

public class OutIntRequest  {
    private String uniqueIdentifier;
    private String interfaceID;
    private String clientId;



    public String getInterfaceID() {
        return interfaceID;
    }

    public void setInterfaceID(String interfaceID) {
        this.interfaceID = interfaceID;
    }

    @Override public String toString() {
        return super.toString();
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<InterfaceFiles> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<InterfaceFiles> inputFiles) {
        this.inputFiles = inputFiles;
    }

    private List<InterfaceFiles> inputFiles;
}
