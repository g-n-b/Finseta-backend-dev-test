package gnb.finseta.backend.logging;

import java.util.Map;

public interface IRequestLogger {
	void log(String message);
	void logErr(Throwable err, String message);
}
