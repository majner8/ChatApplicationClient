package chatAPP_CommontPart.Data.Util;

public final class Triple<V1,V2,V3> {

	private V1 first;
	private V2 second;
	private V3 third;
	
	
    public Triple(V1 first, V2 second, V3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}



	public V1 getFirst() {
		return first;
	}


	public void setFirst(V1 first) {
		this.first = first;
	}


	public V2 getSecond() {
		return second;
	}


	public void setSecond(V2 second) {
		this.second = second;
	}


	public V3 getThird() {
		return third;
	}


	public void setThird(V3 third) {
		this.third = third;
	}


	public static <V1, V2, V3> Triple<V1, V2, V3> of(V1 first, V2 second, V3 third) {
		return new Triple<V1,V2,V3>(first,second,third);
    }
    
}
