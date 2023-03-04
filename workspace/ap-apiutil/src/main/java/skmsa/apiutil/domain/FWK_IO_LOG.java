package skmsa.apiutil.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//저널 로그
@Document(collection = "fwk_io_log" )
@Data
public class FWK_IO_LOG {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    @Size(max=36)
    private String GUID;                //GUID
    private String parentGUID;          //원인거래의GUID, 없으면 최초거래

    @NotBlank
    @Size(max=10)
    @Indexed(unique=false)

    private String TRAN_ID;             // 거래ID

    private String ctx;                 // Online Ctx
    private String inArg;               // 입력 파라미터
    private String result;              // 처리 결과
    private String stackTrace;          // 오류 발생시 StackTrace

    private long execTime;               // 실행시간
    //발생일자, 처리결과,
}
