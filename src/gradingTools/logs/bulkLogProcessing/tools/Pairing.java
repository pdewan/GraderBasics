package gradingTools.logs.bulkLogProcessing.tools;

public  class Pairing <T1, T2>{

	public T1 first;
	public T2 second;
	
	public Pairing(T1 t1, T2 t2) {
		first=t1;
		second=t2;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
}
