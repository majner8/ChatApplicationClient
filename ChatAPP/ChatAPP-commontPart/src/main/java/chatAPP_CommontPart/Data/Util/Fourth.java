package chatAPP_CommontPart.Data.Util;



public final class Fourth<V1,V2,V3,V4> {

	private V1 first;
	private V2 second;
	private V3 third;
	private V4 fourth;
	
	
    public Fourth(V1 first, V2 second, V3 third,V4 fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth=fourth;
	}



	public V4 getFourth() {
		return fourth;
	}



	public void setFourth(V4 fourth) {
		this.fourth = fourth;
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


	public static <V1, V2, V3,V4> Fourth<V1, V2, V3,V4> of(V1 first, V2 second, V3 third,V4 fourh) {
		return new Fourth<V1,V2,V3,V4>(first,second,third,fourh);
    }
    


}
