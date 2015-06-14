import java.util.Map;
import java.util.HashMap;


public class Tester {
	private static final String DEFAULT_FILE_NAME = "result.txt";
	
	public static void test() {
		int precision = 10000;
		ThreadManager threadManager;
		for (int i = 2; i < 8; i++) {
			threadManager = new ThreadManager(i,
					precision, true);
			threadManager.start("result10000.txt");
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Map<String, String> d = new HashMap<String, String>();
		
		d.put("-p", "100");
		d.put("-q", "true");
		d.put("-t", "2");
		d.put("-o", DEFAULT_FILE_NAME);
		for (int i = 0; i < args.length-1; i+=2) {
			d.put(args[i], args[i+1]);
		}
		
		boolean isQuiet = Boolean.parseBoolean(d.get("-q"));
		int numberOfThreads = Integer.parseInt(d.get("-t"));
		int precision = Integer.parseInt(d.get("-p"));
		String fileName = d.get("-o");
		ThreadManager threadManager = new ThreadManager(numberOfThreads,
				precision, isQuiet);
		threadManager.linearCalculation();
		threadManager.parallelCalculation();
		//threadManager.start(fileName);
		threadManager.getInfo();
		//test();

	}

}
