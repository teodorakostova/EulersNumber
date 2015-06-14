import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.HashMap;


public class CalculatingThread implements Runnable {
	private int start;
	private int stop;
	private BigDecimal result;
	private BigDecimal factoriel;
	public long thread_id = 0;
	private Map<String, BigDecimal> factorialCacheMap;
	
	public CalculatingThread(int start, int stop, Map<String, BigDecimal> factorialCacheMap) {
		this.start = start;
		this.stop = stop;
		this.factorialCacheMap = factorialCacheMap;
		this.result = BigDecimal.ZERO;
	}
	
	public BigDecimal getFactoriel() {
		return factoriel;
	}
	
	public void run() {
		doAddition();
	}
	
	public int getStart() {
		return start;
	}

	public int getStop() {
		return stop;
	}

	public BigDecimal getResult() {
		return result;
	}
	
	public BigDecimal factoriel(int number) {
		BigDecimal res = BigDecimal.ONE;
		if (number == 0) {
			factorialCacheMap.put(String.valueOf(number), res);
			return res;
		}
		int i = 1;
		for (int j = number-1; j > 0; j--) {
			if (factorialCacheMap.containsKey(String.valueOf(j))) {
				res = factorialCacheMap.get(String.valueOf(j));
				i = j;
				break;
			}
		}
		for (; i <= number; i++) {
			res = res.multiply(new BigDecimal(i));
			factorialCacheMap.put(String.valueOf(i), res);
		}
		return res;
	}
	
	// Tp, p = 2: 122 - 100
	public void doAddition() {
		int i = start;
		while (i < stop) {
			result = result.add(singleTask(i));
			i++;
		}
	}
	
	public BigDecimal singleTask(int k) {		
		return (new BigDecimal(3 - 4 * Math.pow(k, 2))).divide(factoriel(2 * k + 1), MathContext.DECIMAL64);
	}

	
}
