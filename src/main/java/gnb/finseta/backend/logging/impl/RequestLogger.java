package gnb.finseta.backend.logging.impl;

import gnb.finseta.backend.logging.IRequestLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class RequestLogger implements IRequestLogger {
	private static final String MSG_FORMAT = """
			<log>
			<requestId>
				%s
			</requestId>
			<message>
				%s
			</message>
			</log>
			""";
	Logger logger = LoggerFactory.getLogger(RequestLogger.class);

	@Override
	public void log(String message) {
		logger.info(MSG_FORMAT.formatted(MDC.get("RequestId"), message));
	}

	@Override
	public void logErr(Throwable err, String message) {
		logger.error(MSG_FORMAT.formatted(MDC.get("RequestId"), message), err);
	}
}
