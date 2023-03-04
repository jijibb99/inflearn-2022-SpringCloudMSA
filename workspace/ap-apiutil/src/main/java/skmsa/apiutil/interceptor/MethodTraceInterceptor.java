package skmsa.apiutil.interceptor;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * 클래스명 Search시 실제 호출한 클래스명을 찾기 위하여 추가
 * @author Administrator
 *
 */
public class MethodTraceInterceptor extends CustomizableTraceInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
    protected Class<?> getClassForLogging(Object target) {
        Class<?> classForLogging = super.getClassForLogging(target);
        if (SimpleJpaRepository.class.equals(classForLogging)) {
            Class<?>[] interfaces = AopProxyUtils.proxiedUserInterfaces(target);
            if (interfaces.length > 0) {
                return interfaces[0];
            }
        }
        return classForLogging;
    }
}