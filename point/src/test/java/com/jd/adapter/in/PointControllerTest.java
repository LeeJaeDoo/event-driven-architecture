package com.jd.adapter.in;

import com.jd.adapter.in.model.PointTotalResponse;
import com.jd.application.port.in.PointHistoryQueryUseCase;
import com.jd.domain.PointHistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jaedoo Lee
 */
@WebMvcTest(PointController.class)
@ExtendWith(SpringExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointHistoryQueryUseCase pointHistoryQueryUseCase;

    @Test
    @DisplayName("누적 포인트 총점 조회")
    public void getPointTotal() throws Exception {
        // given
        given(pointHistoryQueryUseCase.getTotalPoint(any())).willReturn(PointTotalResponse.of(List.of(new PointHistory())));;

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/points/users/{userId}/total", "3ede0ef2-92b7-4817-a5f3-0c575361f745").accept(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isOk());
    }


}
