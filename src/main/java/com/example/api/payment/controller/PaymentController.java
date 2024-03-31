package com.example.api.payment.controller;

import com.example.api.member.MemberDTO;
import com.example.api.payment.dto.PaymentCallbackRequest;
import com.example.api.payment.service.PaymentService;
import com.example.core.exception.ExceptionResponse;
import com.example.core.web.security.login.LoginMember;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "결제", description = "결제 관련 API입니다.")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "결제 검증", description = "아임포트로 실제 결제한 내용과 DB에 저장된 결제가 일치하는 지 검증하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 결제는 유효한 결제입니다.", content = @Content(schema = @Schema(implementation = IamportResponse.class))),
            @ApiResponse(responseCode = "400", description = "결제 금액이 양수가 아닌 경우 발생하는 에러입니다.", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "결제 또는 예약이 DB에 존재하지 않는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "502", description = "아임포트에서 결제를 찾을 수 없거나 삭제할 수 없는 에러 또는 결제 미완료, 결제 금액 위변조 의심 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    }
    )
    public ResponseEntity<IamportResponse<Payment>> validationPayment(
            @Parameter(hidden = true) @LoginMember MemberDTO loginMember,
            @Valid @RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(loginMember, request);

        log.info("결제 응답 = {}", iamportResponse.getResponse().toString());

        return ResponseEntity.ok(iamportResponse);
    }
}