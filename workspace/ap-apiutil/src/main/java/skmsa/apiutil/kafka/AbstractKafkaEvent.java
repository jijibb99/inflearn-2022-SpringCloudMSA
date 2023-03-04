package skmsa.apiutil.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

// 메시지 전송시에만 사용
public class AbstractKafkaEvent {

    String eventType;
    String timestamp;

    public AbstractKafkaEvent() {
        this.setEventType(this.getClass().getSimpleName());
        //Event에 String 형식으로 시간 저장
        SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = defaultSimpleDateFormat.format(new Date());
//        this.timestamp = System.currentTimeMillis();
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return json;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean validate() {
        return getEventType().equals(getClass().getSimpleName());
    }

}
