package gnb.finseta.backend.logging;


public interface IRequestLogger {
	void log(String message);
	void logErr(Throwable err, String message);
}
