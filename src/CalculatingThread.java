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
	
	public CalculatingThread(int start, int stop) {
		this.start = start;
		this.stop = stop;
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
		for (int i = 2; i <= number; i++) {
			
			res = res.multiply(new BigDecimal(i));
		}
		return res;
	}
	
	
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
