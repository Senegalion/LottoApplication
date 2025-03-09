package org.example.apivalidationerror;

import org.example.BaseIntegrationTest;
import org.example.infrastructure.apivalidation.ApiValidationErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    public static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";
    public static final String INPUT_NUMBERS_MUST_NOT_BE_NULL = "INPUT NUMBERS MUST NOT BE NULL";
    public static final String INPUT_NUMBERS_MUST_NOT_BE_EMPTY = "INPUT NUMBERS MUST NOT BE EMPTY";

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_input_numbers() throws Exception {
        // given
        // when
        ResultActions perform = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                .content("""
                        {
                        "inputNumbers" : []
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.errorMessages()).containsExactlyInAnyOrder(
                INPUT_NUMBERS_MUST_NOT_BE_EMPTY
        );
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_does_not_have_input_numbers() throws Exception {
        // given
        // when
        ResultActions perform = mockMvc.perform(post(INPUT_NUMBERS_ENDPOINT)
                .content("""
                        {
                        }
                        """.trim()
                )
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.errorMessages()).containsExactlyInAnyOrder(
                INPUT_NUMBERS_MUST_NOT_BE_NULL,
                INPUT_NUMBERS_MUST_NOT_BE_EMPTY
        );
    }
}
