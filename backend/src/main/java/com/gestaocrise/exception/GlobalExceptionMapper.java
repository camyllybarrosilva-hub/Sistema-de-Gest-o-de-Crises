package com.gestaocrise.exception;

import com.gestaocrise.dto.ApiResponseDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        ApiResponseDTO<Object> errorResponse = new ApiResponseDTO<>();
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST;
            errorResponse = new ApiResponseDTO<>(400, exception.getMessage());
        } else if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
            errorResponse = new ApiResponseDTO<>(404, exception.getMessage());
        } else if (exception instanceof ForbiddenException) {
            status = Response.Status.FORBIDDEN;
            errorResponse = new ApiResponseDTO<>(403, exception.getMessage());
        } else if (exception instanceof NotAuthorizedException) {
            status = Response.Status.UNAUTHORIZED;
            errorResponse = new ApiResponseDTO<>(401, "Credenciais inválidas");
        } else {
            errorResponse = new ApiResponseDTO<>(500, "Erro interno do servidor");
        }

        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}
