package com.whoz_in.main_api.shared.presentation.logging;

import java.lang.annotation.*;

// 이거 컨트롤러의 메서드에 붙이면 요청 바디가 로깅됨
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogBody {
}
