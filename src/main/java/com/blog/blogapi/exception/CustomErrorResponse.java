package com.blog.blogapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private Object message;
    private String path;

    public LocalDateTime getTimestamp(){ return timestamp; }
    public int getStatus(){ return status; }
    public String getError(){ return error; }
    public Object getMessage(){ return message; }
    public String getPath(){ return  path; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setStatus(int status) { this.status = status; }
    public void setError(String error) { this.error = error; }
    public void setMessage(Object message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
}
