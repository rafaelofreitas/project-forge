package com.projectforge.template.infrastructure.api;

import com.projectforge.template.infrastructure.api.dto.UserInputDTO;
import com.projectforge.template.infrastructure.api.dto.UserOutputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "users")
@Tag(name = "Users")
public interface UserAPI {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new User", description = "Creates a new User in the system.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request body is missing required fields or has invalid data."
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "A validation error occurred. The request body failed validation checks."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. An unexpected error occurred on the server."
                    )
            }
    )
    ResponseEntity<UserOutputDTO> createUser(@Valid @RequestBody UserInputDTO input);

    @GetMapping
    @Operation(summary = "Retrieve a list of Users", description = "Fetches a list of Users.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Users listed successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid parameter(s) were provided."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. An unexpected error occurred on the server."
                    )
            }
    )
    List<UserOutputDTO> getAllUsers();

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Retrieve a User by its identifier",
            description = "Fetches the details of a User based on the provided unique identifier."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The specified identifier does not match any existing User."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unexpected error occurred while processing the request."
                    )
            }
    )
    ResponseEntity<UserOutputDTO> getById(
            @Schema(
                    name = "id",
                    description = "The unique identifier of the User.",
                    example = "123",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @PathVariable(name = "id") Long id
    );

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update a User by its identifier",
            description = "Updates the details of an existing User based on the provided unique identifier."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The request body contains invalid data or is missing required fields."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The specified identifier does not match any existing User."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unexpected error occurred while processing the request."
                    )
            }
    )
    ResponseEntity<UserOutputDTO> updateById(
            @Schema(
                    name = "id",
                    description = "The unique identifier of the User.",
                    example = "123",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @PathVariable(name = "id") Long id, @Valid @RequestBody UserInputDTO input
    );

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a User by its identifier",
            description = "Deletes the User with the specified unique identifier."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The specified identifier does not match any existing User."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unexpected error occurred while processing the request."
                    )
            }
    )
    ResponseEntity<Void> deleteById(
            @Schema(
                    name = "id",
                    description = "The unique identifier of the User.",
                    example = "123",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @PathVariable(name = "id") Long id
    );
}
