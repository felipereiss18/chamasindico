package br.com.chamasindico.exception;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ChamaSindicoException extends RuntimeException {

    private HttpStatus status;

    public ChamaSindicoException(){
        super("Ocorreu um erro.");
    }

    public ChamaSindicoException(String message){
        super(message);
    }

    public ChamaSindicoException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public ResponseEntity<ResponseDTO> getResponseDTO() {
        return ResponseEntity.status(status != null ? status : HttpStatus.BAD_REQUEST).body(
                new ResponseDTO(null, MessageUtil.tratarMensagem(this.getMessage()))
        );
    }
}
