package br.com.letscode.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CustomMessage {

    private Integer status;
    private String message;
}
