package gnb.finseta.backend.logging.impl;

import gnb.finseta.backend.logging.IRequestLogger;
import org.springframework.stereotype.Component;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequestLogger implements IRequestLogger {
	Logger logger = LoggerFactory.getLogger(RequestLogger.class);

	@Override
	public void log(String message) {
		logger.info(message);
	}

	@Override
	public void logErr(Throwable err, String message) {
		logger.error(message, err);
	}
}
