package com.example.hugbo_team_13.helper;

public class PictureData {
    private final byte[] data;
    private final String contentType;

    public PictureData(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public byte[] getData() { return data; }
    public String getContentType() { return contentType; }
}