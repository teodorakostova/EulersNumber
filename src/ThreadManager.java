import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ThreadManager {
	private int numberOfThreads;
	//number of additions
	private int precision;
	//execution time for single-threaded calculation
	private long T1;
	// execution time for calculation with p threads
	private long Tp;
	private double Sp;
	private double Ep;
	private BigDecimal result;
	private boolean isQuiet;
	
	
	
	public ThreadManager(int numberOfThreads, int precision, boolean isQuiet) {
		this.numberOfThreads = numberOfThreads;
		this.precision = precision;
		this.result = BigDecimal.ZERO;
		this.isQuiet = isQuiet;
	}
	
	
	public void start(String fileName) {
		parallelCalculation();
		if (!isQuiet) {			
			getInfo();
		}
		saveResultInStream(fileName);
	}
	
	public void getInfo() {
		System.out.println("T1 " + T1);
		System.out.println("Tp, p = " + numberOfThreads + ": " + Tp);
		System.out.println("Acceleration Sp: " + Sp);
		System.out.println("Efficiency Ep: " + Ep);
		//System.out.println("Result " + result);
	}
	
	//execution time for single-threaded calculation
	public long getT1() {
		return T1;
	}
	
	// execution time for calculation with p threads
	public long getTp() {
		return Tp;
	}
	
	// acceleration for p-threaded calculation
	public void calculateSp() {
		if (T1 == 0)
			linearCalculation();
		if (Tp == 0)
			parallelCalculation();
		Sp = (double)T1/Tp;
	}
	
	// efficiency for p-threaded calculation
	public void calculateEp() {
		Ep = (double)Sp/numberOfThreads; 
	}
	
	public BigDecimal getResult() {
		if (result.equals(0))
			parallelCalculation();
		return result;
	}
	
	public void linearCalculation() {
		long t1 = System.currentTimeMillis();
		Map<String, BigDecimal> factorialCacheMap = new HashMap<String, BigDecimal>();
		CalculatingThread thread = new CalculatingThread(0, precision, factorialCacheMap);
		thread.doAddition();
		
		long t2 = System.currentTimeMillis();
		T1 = t2 - t1;
		
	}
	
	public void parallelCalculation() {
		Map<String, BigDecimal> factorialCacheMap = new HashMap<String, BigDecimal>();
		CalculatingThread calculatingThreads[] = new CalculatingThread[numberOfThreads];
		calculatingThreads[0] = new CalculatingThread(0, precision/numberOfThreads, factorialCacheMap);
		for (int i = 1; i < numberOfThreads; i++) {
			int start = calculatingThreads[i-1].getStart() + precision/numberOfThreads;
			int end = start + precision/numberOfThreads;
			calculatingThreads[i] = new CalculatingThread(start, end, factorialCacheMap);
			
		}
		Thread workers[] = new Thread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			workers[i] = new Thread(calculatingThreads[i]);
		}
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < numberOfThreads; i++) {
			workers[i].start();
			calculatingThreads[i].thread_id = workers[i].getId();
			if (!isQuiet) {
				System.out.println("Thread " + workers[i].getId() + " started");
				System.out.println("Calculating from " + calculatingThreads[i].getStart() +
						 " to " + calculatingThreads[i].getStop() );
			}	
		}
		for (int i = 0; i < workers.length; i++) {
			try {
				workers[i].join();
				if (!isQuiet) {
					System.out.println("Thread " + workers[i].getId() + " finished");
				}
			} catch (InterruptedException e) {
				System.err.println("INTERRUPTED");
				e.printStackTrace();
			}
		}
		
		long t2 = System.currentTimeMillis();
		Tp = t2 - t1;
		for (int i = 0; i < workers.length; i++) {
			result = result.add(calculatingThreads[i].getResult());
		}
		
		calculateSp();
		calculateEp();
		
	}
	
	
	public void saveResultInStream(String fileName) {
		File file = new File(fileName);
		try {
			if (file.exists() == false) {
				file.createNewFile();
			}
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			//bufferedWriter.write(result.toPlainString());
			bufferedWriter.write(precision);
			bufferedWriter.newLine();
			bufferedWriter.write("T1 " + T1);
			bufferedWriter.newLine();
			bufferedWriter.write("Tp, p = " + numberOfThreads 
					+ " : " + Tp);
			bufferedWriter.newLine();
			bufferedWriter.write("Acceleration Sp: " + Sp);
			bufferedWriter.newLine();
			bufferedWriter.write("Efficiency Ep: " + Ep);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
