

package sample.b.service;

import sample.b.adaptor.kafka.dto.SampleAChanged;
import skmsa.apiutil.interceptor.OnlineContext;

public interface SampleBAsyncProcessService {
    void saveSampleAChanged(OnlineContext ctx, SampleAChanged sampleAChanged);
}
