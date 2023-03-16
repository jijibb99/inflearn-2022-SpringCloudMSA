package sample.b.service;


import sample.b.adaptor.kafka.dto.SampleAChanged;

/**
 * Kafka을 비동기 in 채널 처리
 * 각 컨슈머 그룹별로 whenever을 작성하자
 *
 * @author myinno
 */
public interface KafkaComsumerPolicyHandler {

    //이 이벤트는 삭제하자
    void whateverCategoryChanged(String eventString);

    // 강의 분류가 변경되었을때..
    void wheneverCategoryChanged(SampleAChanged sampleAChanged);

}