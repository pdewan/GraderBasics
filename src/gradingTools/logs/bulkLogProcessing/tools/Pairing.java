package gradingTools.logs.bulkLogProcessing.tools;

public  class Pairing <T1, T2>{

	private final T1 t1;
	private final T2 t2;
	
	public Pairing(T1 t1, T2 t2) {
		this.t1=t1;
		this.t2=t2;
	}
	
	public T1 getFirst() {
		return t1;
	}
	
	public T2 getSecond() {
		return t2;
	}
}
