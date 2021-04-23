package br.com.chamasindico.dto.arquitetura;

import br.com.chamasindico.utils.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private Object data;

    private String message = "Sucesso!";

    public ResponseDTO(Object data){
        this.data = data;
    }

    public ResponseDTO(String message){
        this.message = message;
    }

    public String getMessage() {
        return MessageUtil.tratarMensagem(this.message);
    }

}
