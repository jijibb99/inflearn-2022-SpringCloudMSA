package sample.a.config.controller.errors;

public enum ITLogicException_ERR_CODE {
    IF_DATA_INVALID,  	// 데이터 유효값 오류(미 정의된 유효값으로 I/F 요청)
    IF_DATA_FORMAT,   	// 데이터 Format 오류
    IF_DATA_VALIDATION, // 데이터 정합성 오류 (ex: 최소값 > 최대값)
}
