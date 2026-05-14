package estudo.pessoa.cadastro.config;

import estudo.pessoa.cadastro.dto.ErrorResponse;
import estudo.pessoa.cadastro.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class CadastroControllerAdvice {

	@ExceptionHandler(CepInvalidoException.class)
	public ResponseEntity<ErrorResponse> handleCepInvalidoException(CepInvalidoException ex) {
		ErrorResponse error = new ErrorResponse(400, ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CpfInvalidoException.class)
	public ResponseEntity<ErrorResponse> handleCpfInvalidoException(CpfInvalidoException ex) {
		ErrorResponse error = new ErrorResponse(400, ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<ErrorResponse> handleValidacaoException(ValidacaoException ex) {
		ErrorResponse error = new ErrorResponse(400, ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErrorResponse> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
		ErrorResponse error = new ErrorResponse(404, ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ErroInternoException.class)
	public ResponseEntity<ErrorResponse> handleErroInternoException(ErroInternoException ex) {
		ErrorResponse error = new ErrorResponse(500, ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(CadastroException.class)
	public ResponseEntity<ErrorResponse> handleCadastroException(CadastroException ex) {
		ErrorResponse error = new ErrorResponse(ex.getStatusCodeValue(), ex.getMensagem());
		return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatusCodeValue()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse error = new ErrorResponse(
				500,
				"Erro interno do servidor",
				"Ocorreu um erro inesperado. Por favor, tente novamente mais tarde."
		);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
